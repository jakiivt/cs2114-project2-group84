import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * Manages pending food, pending drinks, and completed orders.
 */
public class OrderManager
{
    private final LinkedChain<FoodOrder> kitchen;
    private final LinkedChain<DrinkOrder> bar;
    private final LinkedChain<Order> sentOut;
    private final Menu menu;
    private int nextOrderId;

    /**
     * Creates an empty order manager.
     *
     * @param menu the restaurant menu
     * @throws IllegalArgumentException if menu is null
     */
    public OrderManager(Menu menu)
    {
        if (menu == null)
        {
            throw new IllegalArgumentException(
                "Menu must not be null.");
        }

        this.menu = menu;
        kitchen = new LinkedChain<FoodOrder>();
        bar = new LinkedChain<DrinkOrder>();
        sentOut = new LinkedChain<Order>();
        nextOrderId = 1;
    }

    /**
     * @return the restaurant menu
     */
    public Menu getMenu()
    {
        return menu;
    }

    /**
     * Adds a food order with no special instructions to the kitchen queue.
     *
     * @param table positive table number
     * @param item food item name
     * @return the new order
     */
    public FoodOrder addFood(int table, String item)
    {
        return addFood(table, item, "");
    }

    /**
     * Adds a food order with optional special instructions to the kitchen queue.
     *
     * @param table positive table number
     * @param item food item name
     * @param instructions special instructions or notes
     * @return the new order
     */
    public FoodOrder addFood(
        int table,
        String item,
        String instructions)
    {
        String name = validateItem(table, item, true);
        checkIdAvailable();

        FoodOrder order = new FoodOrder(
            nextOrderId,
            table,
            name,
            instructions,
            System.currentTimeMillis());

        kitchen.addToEnd(order);
        advanceId();
        return order;
    }

    /**
     * Adds a drink order with no special instructions to the bar queue.
     *
     * @param table positive table number
     * @param item drink item name
     * @return the new order
     */
    public DrinkOrder addDrink(int table, String item)
    {
        return addDrink(table, item, "");
    }

    /**
     * Adds a drink order with optional special instructions to the bar queue.
     *
     * @param table positive table number
     * @param item drink item name
     * @param instructions special instructions or notes
     * @return the new order
     */
    public DrinkOrder addDrink(
        int table,
        String item,
        String instructions)
    {
        String name = validateItem(table, item, false);
        checkIdAvailable();

        DrinkOrder order = new DrinkOrder(
            nextOrderId,
            table,
            name,
            instructions,
            System.currentTimeMillis());

        bar.addToEnd(order);
        advanceId();
        return order;
    }

    /**
     * Completes the oldest pending food order.
     *
     * @return the completed order
     * @throws NoSuchElementException if the kitchen is empty
     */
    public FoodOrder completeFood()
    {
        if (kitchen.isEmpty())
        {
            throw new NoSuchElementException(
                "There are no pending orders to complete.");
        }

        FoodOrder order = kitchen.remove();
        sentOut.addToEnd(order);
        return order;
    }

    /**
     * Completes the oldest pending drink order.
     *
     * @return the completed order
     * @throws NoSuchElementException if the bar is empty
     */
    public DrinkOrder completeDrink()
    {
        if (bar.isEmpty())
        {
            throw new NoSuchElementException(
                "There are no pending orders to complete.");
        }

        DrinkOrder order = bar.remove();
        sentOut.addToEnd(order);
        return order;
    }

    /**
     * Cancels a pending food order without logging it.
     *
     * @param position one-based displayed position
     * @return the canceled order
     * @throws IndexOutOfBoundsException if position is invalid
     */
    public FoodOrder cancelFood(int position)
    {
        validatePosition(position, kitchen.size());
        FoodOrder order = kitchen.get(position - 1);
        kitchen.remove(order);
        return order;
    }

    /**
     * Cancels a pending drink order without logging it.
     *
     * @param position one-based displayed position
     * @return the canceled order
     * @throws IndexOutOfBoundsException if position is invalid
     */
    public DrinkOrder cancelDrink(int position)
    {
        validatePosition(position, bar.size());
        DrinkOrder order = bar.get(position - 1);
        bar.remove(order);
        return order;
    }

    /**
     * @return pending food orders in arrival order
     */
    public String viewKitchen()
    {
        if (kitchen.isEmpty())
        {
            return "No pending food orders.";
        }

        FoodOrder[] orders =
            kitchen.toArray(new FoodOrder[kitchen.size()]);
        long now = System.currentTimeMillis();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < orders.length; i++)
        {
            if (i > 0)
            {
                result.append("\n");
            }

            result.append(i + 1).append(". ")
                .append(formatPending(orders[i], now));
        }

