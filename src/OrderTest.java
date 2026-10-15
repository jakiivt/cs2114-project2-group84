import static org.junit.Assert.*;

class OrderTest
{
    /**
     * Simple concrete implementation for testing.
     */
    private static class TestOrder extends Order
    {
        TestOrder(int id, int table, String item, long creationTime)
        {
            super(id, table, item, creationTime);
        }
    }

    @Test
    void testGetters()
    {
        Order order = new TestOrder(1, 5, " Burger ", 1000L);

        assertEquals(1, order.getOrderId());
        assertEquals(5, order.getTableNumber());
        assertEquals("Burger", order.getItemName());
        assertEquals(1000L, order.getCreatedAtMillis());
    }

    @Test
    void testZeroOrderId()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new TestOrder(0, 1, "Burger", 1000L));
    }

    @Test
    void testNegativeOrderId()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new TestOrder(-1, 1, "Burger", 1000L));
    }

    @Test
    void testsZeroTable()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new TestOrder(1, 0, "Burger", 1000L));
    }

    @Test
    void testNullItem()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new TestOrder(1, 1, null, 1000L));
    }

    @Test
    void testEmptyItem()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new TestOrder(1, 1, "   ", 1000L));
    }

    @Test
    void testNegativeCreationTime()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new TestOrder(1, 1, "Burger", -1L));
    }

    @Test
    void testGetWaitMinutes()
    {
        Order order = new TestOrder(1, 1, "Burger", 0L);

        assertEquals(5, order.getWaitMinutes(300000L));
    }

    @Test
    void testInvalidWait()
    {
        Order order = new TestOrder(1, 1, "Burger", 1000L);

        assertThrows(
            IllegalArgumentException.class,
            () -> order.getWaitMinutes(999L));
    }

    @Test
    void testEqualsTrue()
    {
        Order order1 = new TestOrder(1, 1, "Burger", 1000L);
        Order order2 = new TestOrder(1, 2, "Pizza", 2000L);

        assertEquals(order1, order2);
    }

    @Test
    void testEqualsFalse()
    {
        Order order1 = new TestOrder(1, 1, "Burger", 1000L);
        Order order2 = new TestOrder(2, 1, "Burger", 1000L);

        assertNotEquals(order1, order2);
    }

    @Test
    void testEqualsNull()
    {
        Order order = new TestOrder(1, 1, "Burger", 1000L);

        assertNotEquals(null, order);
    }

    @Test
    void testEqualsTypeDifference()
    {
        Order order = new TestOrder(1, 1, "Burger", 1000L);

        assertNotEquals("not an order", order);
    }

    @Test
    void testHashCode()
    {
        Order order1 = new TestOrder(42, 1, "Burger", 1000L);
        Order order2 = new TestOrder(42, 5, "Pizza", 2000L);

        assertEquals(order1.hashCode(), order2.hashCode());
    }
}