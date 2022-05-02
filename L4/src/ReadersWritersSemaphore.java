import java.util.concurrent.Semaphore;

public class ReadersWritersSemaphore extends ReadersWriters {
    private int read_count, write_count = 0;
    private Semaphore resource_access = new Semaphore(1);
    private Semaphore read_mutex = new Semaphore(1);
    private Semaphore write_mutex = new Semaphore(1);
    private Semaphore read_try = new Semaphore(1);

    @Override
    Reader create_reader(int repeat, int sleep) {
        return new ReaderSemaphore(repeat, sleep);
    }

    @Override
    Writer create_writer(int repeat, int sleep) {
        return new WriterSemaphore(repeat, sleep);
    }

    public class ReaderSemaphore extends Reader {
        ReaderSemaphore(int repeat, int sleep) {
            super(repeat, sleep);
        }

        void before() throws InterruptedException {
            read_try.acquire();
            read_mutex.acquire();
            read_count++;
            if (read_count == 1) {
                resource_access.acquire();
            }
            read_mutex.release();
            read_try.release();
        }

        void after() throws InterruptedException {
            read_mutex.acquire();
            read_count--;
            if (read_count == 0) {
                resource_access.release();
            }
            read_mutex.release();
        }
    }

    public class WriterSemaphore extends Writer {
        WriterSemaphore(int repeat, int sleep) {
            super(repeat, sleep);
        }

        void before() throws InterruptedException {
            write_mutex.acquire();
            write_count++;
            if (write_count == 1) {
                read_try.acquire();
            }
            write_mutex.release();
        }

        @Override
        void write() throws InterruptedException {
            resource_access.acquire();
            super.write();
            resource_access.release();
        }

        void after() throws InterruptedException {
            write_mutex.acquire();
            write_count--;
            if (write_count == 0) {
                read_try.release();
            }
            write_mutex.release();
        }
    }
}
