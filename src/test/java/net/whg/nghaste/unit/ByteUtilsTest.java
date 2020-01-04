package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import net.whg.nghaste.util.ByteUtils;

public class ByteUtilsTest
{
    @Test
    public void countBytes()
    {
        assertEquals(4, ByteUtils.countBytes(1 << 26));
        assertEquals(3, ByteUtils.countBytes(1 << 18));
        assertEquals(2, ByteUtils.countBytes(1 << 13));
        assertEquals(1, ByteUtils.countBytes(1 << 6));

        assertEquals(4, ByteUtils.countBytes((1 << 31) - 1));
        assertEquals(3, ByteUtils.countBytes((1 << 24) - 1));
        assertEquals(2, ByteUtils.countBytes((1 << 16) - 1));
        assertEquals(1, ByteUtils.countBytes((1 << 8) - 1));
    }
}
