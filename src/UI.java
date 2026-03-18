import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UI {
    public static final Color PRIMARY = new Color(44, 62, 80);
    public static final Color SECONDARY = new Color(52, 73, 94);
    public static final Color ACCENT = new Color(39, 174, 96);
    public static final Color WARNING = new Color(231, 76, 60);
    public static final Color BACKGROUND = new Color(236, 240, 241);
    public static final Color TEXT_DARK = new Color(44, 62, 80);
    public static final Color EDIT = new Color(243, 156, 18);
    public static final Color TEXT_WHITE = Color.WHITE;

    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_PLAIN = new Font("Segoe UI", Font.PLAIN, 14);

    public static JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BOLD);

        btn.setUI(new BasicButtonUI());

        btn.setBackground(bg);
        btn.setForeground(TEXT_WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });

        return btn;
    }

    public static JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_PLAIN);

        btn.setUI(new BasicButtonUI());

        btn.setBackground(PRIMARY);
        btn.setForeground(TEXT_WHITE);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, SECONDARY), new EmptyBorder(15, 20, 15, 20)));

        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

    public static JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(FONT_PLAIN);
        txt.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(189, 195, 199), 1), new EmptyBorder(5, 10, 5, 10)));
        txt.setBackground(Color.WHITE);
        txt.setForeground(TEXT_DARK);
        return txt;
    }

    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_BOLD);
        lbl.setForeground(TEXT_DARK);
        return lbl;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(FONT_PLAIN);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        table.getTableHeader().setFont(FONT_BOLD);
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(TEXT_WHITE);
        table.getTableHeader().setBorder(null);

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.LEFT);
    }
}