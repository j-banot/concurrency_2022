import java.util.Random;

public class Producer extends Thread {
    private final Buffer _buf;
    private final String name;
    Random random = new Random();

    public Producer (Buffer _buf, String name) {
        super(name);
        this._buf = _buf;
        this.name = name;
    }

    @Override
    public void run() {
        for(;;) {
            int halfBuffer = 250;
            int numberOfProducts = random.nextInt(halfBuffer) + 1;
            for (int i = 0; i < numberOfProducts; i++) {
                int n = random.nextInt();
                System.out.println(this.name + " producing value " + n);
                _buf.put(n);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
