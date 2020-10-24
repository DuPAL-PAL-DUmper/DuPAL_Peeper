package info.hkzlab.dupal.peeper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import info.hkzlab.dupal.peeper.utilities.BitUtils;

public class UtilsTest 
{
    @Test
    public void bitUtilsShouldCorrectlyModifyBitfields() {
        assertEquals("0b01010101 with a selection mask 0x55 should be consolidated into 0b1111", 0x0F, BitUtils.consolidateBitField(0x55, 0x55));
        assertEquals("0b11111111 with a selection mask 0x55 should be consolidated into 0b1111", 0x0F, BitUtils.consolidateBitField(0xFF, 0x55));
        assertEquals("0b01010101 with a selection mask 0xAA should be consolidated into 0", 0, BitUtils.consolidateBitField(0x55, 0xAA));
        assertEquals("0b01010101 with a selection mask 0xAA should be consolidated into 0b0101", 0x05, BitUtils.consolidateBitField(0x55, 0xF0));
        
        assertEquals("0b00001111 with a scatter mask 0xAA should be scattered into 0b10101010", 0xAA, BitUtils.scatterBitField(0x0F, 0xAA));
        assertEquals("0b00001111 with a scatter mask 0x03 should be scattered into 0b00000011", 0x03, BitUtils.scatterBitField(0x0F, 0x03));
        assertEquals("0b01010101 with a scatter mask 0x0F should be scattered into 0b00000101", 0x05, BitUtils.scatterBitField(0x55, 0x0F));
        assertEquals("0b01011111 with a scatter mask 0xF0 should be scattered into 0b11110000", 0xF0, BitUtils.scatterBitField(0x5F, 0xF0));
    }

    @Test
    public void bitUtilsShouldCorrectlyGenerateA20PinWriteMask() {
        boolean[] pinArray = new boolean[] {
            true,  // 1
            true,  // 2
            false, // 3
            false, // 4
            true,  // 5
            true,  // 6
            false, // 7
            false, // 8
            true,  // 9
            false, // 11
            true,  // 12
            false, // 13
            true,  // 14
            false, // 15
            true,  // 16
            false, // 17
            false, // 18
            true   // 19
        };

        int mask = BitUtils.build_WriteMaskFromPins(pinArray);
        assertEquals(0x35133, mask);
    }

    @Test
    public void bitUtilsShouldCorrectlyGenerateA24PinWriteMask() {
        boolean[] pinArray = new boolean[] {
            true,  // 1
            true,  // 2
            false, // 3
            true,  // 4
            false, // 5
            true,  // 6
            false, // 7
            true,  // 8
            true,  // 9
            false, // 10
            true,  // 11
            false, // 13
            true,  // 14
            false, // 15
            true,  // 16
            false, // 17
            false, // 18
            true,  // 19
            false, // 20
            false, // 21
            true,  // 22
            false, // 23
        };

        int mask = BitUtils.build_WriteMaskFromPins(pinArray);
        assertEquals(0x1649ab, mask);
    }

    @Test
    public void bitUtilsShouldCorrectlyGenerateA20PinReadArray() {
        int mask = 0x54;
        boolean[] pins = BitUtils.build_ReadPinsArrayFromMask(mask, false);

        assertArrayEquals(new boolean[] {false, false, true, false, true, false, false, true}, pins);
    }
    
    @Test
    public void bitUtilsShouldCorrectlyGenerateA24PinReadArray() {
        int mask = 0x54;
        boolean[] pins = BitUtils.build_ReadPinsArrayFromMask(mask, true);

        assertArrayEquals(new boolean[] {false, false, true, false, true, false, true, false}, pins);
    }
}
