import java.util.*;

class RedBlackTree {

    // StringBuilder to append all the outputs and finally write to a outputFile
    public static StringBuilder sb = new StringBuilder();

    // HashMap to store initial color values before performing insert,delete,rotate operations
    public static Map<Integer, Color> hm1 = new HashMap<>();

    //HashMap to store final color values before performing insert,delete,rotate operations
    public static Map<Integer, Color> hm2 = new HashMap<>();
    static
    // ENUM to store color of the node
    public enum Color {RED, BLACK}
    private final Book nullBook = EmptyBookNode.nullBook;
    public int flipCount;

    private Book root;


    // Comparator class to sort by bookId
    public RedBlackTree() {
        this.root = nullBook;
        this.flipCount=0;
    }



    // insert a book into RB Tree
    public void insertBook(int bookId,String bookName,String authorName,String availabilityStatus,int borrowedBy){
        Book book = new Book(bookId,bookName,authorName,availabilityStatus.equals("Yes")?true:false,borrowedBy);
        initializeHashMaps();
        if(root == nullBook){
            hm1.put(bookId, Color.BLACK);
        }
        else{
            hm1.put(bookId,Color.RED);
        }
        insert(book);
        populateHm2();
        calculateFlipCounts();
    }

    // calculateFlip Counts in RB Tree
    private void calculateFlipCounts(){
        for(Map.Entry<Integer, Color> entry: hm1.entrySet()){
            if(entry.getValue() != hm2.get(entry.getKey())){
                this.flipCount++;
            }
        }
    }


    private void populateHm2(){
        inorderTraversal(root);
    }

    // Inorder Traversal into RB Tree
    private void inorderTraversal(Book root){
        if(root == nullBook || root == null){
            return;
        }
        inorderTraversal(root.left);
        hm2.put(root.bookId, root.color);
        inorderTraversal(root.right);
    }

    // Initialize hashmaps used for flipCount
    private void initializeHashMaps(){
        hm1.clear();
        hm1= new HashMap<>(hm2);
        hm2.clear();
    }


    // insert Book into RB Tree
    private void insert(Book book) {

        Book temp = root;
        if (root == nullBook) {
            root = book;
            book.color = Color.BLACK;
            book.parent = nullBook;
        } else {
            book.color = Color.RED;
            while (true) {
                if (book.bookId < temp.bookId) {
                    if (temp.left == nullBook) {
                        temp.left = book;
                        book.parent = temp;
                        break;
                    } else {
                        temp = temp.left;
                    }
                } else if (book.bookId == temp.bookId) {
                    return;
                } else {
                    if (temp.right == nullBook) {
                        temp.right = book;
                        book.parent = temp;
                        break;
                    } else {
                        temp = temp.right;
                    }
                }
            }
            fixupInsert(book);
        }
    }

    // Fix Imbalances into RB Tree after insert operation
    private void fixupInsert(Book book) {
        while (book.parent.color == Color.RED) {
            Book uncle = nullBook;
            if (book.parent == book.parent.parent.left) {
                uncle = book.parent.parent.right;

                if (uncle != nullBook && uncle.color == Color.RED) {
                    book.parent.color = Color.BLACK;
                    if(book.parent.parent.color != Color.RED && book.parent.parent != root) {
                        book.parent.parent.color = Color.RED;
                    }
                    uncle.color = Color.BLACK;
                    book = book.parent.parent;
                    continue;
                }
                if (book == book.parent.right) {
                    book = book.parent;
                    leftRotate(book);
                }
                book.parent.color = Color.BLACK;
                book.parent.parent.color = Color.RED;
                rightRotate(book.parent.parent);
            } else {
                uncle = book.parent.parent.left;
                if (uncle != nullBook && uncle.color == Color.RED) {
                    book.parent.color = Color.BLACK;
                    book.parent.parent.color = Color.RED;
                    uncle.color = Color.BLACK;
                    book = book.parent.parent;
                    continue;
                }
                if (book == book.parent.left) {
                    book = book.parent;
                    rightRotate(book);
                }
                book.parent.color = Color.BLACK;
                book.parent.parent.color = Color.RED;
                leftRotate(book.parent.parent);
            }
        }
        root.color = Color.BLACK;
    }

