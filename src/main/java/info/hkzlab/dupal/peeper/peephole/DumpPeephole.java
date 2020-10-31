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

    public DumpPeephole(JSONObject dumpRoot) {
        pSpecs = DumpParser.getPALType(dumpRoot);
        is24Pins = pSpecs.getPinCount_IN() > 10;

        if(pSpecs.getPinCount_IO() > 0 || pSpecs.getPinCount_RO() > 0) {
            logger.info("DumpPeephole -> Complex PAL");

            osRLMap = new HashMap<>();
            osOLMap = new HashMap<>();

            simplePAL = false;
            RLink[] rlArray = DumpParser.extractRLinks(dumpRoot);
            OLink[] olArray = DumpParser.extractOLinks(dumpRoot);
            IOasOUTMask = DumpParser.extractIOasOutMask(dumpRoot);

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
        data &= ~(IOasOUTMask | pSpecs.getMask_O() | pSpecs.getMask_RO()); // Clean the written data from output pins

        if(!simplePAL) {
            // Check if this is actually a clock pulse
            if((data & pSpecs.getMask_CLK()) != 0) clock(pins);
            else {
                Map<Integer,OLink> olMap = osOLMap.get(curOS);
                OLink ol = olMap.get(Integer.valueOf(data));
                curOS = ol.dst;
            }
        }
    }

    @Override
    public boolean[] read() throws PeepholeException {
        if(simplePAL) {
            int wrIdx = lastWrite & ~(IOasOUTMask | pSpecs.getMask_O() | pSpecs.getMask_RO());
            SimpleState ss = ssMap.get(Integer.valueOf(wrIdx));
            int readVal = ss.outputs.out;
            int hiz_forced = (lastWrite >> DumpParser.MASK_SHIFT) & ss.outputs.hiz;
            readVal |= hiz_forced;

            return BitUtils.build_ReadPinsArrayFromMask(readVal, is24Pins);
        }

        return null;
    }

    @Override
    public void clock(boolean[] pins) throws PeepholeException {
        int data = BitUtils.build_WriteMaskFromPins(pins);
        lastWrite = data;
        data &= ~(IOasOUTMask | pSpecs.getMask_O() | pSpecs.getMask_RO()); // Clean the written data from output pins

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
    
}
