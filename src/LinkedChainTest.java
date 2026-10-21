/**
 * Unit tests for the generic LinkedChain class.
 * Tests generic reuse across multiple element types, normal operations,
 * and boundary / bad-input cases.
 *
 * @author Talal, Jack, Bryan, Abigail
 * @version Fall 2026
 */
public class LinkedChainTest extends student.TestCase
{
    private LinkedChain<String> stringChain;
    private LinkedChain<Integer> intChain;
    private LinkedChain<FoodOrder> foodChain;

    @Override
    public void setUp()
    {
        stringChain = new LinkedChain<String>();
        intChain = new LinkedChain<Integer>();
        foodChain = new LinkedChain<FoodOrder>();
    }

    public void testEmptyChain()
    {
        assertTrue(stringChain.isEmpty());
        assertEquals(0, stringChain.size());
        assertNull(stringChain.remove());
        assertFalse(stringChain.contains("test"));
        assertEquals(0, stringChain.count("test"));
    }

    public void testAdd()
    {
        stringChain.add("apple");
        stringChain.add("banana");

        assertEquals(2, stringChain.size());
        assertFalse(stringChain.isEmpty());
        // add inserts at the front, so banana is at index 0
        assertEquals("banana", stringChain.get(0));
        assertEquals("apple", stringChain.get(1));
    }

    public void testAddNull()
    {
        try
        {
            stringChain.add(null);
            fail("Expected IllegalArgumentException for null add");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }

    public void testAddToEnd()
    {
        stringChain.addToEnd("first");
        stringChain.addToEnd("second");
        stringChain.addToEnd("third");

        assertEquals(3, stringChain.size());
        assertEquals("first", stringChain.get(0));
        assertEquals("second", stringChain.get(1));
        assertEquals("third", stringChain.get(2));
    }

    public void testAddToEndNull()
    {
        try
        {
            stringChain.addToEnd(null);
            fail("Expected IllegalArgumentException for null addToEnd");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }

    public void testRemoveFront()
    {
        assertNull(stringChain.remove());

        stringChain.addToEnd("A");
        stringChain.addToEnd("B");

        assertEquals("A", stringChain.remove());
        assertEquals(1, stringChain.size());
        assertEquals("B", stringChain.get(0));

        assertEquals("B", stringChain.remove());
        assertTrue(stringChain.isEmpty());
        assertNull(stringChain.remove());
    }

    public void testRemoveItem()
    {
        assertFalse(stringChain.remove("absent"));
        assertFalse(stringChain.remove(null));

        stringChain.addToEnd("X");
        stringChain.addToEnd("Y");
        stringChain.addToEnd("Z");

        // Remove from head
        assertTrue(stringChain.remove("X"));
        assertEquals(2, stringChain.size());
        assertEquals("Y", stringChain.get(0));

        // Remove from tail
        assertTrue(stringChain.remove("Z"));
        assertEquals(1, stringChain.size());
        assertEquals("Y", stringChain.get(0));

        // Remove only remaining
        assertTrue(stringChain.remove("Y"));
        assertTrue(stringChain.isEmpty());

        // Test removing middle element
        stringChain.addToEnd("1");
        stringChain.addToEnd("2");
        stringChain.addToEnd("3");
        assertTrue(stringChain.remove("2"));
        assertEquals(2, stringChain.size());
        assertEquals("1", stringChain.get(0));
        assertEquals("3", stringChain.get(1));
    }

    public void testClear()
    {
        stringChain.clear();
        assertTrue(stringChain.isEmpty());

        stringChain.addToEnd("A");
        stringChain.addToEnd("B");
        stringChain.clear();

        assertTrue(stringChain.isEmpty());
        assertEquals(0, stringChain.size());
        assertNull(stringChain.remove());
    }

    public void testGetInvalidIndices()
    {
        try
        {
            stringChain.get(0);
            fail("Expected IndexOutOfBoundsException on empty chain");
        }
        catch (IndexOutOfBoundsException e)
        {
            assertNotNull(e);
        }

        stringChain.addToEnd("only");

        try
        {
            stringChain.get(-1);
            fail("Expected IndexOutOfBoundsException for negative index");
        }
        catch (IndexOutOfBoundsException e)
        {
            assertNotNull(e);
        }

        try
        {
            stringChain.get(1);
            fail("Expected IndexOutOfBoundsException for index >= size");
        }
        catch (IndexOutOfBoundsException e)
        {
            assertNotNull(e);
        }
    }

    public void testContainsAndCount()
    {
        assertFalse(stringChain.contains(null));
        assertEquals(0, stringChain.count(null));

        stringChain.addToEnd("repeat");
        stringChain.addToEnd("unique");
        stringChain.addToEnd("repeat");

        assertTrue(stringChain.contains("repeat"));
        assertTrue(stringChain.contains("unique"));
        assertFalse(stringChain.contains("absent"));

        assertEquals(2, stringChain.count("repeat"));
        assertEquals(1, stringChain.count("unique"));
        assertEquals(0, stringChain.count("absent"));
    }

    public void testToArray()
    {
        stringChain.addToEnd("one");
        stringChain.addToEnd("two");

        String[] array = stringChain.toArray(new String[2]);
        assertEquals(2, array.length);
        assertEquals("one", array[0]);
        assertEquals("two", array[1]);

        try
        {
            stringChain.toArray(null);
            fail("Expected IllegalArgumentException for null array");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }

        try
        {
            stringChain.toArray(new String[1]);
            fail("Expected IllegalArgumentException for too small array");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }

    public void testGenericReuseWithIntegers()
    {
        intChain.addToEnd(10);
        intChain.addToEnd(20);
        intChain.addToEnd(30);

        assertEquals(3, intChain.size());
        assertEquals(10, (int) intChain.get(0));
        assertEquals(20, (int) intChain.get(1));
        assertEquals(30, (int) intChain.get(2));

        assertTrue(intChain.remove(Integer.valueOf(20)));
        assertEquals(2, intChain.size());
        assertEquals(30, (int) intChain.get(1));
    }

    public void testGenericReuseWithOrders()
    {
        FoodOrder order1 = new FoodOrder(1, 4, "burger", 1000L);
        FoodOrder order2 = new FoodOrder(2, 4, "fries", 2000L);

        foodChain.addToEnd(order1);
        foodChain.addToEnd(order2);

        assertEquals(2, foodChain.size());
        assertEquals(order1, foodChain.get(0));
        assertEquals(order2, foodChain.get(1));

        FoodOrder[] orders = foodChain.toArray(new FoodOrder[2]);
        assertEquals("burger", orders[0].getItemName());
        assertEquals("fries", orders[1].getItemName());

        assertTrue(foodChain.remove(order1));
        assertEquals(1, foodChain.size());
        assertEquals(order2, foodChain.get(0));
    }
}
