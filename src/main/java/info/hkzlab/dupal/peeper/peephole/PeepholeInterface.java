package info.hkzlab.dupal.peeper.peephole;

import info.hkzlab.dupal.peeper.exceptions.PeepholeException;

public interface PeepholeInterface {
    public void write(boolean[] pins);
    public boolean[] read();
    public void clock();
    public boolean open() throws PeepholeException;
    public void close();
}
