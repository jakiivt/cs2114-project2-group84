/**
 * Tests the abstract Order class and its invariants.
 *
 * @author Talal, Jack, Bryan, Abigail
 * @version Fall 2026
 */
public class OrderTest extends student.TestCase
{
    private static class ConcreteOrder extends Order
    {
        ConcreteOrder(int id, int table, String item, long creationTime)
        {
            super(id, table, item, creationTime);
        }

        ConcreteOrder(
            int id,
            int table,
            String item,
            String instructions,
            long creationTime)
        {
            super(id, table, item, instructions, creationTime);
        }
    }

    public void testGetters()
    {
        Order order = new ConcreteOrder(1, 5, " Burger ", 1000L);
        assertEquals(1, order.getOrderId());
        assertEquals(5, order.getTableNumber());
        assertEquals("Burger", order.getItemName());
        assertEquals("", order.getInstructions());
        assertFalse(order.hasInstructions());
        assertEquals(1000L, order.getCreatedAtMillis());

        Order orderWithNotes = new ConcreteOrder(
            2, 6, "Fries", "no salt", 2000L);
        assertEquals(2, orderWithNotes.getOrderId());
        assertEquals(6, orderWithNotes.getTableNumber());
        assertEquals("Fries", orderWithNotes.getItemName());
        assertEquals("no salt", orderWithNotes.getInstructions());
        assertTrue(orderWithNotes.hasInstructions());
        assertEquals(2000L, orderWithNotes.getCreatedAtMillis());
    }

    public void testZeroOrderId()
    {
        try
        {
            new ConcreteOrder(0, 1, "Burger", 1000L);
            fail("Expected IllegalArgumentException for zero ID");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }

    public void testNegativeOrderId()
    {
        try
        {
            new ConcreteOrder(-1, 1, "Burger", 1000L);
            fail("Expected IllegalArgumentException for negative ID");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }

    public void testZeroTable()
    {
        try
        {
            new ConcreteOrder(1, 0, "Burger", 1000L);
            fail("Expected IllegalArgumentException for zero table");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }

    public void testNegativeTable()
    {
        try
        {
            new ConcreteOrder(1, -2, "Burger", 1000L);
            fail("Expected IllegalArgumentException for negative table");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }

    public void testNullItem()
    {
        try
        {
            new ConcreteOrder(1, 1, null, 1000L);
            fail("Expected IllegalArgumentException for null item");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }

    public void testEmptyItem()
    {
        try
        {
            new ConcreteOrder(1, 1, "  \t  ", 1000L);
            fail("Expected IllegalArgumentException for empty item");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }

    public void testNegativeCreationTime()
    {
        try
        {
            new ConcreteOrder(1, 1, "Burger", -1L);
            fail("Expected IllegalArgumentException for negative time");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }

    public void testInstructionsLimits()
    {
        ConcreteOrder order = new ConcreteOrder(
            1, 1, "Burger", null, 1000L);
        assertEquals("", order.getInstructions());
        assertFalse(order.hasInstructions());

        StringBuilder maxNotes = new StringBuilder();
        for (int i = 0; i < 100; i++)
        {
            maxNotes.append("x");
        }
        ConcreteOrder order100 = new ConcreteOrder(
            2, 1, "Burger", maxNotes.toString(), 1000L);
        assertEquals(100, order100.getInstructions().length());

        maxNotes.append("x");
        try
        {
            new ConcreteOrder(3, 1, "Burger", maxNotes.toString(), 1000L);
            fail("Expected IllegalArgumentException for instructions > 100 chars");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }

    public void testGetWaitMinutes()
    {
        Order order = new ConcreteOrder(1, 1, "Burger", 0L);
        assertEquals(5, order.getWaitMinutes(300000L));
    }

    public void testInvalidWait()
    {
        Order order = new ConcreteOrder(1, 1, "Burger", 1000L);
        try
        {
            order.getWaitMinutes(999L);
            fail("Expected IllegalArgumentException when now precedes creation");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }

    public void testEqualsTrue()
    {
        Order order1 = new ConcreteOrder(1, 1, "Burger", 1000L);
        Order order2 = new ConcreteOrder(1, 2, "Pizza", 2000L);
        assertEquals(order1, order2);
        assertTrue(order1.equals(order2));
    }

    public void testEqualsFalse()
    {
        Order order1 = new ConcreteOrder(1, 1, "Burger", 1000L);
        Order order2 = new ConcreteOrder(2, 1, "Burger", 1000L);
        assertFalse(order1.equals(order2));
    }

    public void testEqualsNull()
    {
        Order order = new ConcreteOrder(1, 1, "Burger", 1000L);
        assertFalse(order.equals(null));
    }

    public void testEqualsTypeDifference()
    {
        Order order = new ConcreteOrder(1, 1, "Burger", 1000L);
        assertFalse(order.equals("not an order"));
    }

    public void testHashCode()
    {
        Order order1 = new ConcreteOrder(42, 1, "Burger", 1000L);
        Order order2 = new ConcreteOrder(42, 5, "Pizza", 2000L);
        assertEquals(order1.hashCode(), order2.hashCode());
    }
}
