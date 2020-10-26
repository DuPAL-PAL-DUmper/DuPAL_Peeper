package info.hkzlab.dupal.peeper.peephole.DuPALPeephole;

import info.hkzlab.dupal.peeper.board.boardio.DuPALCmdInterface;
import info.hkzlab.dupal.peeper.exceptions.PeepholeException;
import info.hkzlab.dupal.peeper.peephole.PeepholeInterface;

public class DuPALPeephole implements PeepholeInterface {
    private DuPALCmdInterface dpci;

    public DuPALPeephole(DuPALCmdInterface dpci) {
        this.dpci = dpci;
    }


    @Override
    public void write(boolean[] pins) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean[] read() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clock() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean open() throws PeepholeException {
        return false;
    }

    @Override
    public void close() {
        dpci.reset();
    }
    
}
