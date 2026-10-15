/**
 * Common immutable data for food and drink orders.
 */
public abstract class Order
{
    private final int orderId;
    private final int tableNumber;
    private final String itemName;
    private final long createdAtMillis;

    /**
     * Creates an order after validating its fields.
     *
     * @param id positive order ID
     * @param table positive table number
     * @param item nonblank item name
     * @param creationTime nonnegative creation time in milliseconds
     * @throws IllegalArgumentException if any field is invalid
     */
    protected Order(int id, int table, String item, long creationTime)
    {
        if (id <= 0)
        {
            throw new IllegalArgumentException(
                "Order ID must be positive.");
        }
        if (table <= 0)
        {
            throw new IllegalArgumentException(
                "Enter a positive table number.");
        }
        if (item == null || item.trim().isEmpty())
        {
            throw new IllegalArgumentException(
                "Enter a menu item.");
        }
        if (creationTime < 0)
        {
            throw new IllegalArgumentException(
                "Creation time must be nonnegative.");
        }

        orderId = id;
        tableNumber = table;
        itemName = item.trim();
        createdAtMillis = creationTime;
    }

    /**
     * @return this order's ID
     */
    public int getOrderId()
    {
        return orderId;
    }

    /**
     * @return this order's table number
     */
    public int getTableNumber()
    {
        return tableNumber;
    }

    /**
     * @return the trimmed item name
     */
    public String getItemName()
    {
        return itemName;
    }

    /**
     * @return creation time in milliseconds
     */
    public long getCreatedAtMillis()
    {
        return createdAtMillis;
    }

    /**
     * Calculates elapsed whole minutes.
     *
     * @param nowMillis current time in milliseconds
     * @return elapsed whole minutes since creation
     * @throws IllegalArgumentException if now precedes creation
     */
    public long getWaitMinutes(long nowMillis)
    {
        if (nowMillis < createdAtMillis)
        {
            throw new IllegalArgumentException(
                "Current time cannot precede creation time.");
        }

        return (nowMillis - createdAtMillis) / 60000L;
    }

    /**
     * Compares orders by ID, including across order subclasses.
     *
     * @param other object to compare
     * @return true if both orders have the same ID
     */
    @Override
    public final boolean equals(Object other)
    {
        if (!(other instanceof Order))
        {
            return false;
        }

        Order otherOrder = (Order)other;
        return orderId == otherOrder.orderId;
    }

    /**
     * @return a hash based on the order ID
     */
    @Override
    public final int hashCode()
    {
        return Integer.hashCode(orderId);
    }
}