package info.hkzlab.dupal.peeper.devices;

public class PAL20L8Specs implements PALSpecs {
    private static final String[] LABELS = { "i1", "i2", "i3", "i4", "i5", "i6", "i7", "i8", "i9", "i10", "i11", "GND", "i13", "i14", "o15", "io16", "io17", "io18", "io19", "io20", "io21", "o22", "i23", "VCC" };
    public static final String PAL_TYPE = "20L8";

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
        return 0x00;
    }

    @Override
    public int getMask_OE() {
        return 0x00;
    }

    @Override
    public int getMask_IN() {
        return 0x3C03FF;
    }

    @Override
    public int getMask_IO_R() {
        return 0x7E;
    }

    @Override
    public int getMask_IO_W() {
        return 0x1F800;
    }

    @Override
    public int getMask_RO_R() {
        return 0x00;
    }

    @Override
    public int getMask_RO_W() {
        return 0x00;
    }

    @Override
    public int getMask_O_R() {
        return 0x81;
    }

    @Override
    public int getMask_O_W() {
        return 0x020400;
    }

    @Override
    public int getPinCount_IN() {
        return 14;
    }

    @Override
    public int getPinCount_IO() {
        return 6;
    }

    @Override
    public int getPinCount_O() {
        return 2;
    }

    @Override
    public int getPinCount_RO() {
        return 0;
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