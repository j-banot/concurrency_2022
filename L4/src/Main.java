import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ReadersWriters program;
        // options: semaphore, condition
        String mode = "semaphore";

        switch (mode) {
            case "semaphore":
                program = new ReadersWritersSemaphore();
                break;
            case "condition":
                program = new ReadersWritersSCondition();
                break;
            default:
                System.out.println("Error! Wrong 'mode' parameter");
                return;
        }

        int reader_repeat = 10;
        int writer_repeat = 10;
        int reader_sleep = 100;
        int writer_sleep = 100;

        for (int i = 1; i < 10; i++) {
            for (int j = 10; j <= 100; j+= 10) {
                int readers_all = j;
                int writers_all = i;

                long start = System.currentTimeMillis();
                program.execute(
                       readers_all,
                       writers_all,
                       reader_repeat,
                       writer_repeat,
                       reader_sleep,
                       writer_sleep
                );
                long stop = System.currentTimeMillis();
            }
        }
    }
}

abstract class Reader implements Runnable {
    private int repeat;
    private int sleep;

    Reader(int repeat, int sleep) {
        this.repeat = repeat;
        this.sleep = sleep;
    }

    @Override
    public void run() {
        for (int i = 0; i < repeat; i++) {
            try {
                before();
                read();
                after();
            } catch (InterruptedException e) {
                System. err.println(e.getMessage());
            }
        }
    }

    void read() throws InterruptedException {
        Thread.sleep(sleep);
    }

    abstract void before() throws InterruptedException;
    abstract void after() throws InterruptedException;
}

abstract class Writer implements Runnable {
    private int repeat;
    private int sleep;

    Writer(int repeat, int sleep) {
        this.repeat = repeat;
        this.sleep = sleep;
    }

    @Override
    public void run() {
        for (int i = 0; i < repeat; i++) {
            try {
                before();
                write();
                after();
            } catch (InterruptedException e) {
                System. err.println(e.getMessage());
            }
        }
    }

    void write() throws InterruptedException {
        Thread.sleep(sleep);
    }

    abstract void before() throws InterruptedException;
    abstract void after() throws InterruptedException;
}

abstract class ReadersWriters {
    public void execute(int readers_all, int writers_all, int reader_repeat, int writer_repeat, int reader_sleep, int writer_sleep) {
        List<Thread> threads = new ArrayList<>();

        for (int i = 1; i <= readers_all; i++) {
            Thread t = new Thread(create_reader(reader_repeat, reader_sleep));
            threads.add(t);
        }

        for (int i = 1; i <= writers_all; i++) {
            Thread t = new Thread(create_writer(writer_repeat, writer_sleep));
            threads.add(t);
        }

        for (Thread t : threads) {
            t.start();
        }
    }

    abstract Reader create_reader(int repeat, int sleep);
    abstract Writer create_writer(int repeat, int sleep);
}


