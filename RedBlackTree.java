import java.util.*;

class RedBlackTree {
    public enum Color {RED, BLACK}
    private final Book nullBook = new Book(-1);
    public int flipCount;
    public class Book {
        public int bookId;
        public Color color;
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
                            "\nReservations = [" + reservationHeap+"]\n";
        }

        public Book(int bookId, String bookName, String authorName, boolean availabilityStatus, int borrowedBy, ReservationHeap reservationHeap){
            this.bookId = bookId;
            this.left= nullBook;
            this.right= nullBook;
            this.parent= nullBook;
            this.color=Color.BLACK;
            this.bookName=bookName;
            this.authorName=authorName;
            this.availabilityStatus=availabilityStatus;
            this.borrowedBy=borrowedBy;
            this.reservationHeap=reservationHeap;
        }

        public Book(int bookId) {
            this.bookId =bookId;
        }


    }
    public static class ReservationHeap{

        public BinaryHeap<HeapNode> heap;

        public ReservationHeap(){
            heap=new BinaryHeap<HeapNode>((x, y) -> {
                if(x.priorityNumber==y.priorityNumber)
                    return x.timeOfReservation.compareTo(y.timeOfReservation);
                return x.priorityNumber-y.priorityNumber;
            });
        }

        public void addToHeap(int patronId,int priorityNumber){
            HeapNode heapNode=new HeapNode(patronId,priorityNumber,new Date());
            this.heap.offer(heapNode);
        }

        public int getTop(){
            if(this.isEmpty())
                return -1;
            return this.heap.peek().patronId;
        }

        public int deleteFromHeap(){
            if(!this.isEmpty())
                return this.heap.poll().patronId;
            return -1;
        }

        public boolean isEmpty(){
            return this.heap.size()==0;
        }

        @Override
        public String toString(){
            String res="";
            List<HeapNode> tempList=new ArrayList<>();
            while(this.heap.size()>1){
                HeapNode heapNode=this.heap.poll();
                tempList.add(heapNode);
                res+=heapNode.patronId+",";
            }
            if(!this.heap.isEmpty()) {
                HeapNode heapNode = this.heap.poll();
                tempList.add(heapNode);
                res+=heapNode.patronId;
            }
            for(HeapNode heapNode:tempList)
                this.heap.offer(heapNode);
            return res;
        }
        private class HeapNode{
            public int patronId;
            public int priorityNumber;
            public Date timeOfReservation;

            public HeapNode(int patronId, int priorityNumber, Date timeOfReservation) {
                this.patronId = patronId;
                this.priorityNumber = priorityNumber;
                this.timeOfReservation = timeOfReservation;
            }
        }

        public class BinaryHeap<E> {

            private static final int DEFAULT_INITIAL_CAPACITY = 11;
            transient Object[] queue;
            private int size = 0;
            private final Comparator<? super E> comparator;
            transient int modCount = 0;
            public BinaryHeap(Comparator<? super E> comparator) {
                this(DEFAULT_INITIAL_CAPACITY, comparator);
            }
            public BinaryHeap(int initialCapacity,
                              Comparator<? super E> comparator) {
                if (initialCapacity < 1)
                    throw new IllegalArgumentException();
                this.queue = new Object[initialCapacity];
                this.comparator = comparator;
            }
            private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
            private void grow(int minCapacity) {
                int oldCapacity = queue.length;
                int newCapacity = oldCapacity + ((oldCapacity < 64) ?
                        (oldCapacity + 2) :
                        (oldCapacity >> 1));
                if (newCapacity - MAX_ARRAY_SIZE > 0)
                    newCapacity = hugeCapacity(minCapacity);
                queue = Arrays.copyOf(queue, newCapacity);
            }

            private int hugeCapacity(int minCapacity) {
                if (minCapacity < 0) // overflow
                    throw new OutOfMemoryError();
                return (minCapacity > MAX_ARRAY_SIZE) ?
                        Integer.MAX_VALUE :
                        MAX_ARRAY_SIZE;
            }
            public boolean offer(E e) {
                if (e == null)
                    throw new NullPointerException();
                modCount++;
                int i = size;
                if (i >= queue.length)
                    grow(i + 1);
                size = i + 1;
                if (i == 0)
                    queue[0] = e;
                else
                    siftUp(i, e);
                return true;
            }
            @SuppressWarnings("unchecked")
            public E peek() {
                return (size == 0) ? null : (E) queue[0];
            }
            @SuppressWarnings("unchecked")
            public E poll() {
                if (size == 0)
                    return null;
                int s = --size;
                modCount++;
                E result = (E) queue[0];
                E x = (E) queue[s];
                queue[s] = null;
                if (s != 0)
                    siftDown(0, x);
                return result;
            }
            private void siftUp(int k, E x) {
                if (comparator != null)
                    siftUpUsingComparator(k, x);
                else
                    siftUpComparable(k, x);
            }

            @SuppressWarnings("unchecked")
            private void siftUpComparable(int k, E x) {
                Comparable<? super E> key = (Comparable<? super E>) x;
                while (k > 0) {
                    int parent = (k - 1) >>> 1;
                    Object e = queue[parent];
                    if (key.compareTo((E) e) >= 0)
                        break;
                    queue[k] = e;
                    k = parent;
                }
                queue[k] = key;
            }

            @SuppressWarnings("unchecked")
            private void siftUpUsingComparator(int k, E x) {
                while (k > 0) {
                    int parent = (k - 1) >>> 1;
                    Object e = queue[parent];
                    if (comparator.compare(x, (E) e) >= 0)
                        break;
                    queue[k] = e;
                    k = parent;
                }
                queue[k] = x;
            }

            private void siftDown(int k, E x) {
                if (comparator != null)
                    siftDownUsingComparator(k, x);
                else
                    siftDownComparable(k, x);
            }
            @SuppressWarnings("unchecked")
            private void siftDownComparable(int k, E x) {
                Comparable<? super E> key = (Comparable<? super E>)x;
                int half = size >>> 1;
                while (k < half) {
                    int child = (k << 1) + 1;
                    Object c = queue[child];
                    int right = child + 1;
                    if (right < size &&
                            ((Comparable<? super E>) c).compareTo((E) queue[right]) > 0)
                        c = queue[child = right];
                    if (key.compareTo((E) c) <= 0)
                        break;
                    queue[k] = c;
                    k = child;
                }
                queue[k] = key;
            }
            @SuppressWarnings("unchecked")
            private void siftDownUsingComparator(int k, E x) {
                int half = size >>> 1;
                while (k < half) {
                    int child = (k << 1) + 1;
                    Object c = queue[child];
                    int right = child + 1;
                    if (right < size &&
                            comparator.compare((E) c, (E) queue[right]) > 0)
                        c = queue[child = right];
                    if (comparator.compare(x, (E) c) <= 0)
                        break;
                    queue[k] = c;
                    k = child;
                }
                queue[k] = x;
            }
            @SuppressWarnings("unchecked")
            private void heapify() {
                for (int i = (size >>> 1) - 1; i >= 0; i--)
                    siftDown(i, (E) queue[i]);
            }
            public int size() {
                return size;
            }
            public boolean isEmpty(){return size==0;}
        }
    }
    private Book root;
    public RedBlackTree() {
        this.root = nullBook;
        this.flipCount=0;
    }

    public void insertBook(int bookId,String bookName,String authorName,String availabilityStatus,int borrowedBy,ReservationHeap reservationHeap){
        Book book = new Book(bookId,bookName,authorName,availabilityStatus.equals("Yes")?true:false,borrowedBy,reservationHeap);
        insert(book);
    }

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
    private void fixupInsert(Book book) {
        while (book.parent.color == Color.RED) {
            Book uncle = nullBook;
            if (book.parent == book.parent.parent.left) {
                uncle = book.parent.parent.right;

                if (uncle != nullBook && uncle.color == Color.RED) {
                    if(book.parent.color != Color.BLACK)
                        this.flipCount++;
                    book.parent.color = Color.BLACK;
                    if(book.parent.parent.color != Color.RED)
                        this.flipCount++;
                    book.parent.parent.color = Color.RED;
                    if(uncle.color != Color.BLACK)
                        this.flipCount++;
                    uncle.color = Color.BLACK;
                    book = book.parent.parent;
                    continue;
                }
                if (book == book.parent.right) {
                    book = book.parent;
                    leftRotate(book);
                }
                if(book.parent.color != Color.BLACK)
                    this.flipCount++;
                book.parent.color = Color.BLACK;
                if(book.parent.parent.color != Color.RED)
                    this.flipCount++;
                book.parent.parent.color = Color.RED;
                rightRotate(book.parent.parent);
            } else {
                uncle = book.parent.parent.left;
                if (uncle != nullBook && uncle.color == Color.RED) {
                    if(book.parent.color != Color.BLACK)
                        this.flipCount++;
                    book.parent.color = Color.BLACK;
                    if(book.parent.parent.color != Color.RED)
                        this.flipCount++;
                    book.parent.parent.color = Color.RED;
                    if(uncle.color != Color.BLACK)
                        this.flipCount++;
                    uncle.color = Color.BLACK;
                    book = book.parent.parent;
                    continue;
                }
                if (book == book.parent.left) {
                    book = book.parent;
                    rightRotate(book);
                }
                if(book.parent.color != Color.BLACK)
                    this.flipCount++;
                book.parent.color = Color.BLACK;
                if(book.parent.parent.color != Color.RED)
                    this.flipCount++;
                book.parent.parent.color = Color.RED;
                leftRotate(book.parent.parent);
            }
        }
        if(root.color != Color.BLACK)
            this.flipCount++;
        root.color = Color.BLACK;
    }
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

    public Book printBook(int bookId) {
        Book temp = root;
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

    public void deleteBook(int bookId) {
        Book delBook = printBook(bookId);
        if(delBook == null) {
            System.out.println("Book "+bookId+" is no longer available.");
            return;
        }
        delete(delBook);
        if(delBook.reservationHeap.isEmpty()){
            System.out.println("Book "+bookId+" is no longer available.");
        } else{
        System.out.println("Book "+bookId+" is no longer available. Reservations made by Patrons "+ delBook.reservationHeap+" have been cancelled!");
        }
    }

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
            y = treeMinimum(z.right);
            y_original_color = y.color;
            x = y.right;
            if(y.parent == z)
                x.parent = y;
            else{
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if(y_original_color==Color.BLACK){
            fixupDelete(x);
        }
        return true;
    }

    private void transplant(Book u, Book v){
        if(u.parent == nullBook){
            root = v;
        }else if(u == u.parent.left){
            u.parent.left = v;
        }else
            u.parent.right = v;
        v.parent = u.parent;
    }

    private Book treeMinimum(Book z){
        while(z.left!= nullBook){
            z = z.left;
        }
        return z;
    }
    private void fixupDelete(Book x){
        while(x!=root && x.color == Color.BLACK){
            if(x == x.parent.left){
                Book w = x.parent.right;
                if(w.color == Color.RED){
                    if(w.color!=Color.BLACK)
                        this.flipCount++;
                    w.color = Color.BLACK;
                    if(x.parent.color != Color.RED)
                        this.flipCount++;
                    x.parent.color = Color.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == Color.BLACK && w.right.color == Color.BLACK){
                    if(w.color != Color.RED)
                        this.flipCount++;
                    w.color = Color.RED;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == Color.BLACK){
                    if(w.left.color != Color.BLACK)
                        this.flipCount++;
                    w.left.color = Color.BLACK;
                    if(w.color != Color.RED)
                        this.flipCount++;
                    w.color = Color.RED;
                    rightRotate(w);
                    w = x.parent.right;
                }
                if(w.right.color == Color.RED){
                    //doubt flip
//                    if(w.color != x.parent.color)
//                        this.flipCount++;
                    w.color = x.parent.color;
                    if(x.parent.color != Color.BLACK)
                        this.flipCount++;
                    x.parent.color = Color.BLACK;
                    if(w.right.color != Color.BLACK)
                        this.flipCount++;
                    w.right.color = Color.BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            }else{
                Book w = x.parent.left;
                if(w.color == Color.RED){
                    if(w.color != Color.BLACK)
                        this.flipCount++;
                    w.color = Color.BLACK;
                    if(x.parent.color != Color.RED)
                        this.flipCount++;
                    x.parent.color = Color.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == Color.BLACK && w.left.color == Color.BLACK){
                    if(w.color != Color.RED)
                        this.flipCount++;
                    w.color = Color.RED;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == Color.BLACK){
                    if(w.right.color != Color.BLACK)
                        this.flipCount++;
                    w.right.color = Color.BLACK;
                    if(w.color != Color.RED)
                        this.flipCount++;
                    w.color = Color.RED;
                    leftRotate(w);
                    w = x.parent.left;
                }
                if(w.left.color == Color.RED){
                    //doubt flip
//                    if(w.color!=x.parent.color)
//                        this.flipCount++;
                    w.color = x.parent.color;
                    if(x.parent.color!=Color.BLACK)
                        this.flipCount++;
                    x.parent.color = Color.BLACK;
                    if(w.left.color!=Color.BLACK)
                        this.flipCount++;
                    w.left.color = Color.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        if(x.color!=Color.BLACK)
            this.flipCount++;
        x.color = Color.BLACK;
    }

    public void printBooks(int bookId1,int bookId2){
        List<Book> listOfBooks=new ArrayList<>();
        inorder(this.root,bookId1,bookId2,listOfBooks,true);
        for(Book book: listOfBooks)
            System.out.println(book);
    }

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
        Collections.sort(res,(x, y) -> {
            return x.bookId -y.bookId;
        });
        for(Book book:res)
            System.out.println(book);
    }

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

    public void colorFlipCount(){
        System.out.println("Colour Flip Count : "+this.flipCount);
    }

    public void borrowBook(int patronId,int bookId,int patronPriority){
        Book book=printBook(bookId);
        if(book==null)
            return;
        if(book.availabilityStatus){
            book.borrowedBy=patronId;
            book.availabilityStatus=false;
            System.out.println("Book "+bookId+" Borrowed by Patron "+patronId);
        }else {
            System.out.println("Book " + bookId + " Reserved by Patron " + patronId);
            book.reservationHeap.addToHeap(patronId,patronPriority);
        }
    }

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
        System.out.println("Book "+bookId+" Returned by Patron "+patronId);
        if(!book.reservationHeap.isEmpty()){
            int reservedPatronId=book.reservationHeap.deleteFromHeap();
            if(reservedPatronId==-1)
                return;
            book.borrowedBy=reservedPatronId;
            book.availabilityStatus=false;
            System.out.println("\nBook "+bookId+" Allotted to Patron "+reservedPatronId);
        }
    }

    public void quit(){
        System.out.println("Program Terminated!!");
        this.root=null;
    }

}