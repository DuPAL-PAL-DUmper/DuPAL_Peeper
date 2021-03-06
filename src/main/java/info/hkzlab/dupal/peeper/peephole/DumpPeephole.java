package info.hkzlab.dupal.peeper.peephole;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.hkzlab.dupal.peeper.devices.PALSpecs;
import info.hkzlab.dupal.peeper.exceptions.PeepholeException;
import info.hkzlab.dupal.peeper.parser.DumpParser;
import info.hkzlab.dupal.peeper.parser.states.*;
import info.hkzlab.dupal.peeper.utilities.BitUtils;

public class DumpPeephole implements Peephole {
    private static final Logger logger = LoggerFactory.getLogger(DumpPeephole.class);

    private PALSpecs pSpecs = null;
    private boolean is24Pins = false;
    private boolean simplePAL = false;

    private int lastWrite = 0;
    private int IOasOUTMask = 0;

    /* Simple PALs */
    private Map<Integer, SimpleState> ssMap = null;

    /* Complex PALs */
    private Map<OutStatePins, Map<Integer, RLink>> osRLMap = null;
    private Map<OutStatePins, Map<Integer, OLink>> osOLMap = null;
    private OutStatePins curOS = null;

    public DumpPeephole(JSONObject dumpRoot) throws PeepholeException {
        pSpecs = DumpParser.getPALType(dumpRoot);

        if(pSpecs == null) throw new PeepholeException("Unable to extract PAL type from dump.");

        is24Pins = pSpecs.getPinCount_IN() > 10;

        if(pSpecs.getPinCount_IO() > 0 || pSpecs.getPinCount_RO() > 0) {
            logger.info("DumpPeephole -> Complex PAL");

            osRLMap = new HashMap<>();
            osOLMap = new HashMap<>();

            simplePAL = false;
            RLink[] rlArray = DumpParser.extractRLinks(dumpRoot);
            OLink[] olArray = DumpParser.extractOLinks(dumpRoot);
            IOasOUTMask = DumpParser.extractIOasOutMask(dumpRoot);
            logger.info("DumpPeephole -> IO as Outputs: " + String.format("%06X", IOasOUTMask));

            // Build a map associating an OutState with simple Link connections
            logger.info("DumpPeephole -> Build a map for OLinks");
            for(OLink ol : olArray) {
                Map<Integer, OLink> olMap = osOLMap.get(ol.src);

                if(olMap == null) {        // TODO Auto-generated method stub

                    olMap = new HashMap<>();
                    osOLMap.put(ol.src, olMap);
                }

                olMap.put(ol.inputs, ol);
            }

            // Build a similar map for registered links
            logger.info("DumpPeephole -> Build a map for RLinks");
            for(RLink rl : rlArray) {
                Map<Integer, RLink> rlMap = osRLMap.get(rl.src);
                
                if(rlMap == null) {
                    rlMap = new HashMap<>();
                    osRLMap.put(rl.src, rlMap);
                }

                rlMap.put(rl.inputs, rl);
            }

            // Initialize the start state
            curOS = (olArray.length > 0) ? olArray[0].src : rlArray[0].src;
        } else {
            logger.info("DumpPeephole -> Simple PAL");

            simplePAL = true;
            SimpleState[] ssArray = DumpParser.extractSimpleStates(dumpRoot);
            Map<Integer, SimpleState> ssTmpMap = new HashMap<>();

            // Build a map to access the states by input
            for(SimpleState ss : ssArray) ssTmpMap.put(Integer.valueOf(ss.inputs), ss);

            ssMap = Collections.unmodifiableMap(ssTmpMap);
        }        
    }

    @Override
    public void write(boolean[] pins) throws PeepholeException {
        int data = BitUtils.build_WriteMaskFromPins(pins);
        lastWrite = data;
        data &= ~(IOasOUTMask | pSpecs.getMask_O() | pSpecs.getMask_RO() | pSpecs.getMask_OE()); // Clean the written data from output pins

        if(!simplePAL) {
            // Check if this is actually a clock pulse
            if((data & pSpecs.getMask_CLK()) != 0) clock(pins);
            else {
                data &= ~(pSpecs.getMask_CLK());
                Map<Integer,OLink> olMap = osOLMap.get(curOS);
                OLink ol = olMap.get(Integer.valueOf(data));
                curOS = ol.dst;
                logger.info("write() -> " + String.format("%06X", data) + " new OS: " + curOS.toString());
            }
        }
    }

    @Override
    public boolean[] read() throws PeepholeException {
        if(simplePAL) {
            int wrIdx = lastWrite & ~(IOasOUTMask | pSpecs.getMask_O() | pSpecs.getMask_RO() | pSpecs.getMask_OE() | pSpecs.getMask_CLK());
            SimpleState ss = ssMap.get(Integer.valueOf(wrIdx));
            int hiz_forced = (lastWrite >> DumpParser.MASK_SHIFT) & ss.outputs.hiz;

            return BitUtils.build_ReadPinsArrayFromMask(ss.outputs.out | hiz_forced, is24Pins);
        } else {
            boolean oe_disabled = (lastWrite & pSpecs.getMask_OE()) != 0;
            int out = curOS.out;

            logger.info("read() -> Complex PAL. /OE? " + oe_disabled + " raw out:" + String.format("%02X", out) + " hiz:" + String.format("%02X", curOS.hiz));

            // If /OE is not enabled (low), overwrite the Q outputs with what we're setting in the inputs
            if(oe_disabled) {
                out &= ~(pSpecs.getMask_RO() >> DumpParser.MASK_SHIFT);
                out |= (lastWrite & pSpecs.getMask_RO()) >> DumpParser.MASK_SHIFT;
            }

            // Clear the pins that are HI-Z, shouldn't be necessary, but let's make sure
            out &= ~curOS.hiz;

            // Clear the IO pins that are actually inputs
            out &= ((IOasOUTMask | pSpecs.getMask_RO() | pSpecs.getMask_O()) >> DumpParser.MASK_SHIFT) & 0xFF;

            // Registered Outputs can be toggled off by the /OE pin, so make sure of its status (if present) and fake the outputs as hi-z
            int hiz_forced = (lastWrite >> DumpParser.MASK_SHIFT) & curOS.hiz;
            int IOasIN = ((lastWrite & pSpecs.getMask_IO()) & ~IOasOUTMask) >> DumpParser.MASK_SHIFT;

            return BitUtils.build_ReadPinsArrayFromMask(out | hiz_forced | IOasIN, is24Pins);
        }
    }

    @Override
    public void clock(boolean[] pins) throws PeepholeException {
        int data = BitUtils.build_WriteMaskFromPins(pins);
        lastWrite = data;
        data &= ~(IOasOUTMask | pSpecs.getMask_O() | pSpecs.getMask_RO() | pSpecs.getMask_CLK() | pSpecs.getMask_OE()); // Clean the written data from output pins

        if(simplePAL) throw new PeepholeException("Clock command on a PAL that does not support it!");

        Map<Integer,RLink> rlMap = osRLMap.get(curOS);
        RLink rl = rlMap.get(Integer.valueOf(data));
        curOS = rl.dst;
        
        logger.info("clock() -> " + String.format("%06X", data) + " new OS: " + curOS.toString());
    }

    @Override
    public boolean open() throws PeepholeException {
        logger.info("DumpPeephole -> open()");
        return true;
    }

    @Override
    public void close() {
        logger.info("DumpPeephole -> close()");
    }

    @Override
    public PALSpecs getSpecs() {
        return pSpecs;
    }

    @Override
    public String getDeviceName() {
        return pSpecs.getDeviceName() + " (SIM)" ;
    }
    
}
