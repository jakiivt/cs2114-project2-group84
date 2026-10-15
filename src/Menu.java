import java.util.Locale;

/**
 * Provides a fixed menu of food and drinks.
 */
public class Menu
{
    /**
     * Checks whether the item is a food.
     * @param item the item to check
     * @return true if the item is food
     */
    public boolean isFood(String item)
    {
        if (item == null)
        {
            return false;
        }

        String normalized = item.trim().toLowerCase(Locale.ROOT);

        return normalized.equals("burger")
            || normalized.equals("fries")
            || normalized.equals("salad");
    }

    /**
     * Checks whether the item is a drink.
     * @param item the item to check
     * @return true if the item is a drink
     */
    public boolean isDrink(String item)
    {
        if (item == null)
        {
            return false;
        }

        String normalized = item.trim().toLowerCase(Locale.ROOT);

        return normalized.equals("water")
            || normalized.equals("coffee")
            || normalized.equals("lemonade");
    }

    /**
     * Returns the formatted menu.
     * @return the food and drink menu
     */
    public String display()
    {
        return "Food: burger, fries, salad\n"
            + "Drinks: water, coffee, lemonade";
    }
}