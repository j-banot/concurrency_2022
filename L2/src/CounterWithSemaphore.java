class Semaphore {
    private boolean _state;
    private int _wait;

    public Semaphore() {
        this._state = true;
        this._wait = 0;
    }

    public synchronized void take() throws InterruptedException {
        _wait++;
        while(!_state) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        _wait--;
        _state = false;
    }

    public synchronized void release() {
        if(_wait > 0) {
            this.notify();
        }
        _state = true;
    }
}

class Counter2 {
    private int _val;
    private Semaphore semaphore;
    public Counter2(int n, Semaphore semaphore) {
        this.semaphore = semaphore;
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

// Watek, ktory inkrementuje licznik 100 000 razy
class IThread2 extends Thread {
    private final Counter2 counter2;
    private final Semaphore semaphore;

    public IThread2(Counter2 cnt, Semaphore semaphore) {
        this.counter2 = cnt;
        this.semaphore = semaphore;
    }
    public void run() {
        for(int i = 0; i < 100000; i++) {
            try {
                semaphore.take();
                this.counter2.inc();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }
    }
}

// Watek, ktory dekrementuje licznik 100 000 razy
class DThread2 extends Thread {
    private final Counter2 counter2;
    private final Semaphore semaphore;

    public DThread2(Counter2 cnt, Semaphore semaphore) {
        this.counter2 = cnt;
        this.semaphore = semaphore;
    }
    public void run() {
        for(int i = 0; i < 100000; i++) {
            try {
                semaphore.take();
                this.counter2.dec();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }
    }
}

class Race2 {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            Semaphore semaphore = new Semaphore();
            Counter2 cnt = new Counter2(0, semaphore);
            Thread i1 = new IThread2(cnt, semaphore);
            Thread d2 = new DThread2(cnt, semaphore);

            i1.start();
            d2.start();

            System.out.println("value: " + cnt.value());
        }
    }
}