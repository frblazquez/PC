package pc.practice3.schema;

public class Consumer implements Runnable {

    private int pid;
    private Warehouse wh;

    public Consumer(int pid, Warehouse wh) {
	this.pid = pid;
	this.wh = wh;
    }

    @Override
    public void run() {
	Product consumed;

	while(true) {
	    consumed = wh.extract();
	    System.out.println("Process " + pid + " consumed " + consumed.getValueToString());
	}
    }

}
