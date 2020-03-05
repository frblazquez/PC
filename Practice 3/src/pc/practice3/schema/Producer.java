package pc.practice3.schema;

public class Producer implements Runnable {

    private int pid;
    private Warehouse wh;

    public Producer(int pid, Warehouse wh) {
	this.pid = pid;
	this.wh = wh;
    }

    @Override
    public void run() {
	Product produced;

	while(true) {
	    produced = new IntProduct(pid);
	    wh.store(produced);
	    System.out.println("Process " + pid + " produced");
	}
    }

}
