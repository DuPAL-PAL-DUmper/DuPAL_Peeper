package info.hkzlab.dupal.peeper.controllers.components;

public class PinStatus {
    public final int pinNumber;
    public final int index;
    public final String pinName;

    private boolean state = false;
    private boolean previousState = false;

    public PinStatus(int pinNumber, int index, String pinName) {
            this.pinNumber = pinNumber;
            this.pinName = pinName;
            this.index = index;
    }

    public void setStatus(boolean state) {
        previousState = this.state;
        this.state = state;
    }

    public boolean getState() {
        return state;
    }

    public boolean wasModified() {
        return state != previousState;
    }

    public void commitChange() {
        previousState = state;
    }

    public String toString() {
        return pinName;
    }
}
