package info.hkzlab.dupal.peeper.peephole;

import info.hkzlab.dupal.peeper.devices.PALSpecs;
import info.hkzlab.dupal.peeper.exceptions.PeepholeException;
import info.hkzlab.dupal.peeper.utilities.BitUtils;

public class DumpPeephole implements Peephole {

    @Override
    public void write(boolean[] pins) throws PeepholeException {
        int data = BitUtils.build_WriteMaskFromPins(pins);

        // TODO Auto-generated method stub

    }

    @Override
    public boolean[] read() throws PeepholeException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clock(boolean[] pins) throws PeepholeException {
        int data = BitUtils.build_WriteMaskFromPins(pins);

        // TODO Auto-generated method stub

    }

    @Override
    public boolean open() throws PeepholeException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    @Override
    public PALSpecs getSpecs() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
