import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Locale;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the fixed menu, input normalization, and invalid inputs.
 */
public class MenuTest
{
    private Menu menu;

    /** Creates a fresh menu before each test. */
    @Before
    public void setUp()
    {
        menu = new Menu();
    }

    /** Checks every food item and its category. */
    @Test
    public void testFoodItems()
    {
        for (String item : new String[] {"burger", "fries", "salad"})
        {
            assertTrue(menu.isFood(item));
            assertFalse(menu.isDrink(item));
        }
    }

    /** Checks case and surrounding whitespace for food. */
    @Test
    public void testFoodNormalization()
    {
        assertTrue(menu.isFood(" Burger "));
        assertTrue(menu.isFood("FRIES"));
        assertTrue(menu.isFood("\tSalad\t"));
    }

    /** Checks null, empty, blank, unknown, and wrong-category food input. */
    @Test
    public void testInvalidFoodInputs()
    {
        assertFalse(menu.isFood(null));
        assertFalse(menu.isFood(""));
        assertFalse(menu.isFood(" \t "));
        assertFalse(menu.isFood("pizza"));
        assertFalse(menu.isFood(" WATER "));
    }

    /** Checks every drink item and its category. */
    @Test
    public void testDrinkItems()
    {
        for (String item : new String[] {"water", "coffee", "lemonade"})
        {
            assertTrue(menu.isDrink(item));
            assertFalse(menu.isFood(item));
        }
    }

    /** Checks case and surrounding whitespace for drinks. */
    @Test
    public void testDrinkNormalization()
    {
        assertTrue(menu.isDrink(" WATER "));
        assertTrue(menu.isDrink("Coffee"));
        assertTrue(menu.isDrink("\tLEMONADE\t"));
    }

    /** Checks null, empty, blank, unknown, and wrong-category drink input. */
    @Test
    public void testInvalidDrinkInputs()
    {
        assertFalse(menu.isDrink(null));
        assertFalse(menu.isDrink(""));
        assertFalse(menu.isDrink(" \t "));
        assertFalse(menu.isDrink("juice"));
        assertFalse(menu.isDrink(" BURGER "));
    }

    /** Checks the exact text, labels, item order, and newline. */
    @Test
    public void testDisplay()
    {
        String expected = "Food: burger, fries, salad\n"
            + "Drinks: water, coffee, lemonade";

        assertEquals(expected, menu.display());
    }

    /** Checks that repeated display calls preserve the menu. */
    @Test
    public void testDisplayDoesNotChangeMenu()
    {
        String firstDisplay = menu.display();

        assertEquals(firstDisplay, menu.display());
        assertEquals(firstDisplay, menu.display());
        assertTrue(menu.isFood("burger"));
        assertTrue(menu.isDrink("water"));
        assertFalse(menu.isFood("water"));
        assertFalse(menu.isDrink("burger"));
    }

    /** Checks consistent construction of separate menu objects. */
    @Test
    public void testNewMenusHaveSameContents()
    {
        Menu anotherMenu = new Menu();

        assertEquals(menu.display(), anotherMenu.display());
        assertTrue(anotherMenu.isFood("burger"));
        assertTrue(anotherMenu.isDrink("water"));
    }

    /** Checks normalization when the default locale has different case rules. */
    @Test
    public void testNormalizationIgnoresDefaultLocale()
    {
        Locale originalLocale = Locale.getDefault();

        try
        {
            Locale.setDefault(new Locale("tr", "TR"));
            assertTrue(menu.isFood("FRIES"));
        }
        finally
        {
            Locale.setDefault(originalLocale);
        }
    }
}
