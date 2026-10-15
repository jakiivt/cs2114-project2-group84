public class FoodOrderTest extends student.TestCase
{
    /**
     * Simple concrete implementation for testing.
     */

    
    public void testGetters()
    {
        Order order = new FoodOrder(1, 5, " Burger ", 1000L);

        assertEquals(1, order.getOrderId());
        assertEquals(5, order.getTableNumber());
        assertEquals("Burger", order.getItemName());
        assertEquals(1000L, order.getCreatedAtMillis());
    }

    
    public void testZeroOrderId()
    {
        try {
            new FoodOrder(0, 1, "Burger", 1000L);
        } 
        catch (IllegalArgumentException e){
            
            assertNotNull(e);
        }
    }

    
    public void testNegativeOrderId()
    {
        try {
            FoodOrder order = new FoodOrder(-1, 1, "Burger", 1000L);
        } 
        catch (IllegalArgumentException e){
            
            assertNotNull(e);
        }
    }

    
    public void testsZeroTable()
    {
        try {
            new FoodOrder(1, 0, "Burger", 1000L);
        } 
        catch (IllegalArgumentException e){
            
            assertNotNull(e);
        }
    }

    
    public void testNullItem()
    {
        try {
            new FoodOrder(1, 1, null, 1000L);
        } 
        catch (IllegalArgumentException e){
            
            assertNotNull(e);
        }
    }

    
    public void testEmptyItem()
    {
        try {
            new FoodOrder(1, 1, " ", 1000L);
        } 
        catch (IllegalArgumentException e){
            
            assertNotNull(e);
        }
    }

    
    public void testNegativeCreationTime()
    {
        try {
            new FoodOrder(1, 1, "Burger", -1000L);
        } 
        catch (IllegalArgumentException e){
            
            assertNotNull(e);
        }
    }

    
    public void testGetWaitMinutes()
    {
        Order order = new FoodOrder(1, 1, "Burger", 0L);

        assertEquals(5, order.getWaitMinutes(300000L));
    }

    
    public void testInvalidWait()
    {
        Order order = new FoodOrder(1, 1, "Burger", 1000L);

        try {
            order.getWaitMinutes(999L);
        }
        catch (IllegalArgumentException e){
            
            assertNotNull(e);
        }
    }

    
    public void testEqualsTrue()
    {
        Order order1 = new FoodOrder(1, 1, "Burger", 1000L);
        Order order2 = new FoodOrder(1, 2, "Pizza", 2000L);

        assertEquals(order1, order2);
    }

    
    public void testEqualsFalse()
    {
        Order order1 = new FoodOrder(1, 1, "Burger", 1000L);
        Order order2 = new FoodOrder(2, 1, "Burger", 1000L);

        assertFalse(order1.equals(order2));
    }

    
    public void testEqualsNull()
    {
        Order order = new FoodOrder(1, 1, "Burger", 1000L);

        assertFalse(order.equals(null));
    }

    
    public void testEqualsTypeDifference()
    {
        Order order = new FoodOrder(1, 1, "Burger", 1000L);

        assertFalse(order.equals("not an order"));
    }

    
    void testHashCode()
    {
        Order order1 = new FoodOrder(42, 1, "Burger", 1000L);
        Order order2 = new FoodOrder(42, 5, "Pizza", 2000L);

        assertEquals(order1.hashCode(), order2.hashCode());
    }
}