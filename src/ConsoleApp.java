import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Console interface for the restaurant order system.
 */
public class ConsoleApp
{
    private OrderManager manager;
    private Scanner input;

    /**
     * Starts the application. Command-line arguments are ignored.
     *
     * @param args unused command-line arguments
     */
    /**
     * Starts the application. Command-line arguments are ignored.
     *
     * @param args unused command-line arguments
     */
    public static void main(String[] args)
    {
        ConsoleApp app = new ConsoleApp();
        app.run();
    }

    /**
     * Creates a console app with default manager and input.
     */
    public ConsoleApp()
    {
        this(new OrderManager(new Menu()), new Scanner(System.in));
    }

    /**
     * Creates a console app with custom manager and input.
     *
     * @param manager the order manager
     * @param input the input scanner
     */
    public ConsoleApp(OrderManager manager, Scanner input)
    {
        this.manager = manager;
        this.input = input;
    }

    /**
     * Runs the console command loop.
     */
    public void run()
    {
        boolean running = true;

        while (running)
        {
            displayCommands();
            String command = readLine("Choose a command: ");

            if (command == null)
            {
                return;
            }

            try
            {
                switch (command.trim())
                {
                    case "1":
                        running = addOrder(true);
                        break;
                    case "2":
                        running = addOrder(false);
                        break;
                    case "3":
                        System.out.println(manager.viewKitchen());
                        break;
                    case "4":
                        System.out.println(manager.viewBar());
                        break;
                    case "5":
                        showSuccess(
                            "Completed", manager.completeFood());
                        break;
                    case "6":
                        showSuccess(
                            "Completed", manager.completeDrink());
                        break;
                    case "7":
                        running = cancelOrder(true);
                        break;
                    case "8":
                        running = cancelOrder(false);
                        break;
                    case "9":
                        running = showTable();
                        break;
                    case "10":
                        System.out.println(manager.viewLog());
                        break;
                    case "0":
                        System.out.println("Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println(
                            "Choose a command from 0 to 10.");
                        break;
                }
            }
            catch (NoSuchElementException exception)
            {
                System.out.println(exception.getMessage());
            }
            catch (IllegalStateException exception)
            {
                System.out.println(exception.getMessage());
            }
        }
    }

    private void displayCommands()
    {
        System.out.println("\n--- The Pass ---");
        System.out.println("1  Add food");
        System.out.println("2  Add drink");
        System.out.println("3  View kitchen");
        System.out.println("4  View bar");
        System.out.println("5  Complete food");
        System.out.println("6  Complete drink");
        System.out.println("7  Cancel food");
        System.out.println("8  Cancel drink");
        System.out.println("9  View table");
        System.out.println("10 View sent-out log");
        System.out.println("0  Exit");
    }

    private boolean addOrder(boolean food)
    {
        System.out.println(food
            ? manager.getMenu().displayFood()
            : manager.getMenu().displayDrinks());

        Integer table = readPositiveTable();

        if (table == null)
        {
            return false;
        }

        while (true)
        {
            String item = readLine("Enter item: ");

            if (item == null)
            {
                return false;
            }

            String notes = readLine(
                "Enter special instructions (optional, press Enter to skip): ");

            if (notes == null)
            {
                return false;
            }

            try
            {
                Order order;

                if (food)
                {
                    order = manager.addFood(table, item, notes);
                }
                else
                {
                    order = manager.addDrink(table, item, notes);
                }

                showSuccess("Added", order);
                return true;
            }
            catch (IllegalArgumentException exception)
            {
                System.out.println(exception.getMessage());
            }
        }
    }

    private boolean cancelOrder(boolean food)
    {
        System.out.println(food
            ? manager.viewKitchen() : manager.viewBar());

        while (true)
        {
            Integer position = readWholeNumber(
                "Enter position to cancel (0 to go back): ");

            if (position == null)
            {
                return false;
            }

            if (position == 0)
            {
                return true;
            }

            try
            {
                Order order;

                if (food)
                {
                    order = manager.cancelFood(position);
                }
                else
                {
                    order = manager.cancelDrink(position);
                }

                showSuccess("Canceled", order);
                return true;
            }
            catch (IndexOutOfBoundsException exception)
            {
                System.out.println(exception.getMessage());
            }
        }
    }

    private boolean showTable()
    {
        Integer table = readPositiveTable();

        if (table == null)
        {
            return false;
        }

        System.out.println(manager.viewTable(table));
        return true;
    }

    private Integer readPositiveTable()
    {
        while (true)
        {
            Integer table = readWholeNumber(
                "Enter table number: ");

            if (table == null)
            {
                return null;
            }

            if (table > 0)
            {
                return table;
            }

            System.out.println(
                "Enter a positive table number.");
        }
    }

    private Integer readWholeNumber(String prompt)
    {
        while (true)
        {
            String text = readLine(prompt);

            if (text == null)
            {
                return null;
            }

            try
            {
                return Integer.valueOf(text.trim());
            }
            catch (NumberFormatException exception)
            {
                System.out.println(
                    "Enter a valid whole number.");
            }
        }
    }

    private String readLine(String prompt)
    {
        System.out.print(prompt);

        if (!input.hasNextLine())
        {
            return null;
        }

        return input.nextLine();
    }

    private void showSuccess(String action, Order order)
    {
        System.out.println(
            action + " order ID " + order.getOrderId() + ".");
    }
}