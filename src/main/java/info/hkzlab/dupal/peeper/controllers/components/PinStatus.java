package info.hkzlab.dupal.peeper.controllers.components;

public class PinStatus {
    public final int pinNumber;
    public final int index;
    public final String pinName;

    private boolean state = false;
    private boolean previousState = false;
    private boolean wasModified = false;

    public PinStatus(int pinNumber, int index, String pinName) {
            this.pinNumber = pinNumber;
            this.pinName = pinName;
            this.index = index;
    }

    public void setState(boolean state) {
        if(!wasModified) {
            previousState = this.state;
            wasModified = true;
        } else if (state == previousState) {
            this.state = state;
            wasModified = false;
        }

        this.state = state;
    }

    public boolean getState() {
        return state;
    }

    public boolean wasModified() {
        return wasModified;
    }

    public void commitChange() {
        previousState = state;
        wasModified = false;
    }

    public String toString() {
        return pinName;
    }
}
