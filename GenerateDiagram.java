import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Generates a clean, high-resolution system architecture diagram for The Pass.
 */
public class GenerateDiagram
{
    public static void main(String[] args) throws Exception
    {
        int width = 1200;
        int height = 800;
        BufferedImage image = new BufferedImage(
            width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        // Enable anti-aliasing for text and shapes
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        // Background
        g.setColor(new Color(248, 249, 250));
        g.fillRect(0, 0, width, height);

        // Header Banner
        g.setColor(new Color(33, 37, 41));
        g.fillRect(0, 0, width, 80);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.BOLD, 26));
        g.drawString("The Pass — System Architecture & Generic Reuse", 40, 42);

        g.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        g.setColor(new Color(206, 212, 218));
        g.drawString("CS 2114: Building for Reuse | Deliverable 4 — Group 84", 40, 66);

        // Layer 1: Presentation Layer (ConsoleApp)
        drawBox(g, 60, 120, 260, 160,
            new Color(230, 242, 255), new Color(0, 102, 204),
            "ConsoleApp",
            new String[] {
                "- manager: OrderManager",
                "- input: Scanner",
                "+ main(args: String[])",
                "+ run(): void"
            });

        // Menu Box
        drawBox(g, 60, 320, 260, 170,
            new Color(243, 232, 255), new Color(112, 48, 160),
            "Menu",
            new String[] {
                "+ isFood(item: String): boolean",
                "+ isDrink(item: String): boolean",
                "+ display(): String",
                "+ displayFood(): String",
                "+ displayDrinks(): String"
            });

        // Layer 2: Domain Controller (OrderManager)
        drawBox(g, 420, 120, 340, 370,
            new Color(235, 247, 238), new Color(40, 167, 69),
            "OrderManager",
            new String[] {
                "- kitchen: LinkedChain<FoodOrder>",
                "- bar: LinkedChain<DrinkOrder>",
                "- sentOut: LinkedChain<Order>",
                "- menu: Menu",
                "- nextOrderId: int",
                "+ addFood(table, item, [notes]): FoodOrder",
                "+ addDrink(table, item, [notes]): DrinkOrder",
                "+ completeFood(): FoodOrder",
                "+ completeDrink(): DrinkOrder",
                "+ cancelFood(position: int): FoodOrder",
                "+ cancelDrink(position: int): DrinkOrder",
                "+ viewKitchen(): String",
                "+ viewBar(): String",
                "+ viewTable(table: int): String",
                "+ viewLog(): String"
            });

        // Layer 3: Generic LinkedChain Storage Instances
        drawBox(g, 850, 110, 300, 100,
            new Color(254, 243, 199), new Color(217, 119, 6),
            "kitchen: LinkedChain<FoodOrder>",
            new String[] {
                "Element type: FoodOrder",
                "Holds pending kitchen orders in FIFO order",
                "Uses public API only: addToEnd, remove, get"
            });

        drawBox(g, 850, 240, 300, 100,
            new Color(254, 243, 199), new Color(217, 119, 6),
            "bar: LinkedChain<DrinkOrder>",
            new String[] {
                "Element type: DrinkOrder",
                "Holds pending bar orders in FIFO order",
                "Uses public API only: addToEnd, remove, get"
            });

        drawBox(g, 850, 370, 300, 100,
            new Color(254, 243, 199), new Color(217, 119, 6),
            "sentOut: LinkedChain<Order>",
            new String[] {
                "Element type: Order (polymorphic)",
                "Holds completed food & drink orders in FIFO order",
                "Uses public API only: addToEnd, toArray"
            });

        // Layer 4: Generic LinkedChain Contract Definition Box
        drawBox(g, 850, 490, 300, 180,
            new Color(241, 245, 249), new Color(71, 85, 105),
            "LinkedChain<T> (Generic Starter)",
            new String[] {
                "- firstNode: Node<T>",
                "- numberOfEntries: int",
                "+ add(newEntry: T): boolean",
                "+ addToEnd(newEntry: T): boolean",
                "+ remove(): T",
                "+ remove(anEntry: T): boolean",
                "+ get(index: int): T",
                "+ size(): int  |  isEmpty(): boolean",
                "+ toArray(values: T[]): T[]"
            });

        // Layer 5: Domain Models / Hierarchy
        drawBox(g, 420, 540, 340, 210,
            new Color(254, 242, 242), new Color(220, 38, 38),
            "«abstract» Order",
            new String[] {
                "- orderId: int",
                "- tableNumber: int",
                "- itemName: String",
                "- instructions: String [STRETCH GOAL]",
                "- createdAtMillis: long",
                "+ getOrderId(): int  |  getTableNumber(): int",
                "+ getItemName(): String  |  getInstructions(): String",
                "+ getWaitMinutes(now: long): long",
                "+ equals(other: Object): boolean",
                "+ hashCode(): int"
            });

        drawBox(g, 100, 600, 240, 75,
            new Color(254, 242, 242), new Color(220, 38, 38),
            "FoodOrder",
            new String[] {
                "+ FoodOrder(id, tbl, item, [notes], time)",
                "Identifies destination: Kitchen"
            });

