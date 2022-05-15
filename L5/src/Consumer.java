public class Consumer extends Thread {
    private final Buffer _buf;
    private final String name;

    public Consumer (Buffer _buf, String name) {
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
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
