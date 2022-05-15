import java.util.Random;

public class Producer extends Thread {
    private final Buffer _buf;
    private final String name;

    public Producer (Buffer _buf, String name) {
        super(name);
        this._buf = _buf;
        this.name = name;
    }

    @Override
    public void run() {
        for(;;) {
            Random random = new Random();
            int i = random.nextInt();
            System.out.println(this.name + " producing value " + i);
            _buf.put(i);
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
