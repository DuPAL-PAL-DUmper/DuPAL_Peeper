package info.hkzlab.dupal.peeper.peephole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.hkzlab.dupal.peeper.board.boardio.DuPALCmdInterface;
import info.hkzlab.dupal.peeper.devices.PALSpecs;
import info.hkzlab.dupal.peeper.exceptions.DuPALBoardException;
import info.hkzlab.dupal.peeper.exceptions.PeepholeException;
import info.hkzlab.dupal.peeper.utilities.BitUtils;

public class DuPALPeephole implements Peephole {
    private static final Logger logger = LoggerFactory.getLogger(DuPALPeephole.class);

    private DuPALCmdInterface dpci;
    private boolean is24Pins = false;

    public DuPALPeephole(DuPALCmdInterface dpci) {
        this.dpci = dpci;
        is24Pins = dpci.palSpecs.getPinCount_IN() > 10;

        if(dpci.getBoardVersion() >= 2) {
            dpci.setLED(DuPALCmdInterface.DuPAL_LED.P20_LED, false);
            dpci.setLED(DuPALCmdInterface.DuPAL_LED.P24_LED, false);
            dpci.setLED(is24Pins ? DuPALCmdInterface.DuPAL_LED.P24_LED : DuPALCmdInterface.DuPAL_LED.P20_LED, true);
        }

        logger.info("DuPALPeephole -> Instantiating for device " + dpci.palSpecs + ", 24 pins? " + is24Pins);
    }

    @Override
    public void write(boolean[] pins) throws PeepholeException {
        int data = BitUtils.build_WriteMaskFromPins(pins);
        try {
            dpci.write(data);
        } catch (DuPALBoardException e) {
            throw new PeepholeException(e.toString());
        }
    }

    @Override
    public boolean[] read() throws PeepholeException {
        int val = dpci.read();

        if (val < 0)
            throw new PeepholeException("Unable to read from DuPAL");

        return BitUtils.build_ReadPinsArrayFromMask(val, is24Pins);
    }

    @Override
    public void clock(boolean[] pins) throws PeepholeException {
        int data = BitUtils.build_WriteMaskFromPins(pins);
        try {
            dpci.writeAndPulseClock(data);
        } catch (DuPALBoardException e) {
            throw new PeepholeException(e.toString());
        }
    }

    @Override
    public boolean open() throws PeepholeException {
        int bv = dpci.getBoardVersion();
        if (bv < dpci.palSpecs.minimumBoardRev())
            throw new PeepholeException("This IC is not supported by this board type");

        return true;
    }

    @Override
    public void close() {
        logger.info("DuPALPeephole -> closing...");
        dpci.reset();
    }

    @Override
    public PALSpecs getSpecs() {
        return dpci.palSpecs;
    }

    @Override
    public String getDeviceName() {
        return dpci.palSpecs.getDeviceName();
    }
}
