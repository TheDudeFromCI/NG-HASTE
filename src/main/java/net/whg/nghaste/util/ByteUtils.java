package net.whg.nghaste.util;

public final class ByteUtils
{
    private ByteUtils()
    {}

    /**
     * Calculates then fewest number of bytes required to represent a single value.
     * Value is assumed to be unsigned.
     * 
     * @param value
     *     - The value.
     * @return The smallest number of bytes, being 1, 2, 3, or 4.
     */
    public static int countBytes(int value)
    {
        value = Math.abs(value);

        if (value < 1 << 8)
            return 1;

        if (value < 1 << 16)
            return 2;

        if (value < 1 << 24)
            return 3;

        return 4;
    }
}
