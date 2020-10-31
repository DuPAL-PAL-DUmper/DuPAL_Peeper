package info.hkzlab.dupal.peeper.devices;

public class PAL20L8Specs implements PALSpecs {
    private static final String[] LABELS = { "I 1", "I 2", "I 3", "I 4", "I 5", "I 6", "I 7", "I 8", "I 9", "I 10", "I 11", "GND", "I 13", "I 14", "O 15", "I/O 16", "I/O 17", "I/O 18", "I/O 19", "I/O 20", "I/O 21", "O 22", "I 23", "VCC" };
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
    public int getMask_IO() {
        return 0x1F800;
    }

    @Override
    public int getMask_RO() {
        return 0x00;
    }

    @Override
    public int getMask_O() {
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

    @Override
    public String getDeviceName() {
        return PAL_TYPE;
    }
}