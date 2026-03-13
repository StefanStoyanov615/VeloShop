import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UI {
    // --- COLORS ---
    public static final Color PRIMARY = new Color(44, 62, 80);    // Midnight Blue (Sidebar)
    public static final Color SECONDARY = new Color(52, 73, 94);  // Lighter Blue (Sidebar Hover)
    public static final Color ACCENT = new Color(39, 174, 96);    // Emerald Green (Save/Action)
    public static final Color WARNING = new Color(231, 76, 60);   // Red (Delete)
    public static final Color BACKGROUND = new Color(236, 240, 241); // Light Gray Background
    public static final Color TEXT_DARK = new Color(44, 62, 80);
    public static final Color EDIT = new Color(243, 156, 18);
    public static final Color TEXT_WHITE = Color.WHITE;

    // --- FONTS ---
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_PLAIN = new Font("Segoe UI", Font.PLAIN, 14);

    /**
     * Creates a standard Action Button (e.g., Save, Pay, Login)
     */
    public static JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);

        // FORCE FLAT STYLE (Fixes the white button issue)
        btn.setUI(new BasicButtonUI());

        btn.setBackground(bg);
        btn.setForeground(TEXT_WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover Effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Darken the color slightly
                btn.setBackground(bg.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });

        return btn;
    }

    /**
     * Creates a Sidebar Menu Button (The dark ones on the left)
     */
    public static JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_PLAIN);

        // FORCE FLAT STYLE (Fixes the white button issue)
        btn.setUI(new BasicButtonUI());

        btn.setBackground(PRIMARY);
        btn.setForeground(TEXT_WHITE);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        // Add a bottom line separator
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, SECONDARY),
                new EmptyBorder(15, 20, 15, 20)
        ));

        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover Effect (Change from Dark Blue to Lighter Blue)
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(SECONDARY);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(PRIMARY);
            }
        });

        return btn;
    }

    /**
     * Creates a standardized Text Field
     */
    public static JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(FONT_PLAIN);
        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(189, 195, 199), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        txt.setBackground(Color.WHITE);
        txt.setForeground(TEXT_DARK);
        return txt;
    }

    /**
     * Creates a standardized Label
     */
    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_BOLD);
        lbl.setForeground(TEXT_DARK);
        return lbl;
    }

    /**
     * Styles the Tables consistently
     */
    public static void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(FONT_PLAIN);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(52, 152, 219)); // Highlight Blue
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Header Style
        table.getTableHeader().setFont(FONT_BOLD);
        table.getTableHeader().setBackground(PRIMARY); // Dark Header
        table.getTableHeader().setForeground(TEXT_WHITE); // White Text
        table.getTableHeader().setBorder(null);

        // Fix Header Alignment
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer)
                table.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.LEFT);
    }
}