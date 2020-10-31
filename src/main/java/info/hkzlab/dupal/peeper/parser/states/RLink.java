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

    @Override
    public int hashCode() {
        int hash = 7;

        hash = hash*31+inputs;
        hash = hash*31+src.hashCode();
        hash = hash*31+middle.hashCode();
        hash = hash*31+dst.hashCode();

        return hash;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (this.getClass() != o.getClass())
            return false;

        return (this.inputs == ((RLink)o).inputs) &&
               (this.src.equals(((RLink)o).src)) &&
               (this.middle.equals(((RLink)o).middle)) &&
               (this.dst.equals(((RLink)o).dst));
    }
}