        return result.toString();
    }

    /**
     * @return pending drink orders in arrival order
     */
    public String viewBar()
    {
        if (bar.isEmpty())
        {
            return "No pending drink orders.";
        }

        DrinkOrder[] orders =
            bar.toArray(new DrinkOrder[bar.size()]);
        long now = System.currentTimeMillis();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < orders.length; i++)
        {
            if (i > 0)
            {
                result.append("\n");
            }

            result.append(i + 1).append(". ")
                .append(formatPending(orders[i], now));
        }

        return result.toString();
    }

    /**
     * Displays a table's pending food and drinks in adjacent columns.
     *
     * @param table positive table number
     * @return the table's pending orders
     * @throws IllegalArgumentException if table is not positive
     */
    public String viewTable(int table)
    {
        validateTable(table);

        FoodOrder[] foods =
            kitchen.toArray(new FoodOrder[kitchen.size()]);
        DrinkOrder[] drinks =
            bar.toArray(new DrinkOrder[bar.size()]);

        long now = System.currentTimeMillis();
        StringBuilder foodText = new StringBuilder();
        StringBuilder drinkText = new StringBuilder();

        for (FoodOrder order : foods)
        {
            if (order.getTableNumber() == table)
            {
                foodText.append(formatPending(order, now))
                    .append("\n");
            }
        }

        for (DrinkOrder order : drinks)
        {
            if (order.getTableNumber() == table)
            {
                drinkText.append(formatPending(order, now))
                    .append("\n");
            }
        }

        if (foodText.length() == 0 && drinkText.length() == 0)
        {
            return "No pending orders for table " + table + ".";
        }

        String[] foodRows = foodText.length() == 0
            ? new String[0] : foodText.toString().split("\n");
        String[] drinkRows = drinkText.length() == 0
            ? new String[0] : drinkText.toString().split("\n");

        int width = "Food".length();
        for (String row : foodRows)
        {
            width = Math.max(width, row.length());
        }

        StringBuilder result = new StringBuilder(
            "Table " + table + "\n");
        appendColumns(result, "Food", "Drinks", width);

        int rows = Math.max(foodRows.length, drinkRows.length);
        for (int i = 0; i < rows; i++)
        {
            result.append("\n");
            String food = i < foodRows.length ? foodRows[i] : "";
            String drink = i < drinkRows.length ? drinkRows[i] : "";
            appendColumns(result, food, drink, width);
        }

        return result.toString();
    }

    /**
     * @return completed orders in completion order
     */
    public String viewLog()
    {
        if (sentOut.isEmpty())
        {
            return "No completed orders.";
        }

        Order[] orders =
            sentOut.toArray(new Order[sentOut.size()]);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < orders.length; i++)
        {
            if (i > 0)
            {
                result.append("\n");
            }

            result.append(formatOrder(orders[i]));
        }

        return result.toString();
    }

    private void validateTable(int table)
    {
        if (table <= 0)
        {
            throw new IllegalArgumentException(
                "Enter a positive table number.");
        }
    }

    private String validateItem(
        int table, String item, boolean food)
    {
        validateTable(table);

        if (item == null || item.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "Enter a menu item.");
        }

        String name = item.trim().toLowerCase(Locale.ROOT);

        if (!menu.isFood(name) && !menu.isDrink(name))
        {
            throw new IllegalArgumentException(
                "Item is not on the menu.");
        }

        if ((food && !menu.isFood(name))
            || (!food && !menu.isDrink(name)))
        {
            throw new IllegalArgumentException(
                "Choose an item from the selected menu.");
        }

        return name;
    }

    private void validatePosition(int position, int size)
    {
        if (position <= 0 || position > size)
        {
            throw new IndexOutOfBoundsException(
                "Choose a position shown in the queue.");
        }
    }

    private void checkIdAvailable()
    {
        if (nextOrderId <= 0)
        {
            throw new IllegalStateException(
                "No more order IDs are available.");
        }
    }

    private void advanceId()
    {
        if (nextOrderId == Integer.MAX_VALUE)
        {
            nextOrderId = 0;
        }
        else
        {
            nextOrderId++;
        }
    }

    private String formatOrder(Order order)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ID ").append(order.getOrderId())
            .append(" | Table ").append(order.getTableNumber())
            .append(" | ").append(order.getItemName());
        if (order.hasInstructions())
        {
            sb.append(" (").append(order.getInstructions()).append(")");
        }
        return sb.toString();
    }

    private String formatPending(Order order, long now)
    {
        return formatOrder(order)
            + " | Wait: " + order.getWaitMinutes(now) + " min";
    }

    private void appendColumns(
        StringBuilder result, String left, String right, int width)
    {
        result.append(left);
        for (int i = left.length(); i < width; i++)
        {
            result.append(' ');
        }
        result.append(" | ").append(right);
    }
}