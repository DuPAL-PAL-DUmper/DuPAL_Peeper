package info.hkzlab.dupal.peeper.devices;

public class PAL12H6Specs implements PALSpecs {
    private static final String[] LABELS = { "i1", "i2", "i3", "i4", "i5", "i6", "i7", "i8", "i9", "GND", "i11", "i12", "o13", "o14", "o15", "o16", "o17", "o18", "i19", "VCC" };
    public static final String PAL_TYPE = "12H6";

    @Override
    public String toString() {
        return "PAL"+PAL_TYPE;
    }

    @Override
    public boolean isActiveLow() {
        return false;
    }

    @Override
    public int getMask_CLK() {
        return 0x00;
    }

    @Override
    public int getMask_OE() {
        return 0x00;
    }

    @Override
    public int getPinCount_IN() {
        return 12;
    }

    @Override
    public int getPinCount_IO() {
        return 0;
    }

    @Override
    public int getPinCount_O() {
        return 6;
    }

    @Override
    public int getPinCount_RO() {
        return 0;
    }

    @Override
    public int minimumBoardRev() {
        return 1;
    }

    @Override
    public int slotNumber() {
        return 0;
    }

    @Override
    public int[] getWritePinNumbers() {
        return PALSpecs.pal20_write_pin_numbers;
    }

    @Override
    public int[] getReadPinNumbers() {
        return PALSpecs.pal20_read_pin_numbers;
    }

    @Override
    public String[] getLabels() {
        return LABELS;
    }
}