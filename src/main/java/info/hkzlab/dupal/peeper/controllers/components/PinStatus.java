package info.hkzlab.dupal.peeper.controllers.components;

public class PinStatus implements Comparable<PinStatus> {
    public final int pinNumber;
    public final int index;
    public final String pinName;

    private boolean state = false;
    private boolean previousState = false;
    private boolean wasModified = false;
    private boolean hiz = false;

    public PinStatus(int pinNumber, int index, String pinName) {
            this.pinNumber = pinNumber;
            this.pinName = pinName;
            this.index = index;
    }

    public void setState(boolean state) {
        this.hiz = false; // Reset the HI-Z state

        if(!wasModified) {
            previousState = this.state;
            wasModified = true;
        } else if (state == previousState) {
            this.state = state;
            wasModified = false;
        }

        this.state = state;
    }

    public void setHIZ(boolean hiz) {
        this.hiz = hiz;
    }

    public boolean getState() {
        return state;
    }

    public boolean getHIZ() {
        return hiz;
    }

    public boolean wasModified() {
        return wasModified;
    }

    public void commitChange() {
        previousState = state;
        wasModified = false;
    }

    public void clearChange() {
        wasModified = false;
        state = previousState;
    }

    public String toString() {
        return pinName;
    }

    @Override
    public int compareTo(PinStatus o) {
        return this.pinNumber - o.pinNumber;
    }
}
