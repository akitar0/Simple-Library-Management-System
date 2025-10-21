public class Book {
    public int id;
    public String title, author, category;
    public int quantity;
    public boolean isIssued;

    public Book(int id, String title, String author, String category, int quantity, boolean isIssued) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.quantity = quantity;
        this.isIssued = isIssued;
    }
}