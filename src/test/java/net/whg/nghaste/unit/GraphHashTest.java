package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import net.whg.nghaste.GraphHash;

public class GraphHashTest
{
    @Test
    public void equals_identicalGraphs()
    {
        GraphHash hash1 = new GraphHash(new byte[] {0, 23, 19, 5, 7, 21});
        GraphHash hash2 = new GraphHash(new byte[] {0, 23, 19, 5, 7, 21});

        assertEquals(hash1, hash2);
        assertEquals(hash1.hashCode(), hash2.hashCode());
    }

    @Test
    public void notEquals_nonidenticalGraphs()
    {
        GraphHash hash1 = new GraphHash(new byte[] {0, 23, 19, 5, 7, 21});
        GraphHash hash2 = new GraphHash(new byte[] {0, 21, 8, 12, 7, 14});

        assertNotEquals(hash1, hash2);
        assertNotEquals(hash1.hashCode(), hash2.hashCode());
    }

    @Test
    public void notEquals_nonidenticalGraphs_differentLengths()
    {
        GraphHash hash1 = new GraphHash(new byte[] {0, 23, 19, 5, 7, 21});
        GraphHash hash2 = new GraphHash(new byte[] {0, 23, 19, 5, 7, 21, 8});

        assertNotEquals(hash1, hash2);
        assertNotEquals(hash1.hashCode(), hash2.hashCode());
    }

    @Test
    public void notEquals_differentObject()
    {
        GraphHash hash = new GraphHash(new byte[] {3, 66, 12, 6});
        assertNotEquals(hash, new Object());
    }

    @Test
    public void equals_sameObject()
    {
        GraphHash hash = new GraphHash(new byte[] {3, 66, 12, 6});
        assertEquals(hash, hash);
    }

    @Test
    public void validateString()
    {
        GraphHash hash = new GraphHash(new byte[] {1, 2, 3, 4, 5});
        assertEquals("[1, 2, 3, 4, 5]", hash.toString());
    }
}
