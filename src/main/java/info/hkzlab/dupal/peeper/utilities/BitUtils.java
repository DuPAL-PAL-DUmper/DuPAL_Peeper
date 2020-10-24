package info.hkzlab.dupal.peeper.utilities;

import java.security.InvalidParameterException;

public class BitUtils {
    // The following array will map an entry in an ordered list (1-to-20) of pins to the correct shift in the DuPAL write command
    private static int[] h2d_write_20 = new int[] { // human-to-DuPAL conversion map for writing commands (20 pin devices)
       0,  // 1
       1,  // 2
       2,  // 3
       3,  // 4
       4,  // 5
       5,  // 6
       6,  // 7
       7,  // 8
       8,  // 9
       9,  // 11
       17, // 12
       15, // 13
       14, // 14
       13, // 15
       12, // 16
       11, // 17
       10, // 18
       16  // 19
    }; 

    private static int[] h2d_write_24 = new int[] { // human-to-DuPAL conversion map for writing commands (24 pin devices)
       0,  // 1
       1,  // 2
       2,  // 3
       3,  // 4
       4,  // 5
       5,  // 6
       6,  // 7
       7,  // 8
       8,  // 9
       9,  // 10
       18, // 11
       19, // 13
       20, // 14
       10, // 15
       11, // 16
       12, // 17
       13, // 18
       14, // 19
       15, // 20
       16, // 21
       17, // 22
       21  // 23
    };

    private static int[] d2h_read_20 = new int[] { // Add 12 to these to get the pin number
        7, // 0
        5, // 1
        4, // 2
        3, // 3
        2, // 4
        1, // 5
        0, // 6
        6  // 7
    };
    
    private static int[] d2h_read_24 = new int[] { // Add 15 to these to get the pin number
        0, // 0
        1, // 1
        2, // 2
        3, // 3
        4, // 4
        5, // 5
        6, // 6
        7  // 7
    };

    private BitUtils() {};

    static public int build_WriteMaskFromPins(boolean[] pins) {
        int mask = 0;

        int[] map;

        if(pins.length == h2d_write_20.length) map = h2d_write_20;
        else if (pins.length == h2d_write_24.length) map = h2d_write_24;
        else throw new InvalidParameterException("Pins array length ("+pins.length+") does not match the length for 20 or 24 pin devices");

        for(int idx = 0; idx < pins.length; idx++) mask |= (pins[idx] ? 1 : 0) << map[idx];

        return mask;
    }

    static public boolean[] build_ReadPinsArrayFromMask(int mask, boolean pal24) {
        boolean[] pins = new boolean[8];

        int[] map = pal24 ? d2h_read_24 : d2h_read_20;

        for(int idx = 0; idx < pins.length; idx++) pins[idx] = (mask & (1 << map[idx])) > 0;

        return pins;
    }
   
    static public int countBits(int mask) {
        int tot = 0;

        for(int idx = 0; idx < 32; idx++) {
            if((mask & (0x01 << idx)) > 0) tot++;
        }

        return tot;
    }
    
    static public int consolidateBitField(int field, int mask) {
        int data = 0;
        int shift = 0;

        for(int idx = 0; idx < 32; idx++) {
            if(((mask >> idx) & 0x01) != 0) {
                data |= (field >> (idx-shift)) & (1 << shift);
                shift++;
            }
        }

        return data;
    }
    
    static public int scatterBitField(int field, int mask) {
        int bit_idx = 0;
        int data = 0;

        for(int idx = 0; idx < 32; idx++) {
            if(((mask >> idx) & 0x01) != 0) {
                data |= ((field >> bit_idx) & 0x01) << idx;
                bit_idx++;
            }
        }

        return data;
    }
}
