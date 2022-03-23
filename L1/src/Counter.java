import java.util.ArrayList;
import java.util.List;

public class Counter {
    private int _val;
    public Counter(int n) {
        _val = n;
    }
    public void inc() {
        _val++;
    }
    public void dec() {
        _val--;
    }
    public int value() {
        return _val;
    }
}

// Watek, ktory inkrementuje licznik 10 000 razy
class IThread extends Thread {
    private final Counter counter;
    public IThread(Counter cnt) {
        this.counter = cnt;
    }
    public void run() {
        for(int i = 0; i < 10000; i++) {
            this.counter.inc();
        }
    }
}

// Watek, ktory dekrementuje licznik 10 000 razy
class DThread extends Thread {
    private final Counter counter;
    public DThread(Counter cnt) {
        this.counter = cnt;
    }
    public void run() {
        for(int i = 0; i < 10000; i++) {
            this.counter.dec();
        }
    }
}

class Race {
    public static void main(String[] args) {
        List<Integer> histogram = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Counter cnt = new Counter(0);
            IThread iThread = new IThread(cnt);
            DThread dThread = new DThread(cnt);

            iThread.start();
            dThread.start();

            try {
                iThread.join();
                dThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.print("Thread interrupted");
            }

            System.out.println("stan= " + cnt.value());
            histogram.add(cnt.value());
        }
        System.out.println(histogram.toString());
    }
}