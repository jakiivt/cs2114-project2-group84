import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Stores and displays the fixed restaurant menu.
 */
public final class Menu
{
    private final Set<String> foodItems;
    private final Set<String> drinkItems;
    private final List<String> displayItems;

    /**
     * Creates the fixed food and drink menu.
     */
    public Menu()
    {
        foodItems = new HashSet<String>(
            Arrays.asList("burger", "fries", "salad"));

        drinkItems = new HashSet<String>(
            Arrays.asList("water", "coffee", "lemonade"));

        displayItems = Arrays.asList(
            "Food: burger, fries, salad",
            "Drinks: water, coffee, lemonade");
    }

    /**
     * Checks food membership, ignoring case and outer spaces.
     *
     * @param item item name to check
     * @return true for a food item; false for invalid input
     */
    public boolean isFood(String item)
    {
        if (item == null)
        {
            return false;
        }

        return foodItems.contains(
            item.trim().toLowerCase(Locale.ROOT));
    }

    /**
     * Checks drink membership, ignoring case and outer spaces.
     *
     * @param item item name to check
     * @return true for a drink item; false for invalid input
     */
    public boolean isDrink(String item)
    {
        if (item == null)
        {
            return false;
        }

        return drinkItems.contains(
            item.trim().toLowerCase(Locale.ROOT));
    }

    /**
     * @return the food and drink menu sections
     */
    public String display()
    {
        return String.join("\n", displayItems);
    }
}