    // Perform Left Rotation in RB Tree
    private void leftRotate(Book book) {
        if (book.parent != nullBook) {
            if (book == book.parent.left) {
                book.parent.left = book.right;
            } else {
                book.parent.right = book.right;
            }
            book.right.parent = book.parent;
            book.parent = book.right;
            if (book.right.left != nullBook) {
                book.right.left.parent = book;
            }
            book.right = book.right.left;
            book.parent.left = book;
        } else {
            Book right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nullBook;
            root = right;
        }
    }
    // Perform Right Rotation in RB Tree
    private void rightRotate(Book book) {
        if (book.parent != nullBook) {
            if (book == book.parent.left) {
                book.parent.left = book.left;
            } else {
                book.parent.right = book.left;
            }

            book.left.parent = book.parent;
            book.parent = book.left;
            if (book.left.right != nullBook) {
                book.left.right.parent = book;
            }
            book.left = book.left.right;
            book.parent.right = book;
        } else {
            Book left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nullBook;
            root = left;
        }
    }

    // PrintBook based on bookId
    public Book printBook(int bookId) {
        Book temp = root;
        if(root.bookId == -1)
            return null;
        while (true) {
            if (bookId < temp.bookId) {
                if (temp.left == nullBook) {
                    return null;
                } else {
                    temp = temp.left;
                }
            }else if (bookId == temp.bookId) {
                return temp;
            } else {
                if (temp.right == nullBook) {
                    return null;
                } else {
                    temp = temp.right;
                }
            }
        }
    }

    // Delete a book from RB Tree
    public void deleteBook(int bookId) {
        Book delBook = printBook(bookId);
        if(delBook == null) {
            sb.append("\nBook "+bookId+" is no longer available."+"\n");
            return;
        }
        initializeHashMaps();
        hm1.remove(bookId);
        delete(delBook);
        populateHm2();
        calculateFlipCounts();
        if(delBook.reservationHeap.isEmpty()){
            sb.append("\nBook "+bookId+" is no longer available."+"\n");
        } else{
        sb.append("\nBook "+bookId+" is no longer available. Reservations made by Patrons "+ delBook.reservationHeap+" have been cancelled!" +"\n");
        }
    }


    // Delete a book from RB Tree

    private boolean delete(Book z){
        Book y = z;
        Color y_original_color = y.color;
        Book x;
        if(z.left == nullBook){
            x = z.right;
            transplant(z, z.right);
        }else if(z.right == nullBook){
            x = z.left;
            transplant(z, z.left);
        }else{
            y = treeMaximum(z.left);
            y_original_color = y.color;
            x = y.left;
            if(y.parent == z)
                x.parent = y;
            else{
                transplant(y, y.left);
                y.left = z.left;
                y.left.parent = y;
            }
            transplant(z, y);
            y.right = z.right;
            y.right.parent = y;
            y.color = z.color;
        }
        if(y_original_color==Color.BLACK){
            fixupDelete(x);
        }
        return true;
    }

    // Transplant the Node
    private void transplant(Book u, Book v){
        if(u.parent == nullBook){
            root = v;
        }else if(u == u.parent.left){
            u.parent.left = v;
        }else
            u.parent.right = v;
        v.parent = u.parent;
    }

    // Find Maximum element from left Subtree
    private Book treeMaximum(Book z){
        while(z.right != nullBook){
            z= z.right;
        }
        return z;
    }

