import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class MainDashboard extends JFrame {
    private LibraryManager manager = new LibraryManager();

    public MainDashboard() {
        setTitle("Library Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(true);

        JPanel header = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(135, 206, 250),
                        getWidth(), getHeight(), new Color(30, 144, 255));
                g2d.setPaint(gp); g2d.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        header.setPreferredSize(new Dimension(800, 80));
        JLabel title = new JLabel("Library Management System", JLabel.CENTER);
        title.setForeground(Color.BLACK);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        header.setLayout(new BorderLayout());
        header.add(title, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(2, 4, 28, 28));
        center.setBackground(new Color(253, 245, 230));
        center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        String[] actions = {"Add Book", "View Books", "Add Student",
                "Issue Book", "Return Book", "Delete Book", "Reports", "About"};
        for (String act : actions) {
            JButton btn = createModernButton(act);
            center.add(btn);
            btn.addActionListener(e -> handleButtonClick(act));
        }
        add(center, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createModernButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(135, 206, 250)); // Light blue
        btn.setForeground(new Color(40, 53, 80));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(180, 64)); // Landscape rectangle
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(70, 130, 180)); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(new Color(135, 206, 250)); }
        });
        btn.setBorder(BorderFactory.createLineBorder(new Color(25, 25, 112), 2, true));
        return btn;
    }


    private void handleButtonClick(String act) {
        switch (act) {
            case "Add Book": showAddBookDialog(); break;
            case "View Books": showBooksTable(); break;
            case "Add Student": showAddStudentDialog(); break;
            case "Issue Book": showIssueBookDialog(); break;
            case "Return Book": showReturnBookDialog(); break;
            case "Delete Book": showDeleteBookDialog(); break;
            case "Reports": showReportsDialog(); break;
            case "About": showAboutDialog(); break;
        }
    }

    private void showAddBookDialog() {
        JPanel panel = new JPanel(new GridLayout(5,2));
        JTextField id = new JTextField(), title = new JTextField(), author = new JTextField(), quantity = new JTextField();
        JComboBox<String> cat = new JComboBox<>(new String[]{"Fiction","Science","History","Art","Other"});
        panel.add(new JLabel("Book ID")); panel.add(id);
        panel.add(new JLabel("Title")); panel.add(title);
        panel.add(new JLabel("Author")); panel.add(author);
        panel.add(new JLabel("Category")); panel.add(cat);
        panel.add(new JLabel("Quantity")); panel.add(quantity);
        int res = JOptionPane.showConfirmDialog(this, panel, "Add Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            try {
                Book b = new Book(Integer.parseInt(id.getText()), title.getText(), author.getText(), cat.getSelectedItem().toString(), Integer.parseInt(quantity.getText()), false);
                manager.addBook(b);
                JOptionPane.showMessageDialog(this, "Book added successfully!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid input!" ); }
        }
    }

    private void showBooksTable() {
        JFrame frame = new JFrame("Books List");
        frame.setSize(700,400);
        frame.setLocationRelativeTo(this);
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID","Title","Author","Category","Qty","Issued"},0);
        for (Book b : manager.getAllBooks())
            model.addRow(new Object[]{b.id,b.title,b.author,b.category,b.quantity, b.isIssued});
        JTable table = new JTable(model);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(p);
        frame.setVisible(true);
    }

    private void showAddStudentDialog() {
        JPanel panel = new JPanel(new GridLayout(4,2));
        JTextField id = new JTextField(), name = new JTextField(), classField = new JTextField(), email = new JTextField();
        panel.add(new JLabel("Student ID")); panel.add(id);
        panel.add(new JLabel("Name")); panel.add(name);
        panel.add(new JLabel("Class")); panel.add(classField);
        panel.add(new JLabel("Email")); panel.add(email);
        int res = JOptionPane.showConfirmDialog(this, panel, "Add Student", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            try {
                Student s = new Student(Integer.parseInt(id.getText()), name.getText(), classField.getText(), email.getText());
                manager.addStudent(s);
                JOptionPane.showMessageDialog(this, "Student added successfully!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid input!" ); }
        }
    }

    private void showIssueBookDialog() {
        JPanel panel = new JPanel(new GridLayout(3,2));
        JTextField bookId = new JTextField(), studentId = new JTextField(), date = new JTextField();
        panel.add(new JLabel("Book ID")); panel.add(bookId);
        panel.add(new JLabel("Student ID")); panel.add(studentId);
        panel.add(new JLabel("Issue Date (YYYY-MM-DD)")); panel.add(date);
        int res = JOptionPane.showConfirmDialog(this, panel, "Issue Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            boolean ok = manager.issueBook(Integer.parseInt(bookId.getText()), Integer.parseInt(studentId.getText()), date.getText());
            JOptionPane.showMessageDialog(this, ok ? "Book issued!" : "Failed to issue book.");
        }
    }

    private void showReturnBookDialog() {
        JPanel panel = new JPanel(new GridLayout(2,2));
        JTextField bookId = new JTextField(), date = new JTextField();
        panel.add(new JLabel("Book ID")); panel.add(bookId);
        panel.add(new JLabel("Return Date (YYYY-MM-DD)")); panel.add(date);
        int res = JOptionPane.showConfirmDialog(this, panel, "Return Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            boolean ok = manager.returnBook(Integer.parseInt(bookId.getText()), date.getText());
            JOptionPane.showMessageDialog(this, ok ? "Book returned!" : "Failed to return book.");
        }
    }

    private void showDeleteBookDialog() {
        String inp = JOptionPane.showInputDialog(this, "Enter Book ID to delete:");
        if (inp != null) {
            boolean ok = manager.deleteBook(Integer.parseInt(inp));
            JOptionPane.showMessageDialog(this, ok ? "Book deleted!" : "Book not found.");
        }
    }

    private void showReportsDialog() {
        ArrayList<Book> books = manager.getAllBooks();
        int total = books.size(), issued=0;
        for (Book b : books) if(b.isIssued) issued++;
        int available = total-issued;
        JOptionPane.showMessageDialog(this, "Total Books: "+total+"\nIssued: "+issued+"\nAvailable: "+available, "Reports", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this, "Library Management System\nJava Swing + MySQL\nVersion 1.0", "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) { SwingUtilities.invokeLater(MainDashboard::new); }
}