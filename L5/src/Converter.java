public class Converter extends Thread {
    private final Buffer previous;
    private final Buffer next;
    private final String name;

    public Converter (Buffer previous, Buffer next, String name) {
        this.previous = previous;
        this.next = next;
        this.name = name;
    }

    @Override
    public void run() {
        for (;;) {
            int tmp = previous.get();
            System.out.println(this.name + ": converting value " + tmp);

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
            next.put(tmp);
        }
    }
}
