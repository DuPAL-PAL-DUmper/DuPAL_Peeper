package info.hkzlab.dupal.peeper.devices;

public class PAL16R8Specs implements PALSpecs {
    private static final String[] LABELS = { "CLK", "i2", "i3", "i4", "i5", "i6", "i7", "i8", "i9", "GND", "/OE", "ro12", "ro13", "ro14", "ro15", "ro16", "ro17", "ro18", "ro19", "VCC" };
    public static final String PAL_TYPE = "16R8";

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
        return 0x0200;
    }

    @Override
    public int getMask_IN() {
        return 0x1FE;
    }

    @Override
    public int getMask_IO_R() {
        return 0x00;
    }

    @Override
    public int getMask_IO_W() {
        return 0x00;
    }

    @Override
    public int getMask_RO_R() {
        return 0xFF;
    }

    @Override
    public int getMask_RO_W() {
        return 0x03FC00;
    }

    @Override
    public int getMask_O_R() {
        return 0x00;
    }

    @Override
    public int getMask_O_W() {
        return 0x00;
    }

    @Override
    public int getPinCount_IN() {
        return 8;
    }

    @Override
    public int getPinCount_IO() {
        return 0;
    }

    @Override
    public int getPinCount_O() {
        return 0;
    }

    @Override
    public int getPinCount_RO() {
        return 8;
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