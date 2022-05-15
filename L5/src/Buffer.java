public class Buffer {
    private final int[] arr; // array for storing queue elements
    private int front, rear, count;
    private final int capacity; // maximum capacity of the queue

    Buffer(int size) {
        arr = new int[size];
        capacity = size;
        front = 0;
        rear = -1;
        count = 0;
    }

    public synchronized void put(int i) {
        while(!isEmpty()) {
            try {
                wait();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        rear = (rear + 1) % capacity;
        arr[rear] = i;
        count++;
        notifyAll();
    }

    public synchronized int get() {
        while (isEmpty()) {
            try {
                wait();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        int result = arr[front];

        front = (front + 1) % capacity;
        count--;
        notifyAll();
        return result;
    }

    public int size() {
        return count;
    }

    public Boolean isEmpty() {
        return (size() == 0);
    }
}
