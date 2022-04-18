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
            try {
                System.out.println(this.name + ": consuming value " + _buf.get());
                Thread.sleep(200);
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
            int i = random.nextInt() % 1000;
            System.out.println(this.name + ": producing value " + i);
            try {
                _buf.put(i);
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
    private final Semaphore _semProducer, _semConsumer;

    Buffer(int size, Semaphore semProducer, Semaphore semConsumer) {
        array = new int[size];
        capacity = size;
        front = 0;
        rear = -1;
        count = 0;
        _semProducer = semProducer;
        _semConsumer = semConsumer;
    }

    public synchronized void put(int i) throws InterruptedException{
        _semProducer.acquire();
        rear = (rear + 1) % capacity;
        array[rear] = i;
        count++;
        _semConsumer.release();
    }

    public synchronized int get() throws InterruptedException{
        _semConsumer.acquire();
        int result = array[front];
        front = (front + 1) % capacity;
        count--;
        _semProducer.release();
        return result;
    }

    public int size() {
        return count;
    }
}

public class PKmon {
    public static void main(String[] args) {
        Semaphore semCon = new Semaphore(0);
        Semaphore semProd = new Semaphore(1);

        Buffer buffer = new Buffer(500, semCon, semProd);

        Thread producer1 = new Producer(buffer, "Producer1");
        Thread consumer1 = new Consumer(buffer, "Consumer1");

        producer1.start();
        consumer1.start();
    }
}