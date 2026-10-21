/**
 * A food order waiting in the kitchen queue.
 */
public final class FoodOrder extends Order
{
    /**
     * Creates a food order.
     *
     * @param id positive order ID
     * @param table positive table number
     * @param item nonblank food item name
     * @param createdAtMillis nonnegative creation time
     * @throws IllegalArgumentException if any field is invalid
     */
    public FoodOrder(
        int id,
        int table,
        String item,
        long createdAtMillis)
    {
        super(id, table, item, createdAtMillis);
    }

    /**
     * Creates a food order with special instructions.
     *
     * @param id positive order ID
     * @param table positive table number
     * @param item nonblank food item name
     * @param instructions special instructions or notes
     * @param createdAtMillis nonnegative creation time
     * @throws IllegalArgumentException if any field is invalid
     */
    public FoodOrder(
        int id,
        int table,
        String item,
        String instructions,
        long createdAtMillis)
    {
        super(id, table, item, instructions, createdAtMillis);
    }
}