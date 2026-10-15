/**
 * // -------------------------------------------------------------------------
/**
 *  DrinkOrder test class for the methods
 * 
 *  @author Bryan
 *  @version Oct 15, 2026
 */
public class DrinkOrderTest extends student.TestCase
{
    /**
     * Simple concrete implementation for testing.
     */
    /** Creates a fresh menu before each test. */
    
    public void testZeroOrderId()
    {
        Exception exception = null;
        try
        {
            new DrinkOrder(0, 1, "Burger", 1000L);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
    }

    public void testNegativeOrderId()
    {
        Exception exception = null;
        try
        {
            new DrinkOrder(-1, 1, "Burger", 1000L);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
    }

    public void testsZeroTable()
    {
        Exception exception = null;
        try
        {
            new DrinkOrder(1, 0, "Burger", 1000L);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
    }

    public void testNullItem()
    {
        Exception exception = null;
        try
        {
            new DrinkOrder(1, 1, null, 1000L);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
    }

    public void testEmptyItem()
    {
        Exception exception = null;
        try
        {
            new DrinkOrder(1, 1, "  ", 1000L);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
    }

    public void testNegativeCreationTime()
    {
        Exception exception = null;
        try
        {
            new DrinkOrder(1, 1, "Burger", -1L);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
    }

    public void testGetWaitMinutes()
    {
        Order order = new DrinkOrder(1, 1, "Burger", 0L);

        assertEquals(5, order.getWaitMinutes(300000L));
    }

   public void testInvalidWait()
    {
        Order order = new DrinkOrder(1, 1, "Burger", 1000L);
        Exception exception = null;
        try
        {
            order.getWaitMinutes(999L);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }
        assertNotNull(exception);
    }

    public void testEqualsTrue()
    {
        Order order1 = new DrinkOrder(1, 1, "Burger", 1000L);
        Order order2 = new DrinkOrder(1, 2, "Pizza", 2000L);

        assertEquals(order1, order2);
    }

    public void testEqualsFalse()
    {
        Order order1 = new DrinkOrder(1, 1, "Burger", 1000L);
        Order order2 = new DrinkOrder(2, 1, "Burger", 1000L);

        assertFalse(order1.equals(order2));
    }

    public void testEqualsNull()
    {
        Order order = new DrinkOrder(1, 1, "Burger", 1000L);

        assertFalse(order.equals(null));
    }

    public void testEqualsTypeDifference()
    {
        Order order = new DrinkOrder(1, 1, "Burger", 1000L);

        assertFalse(order.equals("not an order"));
    }

    public void testHashCode()
    {
        Order order1 = new DrinkOrder(42, 1, "Burger", 1000L);
        Order order2 = new DrinkOrder(42, 5, "Pizza", 2000L);

        assertEquals(order1.hashCode(), order2.hashCode());
    }
}