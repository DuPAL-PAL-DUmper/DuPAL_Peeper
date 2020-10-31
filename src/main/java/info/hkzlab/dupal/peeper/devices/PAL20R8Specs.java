package info.hkzlab.dupal.peeper.devices;

public class PAL20R8Specs implements PALSpecs {
    private static final String[] LABELS = { "CLK", "I 2", "I 3", "I 4", "I 5", "I 6", "I 7", "I 8", "I 9", "I 10", "I 11", "GND", "/OE", "I 14", "Q 15", "Q 16", "Q 17", "Q 18", "Q 19", "Q 20", "Q 21", "Q 22", "I 23", "VCC" };
    public static final String PAL_TYPE = "20R8";

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
    public int getMask_IN() {
        return 0x3403FE;
    }

    @Override
    public int getMask_IO() {
        return 0x00;
    }

    @Override
    public int getMask_RO() {
        return 0x3FC00;
    }

    @Override
    public int getMask_O() {
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
        return 0;
    }

    @Override
    public int getPinCount_RO() {
        return 8;
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