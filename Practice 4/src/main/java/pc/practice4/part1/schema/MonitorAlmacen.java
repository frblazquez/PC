package pc.practice4.part1.schema;

public class MonitorAlmacen {

    // Variables de control de estado
    private boolean produciendo;
    private int nConsumidoresConsumiendo;

    public MonitorAlmacen() {
	produciendo = false;
	nConsumidoresConsumiendo = 0;
    }

    public synchronized void request_produce() {
	// A diferencia del ejercicio anterior, donde nosotros controlábamos el mutex y
	// hacíamos paso de testigo, aquí si que tenemos que controlar si hay algún
	// productorproduciendo (segunda condición while)
	while(nConsumidoresConsumiendo > 0 || produciendo)
	    try { wait(); } 
	    catch (InterruptedException e) { e.printStackTrace(); }

	produciendo = true;
    }

    public synchronized void release_produce() {
	produciendo = false;

	// Los productores notifican una vez han terminado a consumidores y productores en espera.
	// (En el ejercicio anterior se daba preferencia a los productores en este punto)
	notifyAll();
    }

    public synchronized void request_consume() {
	while(produciendo)
	    try { wait(); } 
	    catch (InterruptedException e) { e.printStackTrace(); }

	nConsumidoresConsumiendo++;
    }

    public synchronized void release_consume() {
	nConsumidoresConsumiendo--;

	// Los consumidores no notifican mientras haya más consumidores
	if (nConsumidoresConsumiendo == 0)
	    notifyAll();
    }
}
