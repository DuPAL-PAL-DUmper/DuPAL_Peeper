package info.hkzlab.dupal.peeper.parser.states;

public class RLink {
    public final int inputs;
    public final OutStatePins src, middle, dst;

    public RLink(final int inputs, final OutStatePins src, final OutStatePins middle, final OutStatePins dst) {
        this.inputs = inputs;
        this.src = src;
        this.middle = middle;
        this.dst = dst;
    } 
}
