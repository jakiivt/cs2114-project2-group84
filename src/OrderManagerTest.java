import java.util.NoSuchElementException;

/**
 * Unit tests for OrderManager.
 * Tests every public method with at least one normal case
 * and at least one bad-input or boundary case.
 *
 * @author Talal, Jack, Bryan, Abigail
 * @version Fall 2026
 */
public class OrderManagerTest extends student.TestCase
{
    private OrderManager manager;
    private Menu menu;

    @Override
    public void setUp()
    {
        menu = new Menu();
        manager = new OrderManager(menu);
    }

    public void testConstructorAndGetMenu()
    {
        assertNotNull(manager.getMenu());
        assertEquals(menu, manager.getMenu());

        try
        {
            new OrderManager(null);
            fail("Expected IllegalArgumentException for null menu");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }

    public void testAddFoodNormal()
    {
        FoodOrder order = manager.addFood(1, "burger");
        assertEquals(1, order.getOrderId());
        assertEquals(1, order.getTableNumber());
        assertEquals("burger", order.getItemName());
        assertEquals("", order.getInstructions());
        assertFalse(order.hasInstructions());

        // Second food gets ID 2
        FoodOrder order2 = manager.addFood(1, "fries");
        assertEquals(2, order2.getOrderId());
    }

    public void testAddFoodWithInstructions()
    {
        FoodOrder order = manager.addFood(3, "salad", "dressing on side");
        assertEquals("salad", order.getItemName());
        assertEquals("dressing on side", order.getInstructions());
        assertTrue(order.hasInstructions());
    }

    public void testAddFoodBadInput()
    {
        // Bad table number: 0
        try
        {
            manager.addFood(0, "burger");
            fail("Expected IllegalArgumentException for table 0");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }

        // Bad table number: negative
        try
        {
            manager.addFood(-5, "burger");
            fail("Expected IllegalArgumentException for negative table");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }

        // Null item
        try
        {
            manager.addFood(1, null);
            fail("Expected IllegalArgumentException for null item");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }

        // Blank item
        try
        {
            manager.addFood(1, "   ");
            fail("Expected IllegalArgumentException for blank item");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }

        // Item not on menu
        try
        {
            manager.addFood(1, "pizza");
            fail("Expected IllegalArgumentException for item not on menu");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }

        // Item is drink, not food
        try
        {
            manager.addFood(1, "water");
            fail("Expected IllegalArgumentException for drink item in addFood");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }

        // Instructions exceeding 100 characters
        StringBuilder longNote = new StringBuilder();
        for (int i = 0; i < 101; i++)
        {
            longNote.append("x");
        }
        try
        {
            manager.addFood(1, "burger", longNote.toString());
            fail("Expected IllegalArgumentException for instructions > 100 chars");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }

    public void testAddDrinkNormal()
    {
        DrinkOrder order = manager.addDrink(2, "coffee");
        assertEquals(1, order.getOrderId());
        assertEquals(2, order.getTableNumber());
        assertEquals("coffee", order.getItemName());
        assertEquals("", order.getInstructions());
        assertFalse(order.hasInstructions());

        DrinkOrder order2 = manager.addDrink(2, "water");
        assertEquals(2, order2.getOrderId());
    }

    public void testAddDrinkWithInstructions()
    {
        DrinkOrder order = manager.addDrink(4, "lemonade", "extra ice");
        assertEquals("lemonade", order.getItemName());
        assertEquals("extra ice", order.getInstructions());
        assertTrue(order.hasInstructions());
    }

    public void testAddDrinkBadInput()
    {
        // Bad table number: 0
        try
        {
            manager.addDrink(0, "coffee");
            fail("Expected IllegalArgumentException for table 0");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }

        // Bad table number: negative
        try
        {
            manager.addDrink(-3, "water");
            fail("Expected IllegalArgumentException for negative table");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }

        // Null item
        try
        {
            manager.addDrink(1, null);
            fail("Expected IllegalArgumentException for null item");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }

        // Blank item
        try
        {
            manager.addDrink(1, "\t");
            fail("Expected IllegalArgumentException for blank item");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }

        // Item not on menu
        try
        {
            manager.addDrink(1, "tea");
            fail("Expected IllegalArgumentException for item not on menu");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }

        // Item is food, not drink
        try
        {
            manager.addDrink(1, "burger");
            fail("Expected IllegalArgumentException for food item in addDrink");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }

        // Instructions exceeding 100 characters
        StringBuilder longNote = new StringBuilder();
        for (int i = 0; i < 101; i++)
        {
            longNote.append("y");
        }
        try
        {
            manager.addDrink(1, "water", longNote.toString());
            fail("Expected IllegalArgumentException for instructions > 100 chars");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }
    }

    public void testCompleteFood()
    {
        // Bad input: empty kitchen throws NoSuchElementException
        try
        {
            manager.completeFood();
            fail("Expected NoSuchElementException for empty kitchen");
        }
        catch (NoSuchElementException e)
        {
            assertNotNull(e);
        }

        // Normal FIFO completion
        manager.addFood(1, "burger");
        manager.addFood(2, "fries");

        FoodOrder completed1 = manager.completeFood();
        assertEquals(1, completed1.getOrderId());
        assertEquals("burger", completed1.getItemName());

        FoodOrder completed2 = manager.completeFood();
        assertEquals(2, completed2.getOrderId());
        assertEquals("fries", completed2.getItemName());

        // Now empty again
        try
        {
            manager.completeFood();
            fail("Expected NoSuchElementException after all completed");
        }
        catch (NoSuchElementException e)
        {
            assertNotNull(e);
        }
    }

    public void testCompleteDrink()
    {
        // Bad input: empty bar throws NoSuchElementException
        try
        {
            manager.completeDrink();
            fail("Expected NoSuchElementException for empty bar");
        }
        catch (NoSuchElementException e)
        {
            assertNotNull(e);
        }

        // Normal FIFO completion
        manager.addDrink(1, "water");
        manager.addDrink(2, "coffee");

        DrinkOrder completed1 = manager.completeDrink();
        assertEquals(1, completed1.getOrderId());
        assertEquals("water", completed1.getItemName());

        DrinkOrder completed2 = manager.completeDrink();
        assertEquals(2, completed2.getOrderId());
        assertEquals("coffee", completed2.getItemName());

        // Now empty again
        try
        {
            manager.completeDrink();
            fail("Expected NoSuchElementException after all completed");
        }
        catch (NoSuchElementException e)
        {
            assertNotNull(e);
        }
    }

    public void testCancelFood()
    {
        // Bad input on empty
        try
        {
            manager.cancelFood(1);
            fail("Expected IndexOutOfBoundsException for cancel on empty");
        }
        catch (IndexOutOfBoundsException e)
        {
            assertNotNull(e);
        }

        manager.addFood(1, "burger");
        manager.addFood(1, "fries");
        manager.addFood(2, "salad");

        // Bad input: position 0
        try
        {
            manager.cancelFood(0);
            fail("Expected IndexOutOfBoundsException for position 0");
        }
        catch (IndexOutOfBoundsException e)
        {
            assertNotNull(e);
        }

        // Bad input: position out of bounds (> 3)
        try
        {
            manager.cancelFood(4);
            fail("Expected IndexOutOfBoundsException for position 4");
        }
        catch (IndexOutOfBoundsException e)
        {
            assertNotNull(e);
        }

        // Normal cancel middle element (position 2 = fries)
        FoodOrder canceled = manager.cancelFood(2);
        assertEquals("fries", canceled.getItemName());

        // Now size is 2; cancel position 1 = burger
        FoodOrder canceledFirst = manager.cancelFood(1);
        assertEquals("burger", canceledFirst.getItemName());

        // Now size is 1; cancel remaining salad
        FoodOrder canceledLast = manager.cancelFood(1);
        assertEquals("salad", canceledLast.getItemName());

        // Cancelled orders should NOT appear in sent-out log
        assertEquals("No completed orders.", manager.viewLog());
    }

    public void testCancelDrink()
    {
        // Bad input on empty
        try
        {
            manager.cancelDrink(1);
            fail("Expected IndexOutOfBoundsException for cancel on empty");
        }
        catch (IndexOutOfBoundsException e)
        {
            assertNotNull(e);
        }

        manager.addDrink(1, "water");
        manager.addDrink(2, "lemonade");

        // Bad input: negative position
        try
        {
            manager.cancelDrink(-1);
            fail("Expected IndexOutOfBoundsException for negative position");
        }
        catch (IndexOutOfBoundsException e)
        {
            assertNotNull(e);
        }

        // Bad input: position 3
        try
        {
            manager.cancelDrink(3);
            fail("Expected IndexOutOfBoundsException for position 3");
        }
        catch (IndexOutOfBoundsException e)
        {
            assertNotNull(e);
        }

        // Normal cancel position 1
        DrinkOrder canceled = manager.cancelDrink(1);
        assertEquals("water", canceled.getItemName());
        assertEquals("ID 1 | Table 1 | water", "ID " + canceled.getOrderId()
            + " | Table " + canceled.getTableNumber() + " | " + canceled.getItemName());
    }

    public void testViewKitchen()
    {
        assertEquals("No pending food orders.", manager.viewKitchen());

        manager.addFood(5, "burger", "well done");
        String view = manager.viewKitchen();
        assertTrue(view.contains("1. ID 1 | Table 5 | burger (well done)"));
        assertTrue(view.contains("Wait: 0 min"));
    }

    public void testViewBar()
    {
        assertEquals("No pending drink orders.", manager.viewBar());

        manager.addDrink(3, "lemonade");
        String view = manager.viewBar();
        assertTrue(view.contains("1. ID 1 | Table 3 | lemonade"));
        assertTrue(view.contains("Wait: 0 min"));
    }

    public void testViewTable()
    {
        // Bad input: table 0 or negative
        try
        {
            manager.viewTable(0);
            fail("Expected IllegalArgumentException for table 0");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }

        try
        {
            manager.viewTable(-1);
            fail("Expected IllegalArgumentException for table -1");
        }
        catch (IllegalArgumentException e)
        {
            assertNotNull(e);
        }

        // Normal: table with no orders
        assertEquals("No pending orders for table 8.", manager.viewTable(8));

        // Add food and drink for table 8
        manager.addFood(8, "burger", "no mayo");
        manager.addDrink(8, "water");
        manager.addFood(2, "fries"); // different table

        String table8View = manager.viewTable(8);
        assertTrue(table8View.contains("Table 8"));
        assertTrue(table8View.contains("Food"));
        assertTrue(table8View.contains("Drinks"));
        assertTrue(table8View.contains("burger (no mayo)"));
        assertTrue(table8View.contains("water"));
        assertFalse(table8View.contains("fries"));
    }

    public void testViewLog()
    {
        assertEquals("No completed orders.", manager.viewLog());

        manager.addFood(1, "burger");
        manager.addDrink(2, "coffee");

        manager.completeDrink(); // bar completed first
        manager.completeFood();  // food completed second

        String log = manager.viewLog();
        assertTrue(log.startsWith("ID 2 | Table 2 | coffee"));
        assertTrue(log.contains("\nID 1 | Table 1 | burger"));
    }

    public void testDuplicateItemsMaintainUniqueIds()
    {
        FoodOrder f1 = manager.addFood(4, "burger");
        FoodOrder f2 = manager.addFood(4, "burger");

        assertEquals(1, f1.getOrderId());
        assertEquals(2, f2.getOrderId());
        assertFalse(f1.equals(f2));

        // Canceling first order leaves second order intact
        FoodOrder canceled = manager.cancelFood(1);
        assertEquals(1, canceled.getOrderId());

        String kitchenView = manager.viewKitchen();
        assertTrue(kitchenView.contains("ID 2"));
        assertFalse(kitchenView.contains("ID 1"));
    }
}
