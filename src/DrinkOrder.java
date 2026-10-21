/**
 * A drink order waiting in the bar queue.
 */
public final class DrinkOrder extends Order
{
    /**
     * Creates a drink order.
     *
     * @param id positive order ID
     * @param table positive table number
     * @param item nonblank drink item name
     * @param createdAtMillis nonnegative creation time
     * @throws IllegalArgumentException if any field is invalid
     */
    public DrinkOrder(
        int id,
        int table,
        String item,
        long createdAtMillis)
    {
        super(id, table, item, createdAtMillis);
    }

    /**
     * Creates a drink order with special instructions.
     *
     * @param id positive order ID
     * @param table positive table number
     * @param item nonblank drink item name
     * @param instructions special instructions or notes
     * @param createdAtMillis nonnegative creation time
     * @throws IllegalArgumentException if any field is invalid
     */
    public DrinkOrder(
        int id,
        int table,
        String item,
        String instructions,
        long createdAtMillis)
    {
        super(id, table, item, instructions, createdAtMillis);
    }
}