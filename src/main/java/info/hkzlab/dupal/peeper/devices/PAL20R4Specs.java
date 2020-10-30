package info.hkzlab.dupal.peeper.devices;

public class PAL20R4Specs implements PALSpecs {
    private static final String[] LABELS = { "CLK", "i2", "i3", "i4", "i5", "i6", "i7", "i8", "i9", "i10", "i11", "GND", "/OE", "i14", "io15", "io16", "ro17", "ro18", "ro19", "ro20", "io21", "io22", "i23", "VCC" };
    public static final String PAL_TYPE = "20R4";

    @Override
    public String toString() {
        return "PAL"+PAL_TYPE;
    }

    @Override
    public boolean isActiveLow() {
        return true;
    }

    @Override
    public int getMask_CLK() {
        return 0x01;
    }

    @Override
    public int getMask_OE() {
        return 0x80000;
    }

    @Override
    public int getPinCount_IN() {
        return 12;
    }

    @Override
    public int getPinCount_IO() {
        return 4;
    }

    @Override
    public int getPinCount_O() {
        return 0;
    }

    @Override
    public int getPinCount_RO() {
        return 4;
    }

    @Override
    public int minimumBoardRev() {
        return 2;
    }

    @Override
    public int slotNumber() {
        return 1;
    }

    @Override
    public int[] getWritePinNumbers() {
        return PALSpecs.pal24_write_pin_numbers;
    }

    @Override
    public int[] getReadPinNumbers() {
        return PALSpecs.pal24_read_pin_numbers;
    }

    @Override
    public String[] getLabels() {
        return LABELS;
    }
}