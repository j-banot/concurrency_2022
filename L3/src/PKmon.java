import javax.swing.*;
import java.util.Random;
import java.util.concurrent.Semaphore;

class Consumer extends Thread {
    private final Buffer _buf;
    private final String name;

    public Consumer(Buffer _buf, String name) {
        super(name);
        this._buf = _buf;
        this.name = name;
    }

    @Override
    public void run() {
        for (;;) {
            System.out.println(this.name + ": consuming value " + _buf.get());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }
}

class Producer extends Thread {
    private final Buffer _buf;
    private final String name;

    public Producer(Buffer _buf, String name) {
        super(name);
        this._buf = _buf;
        this.name = name;
    }

    @Override
    public void run() {
        for (;;) {
            Random random = new Random();
            int i = random.nextInt();
            System.out.println(this.name + ": producing value " + i);
            _buf.put(i);
            try {
                Thread.sleep(200);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
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