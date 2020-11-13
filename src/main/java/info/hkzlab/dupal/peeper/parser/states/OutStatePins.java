package info.hkzlab.dupal.peeper.parser.states;

public class OutStatePins {
    public final int out;
    public final int hiz;
    public final int json_hash; // The hashcode this state had in the JSON, for selection purposes

    public OutStatePins(int out, int hiz) {
        this(out, hiz, 0);
    }

    public OutStatePins(int out, int hiz, int json_hash) {
        this.out = out;
        this.hiz = hiz;
        this.json_hash = json_hash;
    }

    
    @Override
    public int hashCode() {
        int hash = 7;

        hash = hash*31+out;
        hash = hash*31+hiz;

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

        return (this.hiz == ((OutStatePins)o).hiz) &&
               (this.out == ((OutStatePins)o).out);
    }

    public String toString() {
        return "OutStatePins-["+String.format("%02X", out)+"|"+String.format("%02X", hiz)+"]";
    }
}