    // Fix Imbalances into RB Tree after delete operation
    private void fixupDelete(Book x){
        while(x!=root && x.color == Color.BLACK){
            if(x == x.parent.left){
                Book w = x.parent.right;
                if(w.color == Color.RED){
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == Color.BLACK && w.right.color == Color.BLACK){
                    w.color = Color.RED;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == Color.BLACK){
                    w.left.color = Color.BLACK;
                    w.color = Color.RED;
                    rightRotate(w);
                    w = x.parent.right;
                }
                if(w.right.color == Color.RED){
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.right.color = Color.BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            }else{
                Book w = x.parent.left;
                if(w.color == Color.RED){
                    w.color = Color.BLACK;
                    x.parent.color = Color.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == Color.BLACK && w.left.color == Color.BLACK){
                    w.color = Color.RED;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == Color.BLACK){
                    w.right.color = Color.BLACK;
                    w.color = Color.RED;
                    leftRotate(w);
                    w = x.parent.left;
                }
                if(w.left.color == Color.RED){
                    w.color = x.parent.color;
                    x.parent.color = Color.BLACK;
                    w.left.color = Color.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = Color.BLACK;
    }



    // printBooks based on bookId1 and bookId2
    public void printBooks(int bookId1,int bookId2){
        List<Book> listOfBooks=new ArrayList<>();
        inorder(this.root,bookId1,bookId2,listOfBooks,true);
        for(Book book: listOfBooks)
            sb.append(book+"\n");
    }


    // findClosestBook function based on bookId
    public void findClosestBook(int targetId){
        int minDiff=Integer.MAX_VALUE;
        List<Book> listOfBooks=new ArrayList<>();
        inorder(this.root,-1,-1,listOfBooks,false);
        List<Book> res=new ArrayList<>();
        for(Book book:listOfBooks){
            int diff=Math.abs(targetId-book.bookId);
            if(minDiff>diff){
                minDiff=diff;
                res=new ArrayList<>();
                res.add(book);
            }else if(minDiff==diff)
                res.add(book);
        }
        res.sort(new sortByBookId());
        for(Book book:res) {
           // System .out.println(book);
            sb.append(book + "\n");
        }
    }

    public class sortByBookId implements Comparator<Book>{
        public int compare(Book a, Book b){
            return a.bookId - b.bookId;
        }
    }


    // Inorder Traversal for printBooks
    private void inorder(Book book, int lower, int upper, List<Book> listOfBooks, boolean flag){
        if(book== nullBook)
            return;
        inorder(book.left,lower,upper,listOfBooks,flag);
        if(flag) {
            if (book.bookId >= lower && book.bookId <= upper)
                listOfBooks.add(book);
        }
        else {
            listOfBooks.add(book);
        }
        inorder(book.right,lower,upper,listOfBooks,flag);
    }

    // Print the colorFlipCount
    public void colorFlipCount(){
        sb.append("\nColor Flip Count : "+this.flipCount +"\n");
    }

    // Borrow a book from GatorLibrary
    public void borrowBook(int patronId,int bookId,int patronPriority){
        Book book=printBook(bookId);
        if(book==null)
            return;
        if(book.availabilityStatus){
            book.borrowedBy=patronId;
            book.availabilityStatus=false;
            sb.append("\nBook "+bookId+" Borrowed by Patron "+patronId +"\n");
        }  else if (alreadyReservedByPatron(patronId, book)) {
            sb.append("Book " + bookId + " Already Reserved by Patron " + patronId + "\n");
        } else {
            sb.append("\nBook " + bookId + " Reserved by Patron " + patronId+"\n");
            book.reservationHeap.addToHeap(new HeapNode(patronId, patronPriority, new Date()));
        }
    }

    public boolean alreadyReservedByPatron(int patronId, Book book) {
        for (HeapNode reservation : book.reservationHeap.heap) {
            if (reservation != null && reservation.getPatronId() == patronId) {
                return true;
            }
        }
        return false;
    }

    // Return a book to the GatorLibrary
    public void returnBook(int patronId,int bookId){
        Book book=printBook(bookId);
        if(book==null)
            return;
        if(book.borrowedBy!=patronId)
            return;
        if(book.availabilityStatus)
            return;
        book.borrowedBy=-1;
        book.availabilityStatus=true;
        sb.append("Book "+bookId+" Returned by Patron "+patronId+"\n");
        if(!book.reservationHeap.isEmpty()){
            int reservedPatronId=book.reservationHeap.poll().patronId;
            if(reservedPatronId==-1)
                return;
            book.borrowedBy=reservedPatronId;
            book.availabilityStatus=false;
            sb.append("\nBook "+bookId+" Allotted to Patron "+reservedPatronId+"\n");
        }
    }

    // Terminate the program
    public void quit(){
        sb.append("\nProgram Terminated!!" +"\n");
        this.root=null;
    }

}