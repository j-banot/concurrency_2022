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
    public int multiplier;
    public IThread(Counter cnt, int mlt) {
        this.counter = cnt;
        this.multiplier = mlt;
    }
    public void run() {
        for(int i = 0; i < multiplier; i++) {
            this.counter.inc();
        }
    }
}

// Watek, ktory dekrementuje licznik 10 000 razy
class DThread extends Thread {
    private final Counter counter;
    public int multiplier;
    public DThread(Counter cnt, int mlt) {
        this.counter = cnt;
        this.multiplier = mlt;
    }
    public void run() {
        for(int i = 0; i < multiplier; i++) {
            this.counter.dec();
        }
    }
}

class Race {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> histogram = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Counter cnt = new Counter(0);
            int multiplier = 10000;
            IThread iThread = new IThread(cnt, multiplier);
            DThread dThread = new DThread(cnt, multiplier);

            iThread.start();
            while (cnt.value() != multiplier) {
                Thread.sleep(10);
            }

            dThread.start();
            while (cnt.value() != 0) {
                Thread.sleep(10);
            }
            System.out.println("stan= " + cnt.value());
            histogram.add(cnt.value());
        }
        System.out.println(histogram.toString());
    }
}