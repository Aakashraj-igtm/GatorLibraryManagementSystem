public class ReservationHeap {
    // Set a fixed capacity for the heap. Given 20 as per the problem statement
    public static final int CAPACITY = 20;

    // Array to store heap elements.
    public HeapNode[] heap;

    // Current number of elements in the heap.
    public int size;

    // Constructor initializes the heap array and size.
    public ReservationHeap() {
        this.heap = new HeapNode[CAPACITY];
        this.size = 0;
    }

    // Check if the heap is empty.
    public boolean isEmpty() {
        return size == 0;
    }

    // Return the current size of the heap.
    public int size() {
        return size;
    }

    // Calculate the index of the left child of a given node.
    private int getLeftChildIdx(int parentIdx) {
        return 2 * parentIdx + 1;
    }

    // Calculate the index of the right child of a given node.
    private int getRightChildIdx(int parentIdx) {
        return 2 * parentIdx + 2;
    }

    // Calculate the index of the parent of a given node.
    private int getParentIdx(int childIdx) {
        return (childIdx - 1) / 2;
    }

    // Retrieve the left child node of a given node.
    private HeapNode leftChild(int parentIdx) {
        return heap[getLeftChildIdx(parentIdx)];
    }

    // Retrieve the right child node of a given node.
    private HeapNode rightChild(int parentIdx) {
        return heap[getRightChildIdx(parentIdx)];
    }

    // Retrieve the parent node of a given node.
    private HeapNode parent(int childIdx) {
        return heap[getParentIdx(childIdx)];
    }

    // Retrieve the top (minimum) element of the heap.
    public HeapNode peek() {
        if (isEmpty()) {
            System.out.println("MinHeap empty, invalid peek()");
            return null;
        }
        return heap[0];
    }

    // Remove and return the top (minimum) element of the heap.
    public HeapNode poll() {
        if (isEmpty()) {
            System.out.println("MinHeap empty, invalid poll()");
            return null;
        }
        HeapNode minNode = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapifyDown();
        return minNode;
    }

    // Add a new element to the heap.
    public void addToHeap(HeapNode reservation) {
        if (size == CAPACITY) {
            System.out.println("Heap is full, cannot add more elements.");
            return;
        }
        heap[size] = reservation;
        size++;
        heapifyUp();
    }

    // Adjust the heap upwards after adding a new element.
    private void heapifyUp() {
        int idx = size - 1;
        while (getParentIdx(idx) >= 0 && heap[idx].compareTo(parent(idx)) < 0) {
            swap(getParentIdx(idx), idx);
            idx = getParentIdx(idx);
        }
    }

    // Adjust the heap downwards after removing the top element.
    private void heapifyDown() {
        int idx = 0;
        while (getLeftChildIdx(idx) < size) {
            int smallestChildIdx = getLeftChildIdx(idx);

            if (getRightChildIdx(idx) < size && rightChild(idx).compareTo(leftChild(idx)) < 0) {
                smallestChildIdx = getRightChildIdx(idx);
            }

            if (heap[idx].compareTo(heap[smallestChildIdx]) <= 0) {
                break;
            } else {
                swap(idx, smallestChildIdx);
            }
            idx = smallestChildIdx;
        }
    }

    // Swap two elements in the heap.
    private void swap(int x, int y) {
        HeapNode temp = heap[x];
        heap[x] = heap[y];
        heap[y] = temp;
    }

    // Generate a string representation of the heap's elements.
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < size; i++) {
            res.append(heap[i].getPatronId());
            if (i < size - 1) {
                res.append(",");
            }
        }
        return String.valueOf(res);
    }
}
