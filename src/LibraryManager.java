import java.sql.*;
import java.util.*;

public class LibraryManager {
    private Connection conn;

    public LibraryManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/SLibraryDB",
                "root", "r00t123"
            );
            System.out.println("Database connected successfully!");
        } catch(Exception e) { 
            System.out.println("DB Connection Failed: " + e.getMessage());
            System.out.println("App will run without database...");
            conn = null;
        }
    }

    public void addBook(Book b) {
        if (conn == null) return;
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO books VALUES (?,?,?,?,?,?)");
            ps.setInt(1, b.id);
            ps.setString(2, b.title);
            ps.setString(3, b.author);
            ps.setString(4, b.category);
            ps.setInt(5, b.quantity);
            ps.setBoolean(6, b.isIssued);
            ps.executeUpdate();
        } catch(Exception e) { System.out.println("Error adding:" + e.getMessage()); }
    }

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> out = new ArrayList<>();
        if (conn == null) return out;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM books");
            while(rs.next()) {
                out.add(new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("category"),
                    rs.getInt("quantity"),
                    rs.getBoolean("isIssued")
                ));
            }
        } catch(Exception e) { System.out.println("Fetch error:" + e.getMessage()); }
        return out;
    }

    public boolean deleteBook(int bookId) {
        if (conn == null) return false;
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE id=?");
            ps.setInt(1, bookId);
            return ps.executeUpdate() > 0;
        } catch(Exception e) { System.out.println("Delete error:" + e.getMessage()); }
        return false;
    }

    public void addStudent(Student s) {
        if (conn == null) return;
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO students VALUES (?,?,?,?)");
            ps.setInt(1, s.id);
            ps.setString(2, s.name);
            ps.setString(3, s.stdClass);
            ps.setString(4, s.email);
            ps.executeUpdate();
        } catch(Exception e) { System.out.println("Student add error:" + e.getMessage()); }
    }

    public boolean issueBook(int bookId, int studentId, String dt) {
        if (conn == null) return false;
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO issues(book_id,student_id,issue_date) VALUES (?,?,?)");
            ps.setInt(1, bookId); ps.setInt(2, studentId); ps.setString(3, dt);
            int out = ps.executeUpdate();
            if(out > 0) {
                PreparedStatement upd = conn.prepareStatement("UPDATE books SET isIssued=TRUE,quantity=quantity-1 WHERE id=? AND quantity>0");
                upd.setInt(1, bookId); upd.executeUpdate();
                return true;
            }
        } catch(Exception e) { System.out.println("Issue error:" + e.getMessage()); }
        return false;
    }

    public boolean returnBook(int bookId, String dt) {
        if (conn == null) return false;
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE issues SET return_date=? WHERE book_id=? AND return_date IS NULL");
            ps.setString(1, dt); ps.setInt(2, bookId);
            int out = ps.executeUpdate();
            if(out > 0) {
                PreparedStatement upd = conn.prepareStatement("UPDATE books SET isIssued=FALSE,quantity=quantity+1 WHERE id=?");
                upd.setInt(1, bookId); upd.executeUpdate();
                return true;
            }
        } catch(Exception e) { System.out.println("Return error:" + e.getMessage()); }
        return false;
    }
}