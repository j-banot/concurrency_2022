import java.util.Random;

public class Consumer extends Thread {
    private final Buffer _buf;
    private final String name;
    Random random = new Random();

    public Consumer (Buffer _buf, String name) {
        super(name);
        this._buf = _buf;
        this.name = name;
    }

    @Override
    public void run() {
        for (; ; ) {
            int halfBuffer = 250;
            int numberOdProducts = random.nextInt(halfBuffer) + 1;
            for (int i = 0; i < numberOdProducts; i++) {
                System.out.println(this.name + ": consuming value " + _buf.get());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
