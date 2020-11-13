package info.hkzlab.dupal.peeper.parser.states;

public class OutStatePins {
    public final int out;
    public final int hiz;
    public final int dump_hash; // The hashcode this state had in the JSON, for selection purposes

    public OutStatePins(int out, int hiz) {
        this(out, hiz, internal_hashCode(out, hiz)); // If not provided, we'll just calculate a value by ourselves
    }

    public OutStatePins(int out, int hiz, int dump_hash) {
        this.out = out;
        this.hiz = hiz;
        this.dump_hash = dump_hash;
    }

    private static int internal_hashCode(int iout, int ihiz) {
        int hash = 7;

        hash = hash*31+iout;
        hash = hash*31+ihiz;

        return hash;        
    }
    
    @Override
    public int hashCode() {
        return internal_hashCode(out, hiz);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (this.getClass() != o.getClass())
            return false;

        return (this.hiz == ((OutStatePins)o).hiz) &&
               (this.out == ((OutStatePins)o).out);
    }

    public String toString() {
        return "OutStatePins-["+String.format("%02X", out)+"|"+String.format("%02X", hiz)+"]";
    }
}
