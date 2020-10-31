package info.hkzlab.dupal.peeper.devices;

public class PAL16R4Specs implements PALSpecs {
    private static final String[] LABELS = { "CLK", "I 2", "I 3", "I 4", "I 5", "I 6", "I 7", "I 8", "I 9", "GND", "/OE", "I/O 12", "I/O 13", "Q 14", "Q 15", "Q 16", "Q 17", "I/O 18", "I/O 19", "VCC" };
    public static final String PAL_TYPE = "16R4";

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
    public int getMask_IO() {
        return 0x038400;
    }

    @Override
    public int getMask_RO() {
        return 0x7800;
    }

    @Override
    public int getMask_O() {
        return 0x00;
    }

    @Override
    public int getPinCount_IN() {
        return 8;
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

    @Override
    public String getDeviceName() {
        return PAL_TYPE;
    }
}