package info.hkzlab.dupal.peeper.peephole;

public interface PeepholeInterface {
    public void write(boolean[] pins);
    public boolean[] read();
    public void clock();
}
