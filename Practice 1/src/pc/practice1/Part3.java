package pc.practice1;

/**
 * Practice 1 third part, square matrices multiplication with several threads.
 * 
 * @author Francisco Javier Blázquez Martínez
 */
public class Part3 {

    private static final int ORDER = 3;
    private static final int[][] A = {{1,2,3},{3,4,5},{4,5,6}}; // new int[ORDER][ORDER];
    private static final int[][] B = {{0,1,1},{1,2,1},{1,1,0}};
    private static int[][] C = new int[ORDER][ORDER];

    public static void main(String[] args) {

	Thread[] multipliers = new Thread[ORDER];

	for (int i = 0; i < ORDER; i++)
	    multipliers[i] = new Thread(new Part3().new Multiplier(i));

	for (int i = 0; i < ORDER; i++)
	    multipliers[i].start();

	try {
	    for (int i = 0; i < ORDER; i++)
		    multipliers[i].join();
	} catch (InterruptedException e) {
        // Interruptions are not considered
	    e.printStackTrace();
	}

	for (int i = 0; i < ORDER; i++) {
	    for (int j = 0; j < ORDER; j++) {
		    System.out.print(C[i][j] + " ");
	    }
	    System.out.println();
	}
    }

    /**
     * Gets a row of the A*B product. Row index is given in the constructor.
     */
    class Multiplier implements Runnable {

	int rowIndex;

	public Multiplier(int rowIndex) {
	    this.rowIndex = rowIndex;
	}

	@Override
	public void run() {
	    for (int i = 0; i < ORDER; i++) {
		int sum = 0;

		for (int k = 0; k < ORDER; k++)
		    sum += A[rowIndex][k] * B[k][i];

		C[rowIndex][i] = sum;
	    }
	}
    }
}

