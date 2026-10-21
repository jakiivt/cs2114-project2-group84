import java.util.Scanner;

/**
 * Unit and integration tests for ConsoleApp.
 * Tests menu navigation, input validation, error recovery,
 * bad input handling, and EOF termination.
 *
 * @author Talal, Jack, Bryan, Abigail
 * @version Fall 2026
 */
public class ConsoleAppTest extends student.TestCase
{
    public void testConstructors()
    {
        ConsoleApp app1 = new ConsoleApp();
        assertNotNull(app1);

        OrderManager manager = new OrderManager(new Menu());
        Scanner scanner = new Scanner("0\n");
        ConsoleApp app2 = new ConsoleApp(manager, scanner);
        assertNotNull(app2);
        app2.run();
        assertTrue(systemOut().getHistory().contains("Goodbye!"));
    }

    public void testExitImmediately()
    {
        setSystemIn("0\n");
        ConsoleApp.main(new String[0]);
        String history = systemOut().getHistory();
        assertTrue(history.contains("--- The Pass ---"));
        assertTrue(history.contains("Goodbye!"));
    }

    public void testAddFoodAndExit()
    {
        setSystemIn("1\n4\nburger\nno onions\n0\n");
        ConsoleApp.main(new String[0]);
        String history = systemOut().getHistory();
        assertTrue(history.contains("Food: burger, fries, salad"));
        assertTrue(history.contains("Added order ID 1."));
        assertTrue(history.contains("Goodbye!"));
    }

    public void testAddDrinkAndExit()
    {
        setSystemIn("2\n3\nwater\n\n0\n");
        ConsoleApp.main(new String[0]);
        String history = systemOut().getHistory();
        assertTrue(history.contains("Drinks: water, coffee, lemonade"));
        assertTrue(history.contains("Added order ID 1."));
        assertTrue(history.contains("Goodbye!"));
    }

    public void testViewKitchenAndBar()
    {
        setSystemIn("3\n4\n0\n");
        ConsoleApp.main(new String[0]);
        String history = systemOut().getHistory();
        assertTrue(history.contains("No pending food orders."));
        assertTrue(history.contains("No pending drink orders."));
    }

    public void testCompleteFoodAndDrink()
    {
        setSystemIn("1\n1\nburger\n\n2\n1\nwater\n\n5\n6\n0\n");
        ConsoleApp.main(new String[0]);
        String history = systemOut().getHistory();
        assertTrue(history.contains("Completed order ID 1."));
        assertTrue(history.contains("Completed order ID 2."));
    }

    public void testCompleteEmptyQueues()
    {
        setSystemIn("5\n6\n0\n");
        ConsoleApp.main(new String[0]);
        String history = systemOut().getHistory();
        assertTrue(history.contains("There are no pending orders to complete."));
    }

    public void testCancelFoodAndDrink()
    {
        setSystemIn("1\n2\nfries\n\n2\n2\nlemonade\n\n7\n1\n8\n1\n0\n");
        ConsoleApp.main(new String[0]);
        String history = systemOut().getHistory();
        assertTrue(history.contains("Canceled order ID 1."));
        assertTrue(history.contains("Canceled order ID 2."));
    }

    public void testCancelGoBack()
    {
        setSystemIn("1\n1\nsalad\n\n7\n0\n0\n");
        ConsoleApp.main(new String[0]);
        String history = systemOut().getHistory();
        assertTrue(history.contains("Added order ID 1."));
        assertTrue(history.contains("Goodbye!"));
    }

    public void testCancelDrinkGoBack()
    {
        setSystemIn("2\n1\ncoffee\n\n8\n0\n0\n");
        ConsoleApp.main(new String[0]);
        String history = systemOut().getHistory();
        assertTrue(history.contains("Added order ID 1."));
        assertTrue(history.contains("Goodbye!"));
    }

    public void testViewTableAndLog()
    {
        setSystemIn("1\n7\nburger\n\n9\n7\n5\n10\n0\n");
        ConsoleApp.main(new String[0]);
        String history = systemOut().getHistory();
        assertTrue(history.contains("Table 7"));
        assertTrue(history.contains("Completed order ID 1."));
        assertTrue(history.contains("ID 1 | Table 7 | burger"));
    }

    public void testBadInputsAndRecovery()
    {
        // Invalid command, bad table input (non-numeric and negative), bad menu item
        setSystemIn("99\n1\nabc\n-2\n3\npizza\n\nburger\n\n0\n");
        ConsoleApp.main(new String[0]);
        String history = systemOut().getHistory();
        assertTrue(history.contains("Choose a command from 0 to 10."));
        assertTrue(history.contains("Enter a valid whole number."));
        assertTrue(history.contains("Enter a positive table number."));
        assertTrue(history.contains("Item is not on the menu."));
        assertTrue(history.contains("Added order ID 1."));
    }

    public void testCancelBadPositionRecovery()
    {
        setSystemIn("1\n1\nburger\n\n7\n99\n1\n0\n");
        ConsoleApp.main(new String[0]);
        String history = systemOut().getHistory();
        assertTrue(history.contains("Choose a position shown in the queue."));
        assertTrue(history.contains("Canceled order ID 1."));
    }

    public void testEofHandling()
    {
        // Empty input stream (immediate EOF)
        setSystemIn("");
        ConsoleApp.main(new String[0]);

        // EOF during command prompt
        setSystemIn("1\n");
        ConsoleApp.main(new String[0]);

        // EOF during table prompt
        setSystemIn("9\n");
        ConsoleApp.main(new String[0]);
    }
}