        drawBox(g, 100, 695, 240, 75,
            new Color(254, 242, 242), new Color(220, 38, 38),
            "DrinkOrder",
            new String[] {
                "+ DrinkOrder(id, tbl, item, [notes], time)",
                "Identifies destination: Bar"
            });

        // Draw Connecting Arrows
        g.setColor(new Color(108, 117, 125));
        g.setStroke(new BasicStroke(2.0f));

        // ConsoleApp -> OrderManager
        drawArrow(g, 320, 200, 420, 200, "delegates commands");

        // OrderManager -> Menu
        drawArrow(g, 420, 380, 320, 380, "validates items");

        // OrderManager -> kitchen
        drawArrow(g, 760, 160, 850, 160, "manages FIFO");

        // OrderManager -> bar
        drawArrow(g, 760, 290, 850, 290, "manages FIFO");

        // OrderManager -> sentOut
        drawArrow(g, 760, 420, 850, 420, "appends completed");

        // Inheritance: FoodOrder -> Order
        drawInheritanceArrow(g, 340, 635, 420, 635);

        // Inheritance: DrinkOrder -> Order
        drawInheritanceArrow(g, 340, 725, 420, 680);

        // Footer Note
        g.setColor(new Color(100, 116, 139));
        g.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        g.drawString(
            "* LinkedChain is reused without modification across 3 type parameters (FoodOrder, DrinkOrder, Order). App classes never touch nodes.",
            40, 785);

        g.dispose();

        File outputFile = new File("system_diagram.png");
        ImageIO.write(image, "png", outputFile);
        System.out.println("Diagram generated at: " + outputFile.getAbsolutePath());
    }

    private static void drawBox(
        Graphics2D g,
        int x,
        int y,
        int w,
        int h,
        Color bg,
        Color border,
        String title,
        String[] lines)
    {
        // Shadow
        g.setColor(new Color(0, 0, 0, 18));
        g.fillRoundRect(x + 4, y + 4, w, h, 12, 12);

        // Background
        g.setColor(bg);
        g.fillRoundRect(x, y, w, h, 12, 12);

        // Border
        g.setColor(border);
        g.setStroke(new BasicStroke(2.0f));
        g.drawRoundRect(x, y, w, h, 12, 12);

        // Title Header
        g.setColor(border);
        g.fillRoundRect(x, y, w, 32, 12, 12);
        g.fillRect(x, y + 16, w, 16);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.BOLD, 13));
        FontMetrics fm = g.getFontMetrics();
        int titleX = x + (w - fm.stringWidth(title)) / 2;
        g.drawString(title, titleX, y + 21);

        // Content
        g.setColor(new Color(33, 37, 41));
        g.setFont(new Font("Consolas", Font.PLAIN, 11));
        int textY = y + 50;
        for (String line : lines)
        {
            if (line.contains("[STRETCH GOAL]"))
            {
                g.setColor(new Color(180, 40, 40));
                g.setFont(new Font("Consolas", Font.BOLD, 11));
            }
            else
            {
                g.setColor(new Color(33, 37, 41));
                g.setFont(new Font("Consolas", Font.PLAIN, 11));
            }
            g.drawString(line, x + 12, textY);
            textY += 16;
        }
    }

    private static void drawArrow(
        Graphics2D g, int x1, int y1, int x2, int y2, String label)
    {
        g.drawLine(x1, y1, x2, y2);
        drawArrowHead(g, x1, y1, x2, y2);

        if (label != null)
        {
            g.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            g.setColor(new Color(80, 80, 80));
            int midX = (x1 + x2) / 2;
            int midY = (y1 + y2) / 2 - 6;
            g.drawString(label, midX - 35, midY);
            g.setColor(new Color(108, 117, 125));
        }
    }

    private static void drawInheritanceArrow(
        Graphics2D g, int x1, int y1, int x2, int y2)
    {
        g.drawLine(x1, y1, x2, y2);
        // Closed triangle for inheritance
        Polygon p = new Polygon();
        p.addPoint(x2, y2);
        p.addPoint(x2 - 12, y2 - 6);
        p.addPoint(x2 - 12, y2 + 6);
        g.setColor(Color.WHITE);
        g.fillPolygon(p);
        g.setColor(new Color(108, 117, 125));
        g.drawPolygon(p);
    }

    private static void drawArrowHead(
        Graphics2D g, int x1, int y1, int x2, int y2)
    {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int len = 10;
        int xA = (int)(x2 - len * Math.cos(angle - Math.PI / 6));
        int yA = (int)(y2 - len * Math.sin(angle - Math.PI / 6));
        int xB = (int)(x2 - len * Math.cos(angle + Math.PI / 6));
        int yB = (int)(y2 - len * Math.sin(angle + Math.PI / 6));

        Polygon p = new Polygon();
        p.addPoint(x2, y2);
        p.addPoint(xA, yA);
        p.addPoint(xB, yB);
        g.fillPolygon(p);
    }
}
