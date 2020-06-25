package pc.practice4.part1.schema;

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
	    consumed = wh.extract(0);
	    System.out.println("Process " + pid + " consumed " + consumed.getValueToString());
	    try {
		Thread.sleep(10);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }

}
