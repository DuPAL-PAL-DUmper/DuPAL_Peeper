package info.hkzlab.dupal.peeper.devices;

public interface PALSpecs {
    public static final int[] pal20_write_pin_numbers = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 13, 14, 15, 16, 17, 18, 19 };
    public static final int[] pal24_write_pin_numbers = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 };
    public static final int[] pal20_read_pin_numbers = new int[] { 12, 13, 14, 15, 16, 17, 18, 19 };
    public static final int[] pal24_read_pin_numbers = new int[] { 15, 16, 17, 18, 19, 20, 21, 22 };

    public int[] getWritePinNumbers();
    public int[] getReadPinNumbers();

    public int getPinCount_IN();
    public int getPinCount_IO();
    public int getPinCount_O();
    public int getPinCount_RO();

    public int getMask_CLK();
    public int getMask_OE();

    public String[] getLabels();

    public boolean isActiveLow();

    public int minimumBoardRev();

    public int slotNumber();

    public String getDeviceName();
}