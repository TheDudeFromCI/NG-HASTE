package net.whg.nghaste.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import java.util.Random;
import org.junit.Test;
import net.whg.nghaste.Connection;

public class ConnectionTest
{
    @Test
    public void setData()
    {
        Connection connection = new Connection();

        connection.set(1, 2, 3, 4, 5, 6);

        assertEquals(1, connection.getOutputNode());
        assertEquals(2, connection.getOutputPlug());
        assertEquals(3, connection.getInputNode());
        assertEquals(4, connection.getInputPlug());
        assertEquals(5, connection.getOutputNodeType());
        assertEquals(6, connection.getInputNodeType());
    }

    @Test
    public void equals_isEqual()
    {
        Connection con1 = new Connection();
        Connection con2 = new Connection();

        con1.set(10, 20, 30, 40, 50, 60);
        con2.set(10, 20, 30, 40, 50, 60);

        assertTrue(con1.equals(con2));
    }

    @Test
    public void equals_isEqual_self()
    {
        Connection c = new Connection();
        c.set(102, 203, 305, 406, 505, 680);

        assertTrue(c.equals(c));
    }

    @Test
    public void equals_notEqual_otherObject()
    {
        Connection c = new Connection();
        c.set(102, 203, 305, 406, 505, 680);

        assertFalse(c.equals(new Object()));
    }

    @Test
    public void equals_notEqual()
    {
        Connection con1 = new Connection();
        Connection con2 = new Connection();

        con1.set(10, 20, 30, 40, 50, 60);
        con2.set(10, 21, 30, 40, 50, 60);

        assertFalse(con1.equals(con2));
    }

    @Test
    public void hashCode_randomData()
    {
        Connection c = new Connection();
        Random r = new Random();

        int last = 0;
        for (int i = 0; i < 10; i++)
        {
            c.set(r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt(), r.nextInt());
            assertNotEquals(last, c.hashCode());
            last = c.hashCode();
        }
    }

    @Test
    public void toString_formatted()
    {
        Connection c = new Connection();
        c.set(2, 4, 6, 8, 10, 12);

        assertEquals("2:4 -> 6:8 (10 -> 12)", c.toString());
    }
}
