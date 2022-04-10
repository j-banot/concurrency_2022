import java.util.Random;
import java.util.concurrent.Semaphore;

class Producer extends Thread {
    private Buffer _buf;

    public void run() {
        for (int i = 0; i < 100; ++i) {
            _buf.put(i);
        }
    }
}

class Consumer extends Thread {
    private Buffer _buf;

    public void run() {
        for (int i = 0; i < 100; ++i) {
            System.out.println(_buf.get());
        }
    }
}

class Buffer {
    private final int[] array; // array storing queue elements
    private int front; // front points to the first element in the queue
    private int rear; // rear points to the last element in the queue
    private int count; // current size of the queue
    private final int capacity; // maximum capacity of the queue

    Buffer(int size) {
        array = new int[size];
        capacity = size;
        front = 0;
        rear = -1;
        count = 0;
    }

    public synchronized void put(int i) {
        while (!isEmpty()) {
            try {
                wait();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        rear = (rear + 1) % capacity;
        array[rear] = i;
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
        int result = array[front];
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

public class PKmon {
    public static void main(String[] args) {

    }
}