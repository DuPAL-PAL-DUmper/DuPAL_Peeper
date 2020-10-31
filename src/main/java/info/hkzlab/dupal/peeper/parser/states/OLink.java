package info.hkzlab.dupal.peeper.parser.states;

public class OLink {
    public final int inputs;
    public final OutStatePins src, dst;

    public OLink(final int inputs, final OutStatePins src, final OutStatePins dst) {
        this.inputs = inputs;
        this.src = src;
        this.dst = dst;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = hash*31+inputs;
        hash = hash*31+src.hashCode();
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

        return (this.inputs == ((OLink)o).inputs) &&
               (this.src.equals(((OLink)o).src)) &&
               (this.dst.equals(((OLink)o).dst));
    }
}
