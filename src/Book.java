public class Book {
    public int bookId;
    public RedBlackTree.Color color;
    public Book left, right, parent;
    public String bookName;
    public String authorName;
    public boolean availabilityStatus;
    public int borrowedBy;
    public ReservationHeap reservationHeap;

    @Override
    public String toString() {
        return
                "BookID = " + bookId +
                        "\nTitle = \"" + bookName +
                        "\"\nAuthor = \"" + authorName +
                        "\"\nAvailability = \"" + (availabilityStatus?"Yes":"No") +
                        "\"\nBorrowedBy = " + (borrowedBy==-1?"None":borrowedBy) +
                        "\nReservations = [" + this.reservationHeap+"]\n";
    }

    public Book(int bookId, String bookName, String authorName, boolean availabilityStatus, int borrowedBy){
        this.bookId = bookId;
        this.left= EmptyBookNode.nullBook;
        this.right= EmptyBookNode.nullBook;
        this.parent= EmptyBookNode.nullBook;
        this.color= RedBlackTree.Color.BLACK;
        this.bookName=bookName;
        this.authorName=authorName;
        this.availabilityStatus=availabilityStatus;
        this.borrowedBy=borrowedBy;
        this.reservationHeap=new ReservationHeap();
    }

    public Book(int bookId) {
        this.bookId =bookId;
        this.color = RedBlackTree.Color.BLACK;
    }
}