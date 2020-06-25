package pc.practice2.locks;

/**
 * Interface to implement custom locks for mutual exclusion.
 * 
 * @author Francisco Javier Blázquez Martínez, Miguel Franqueira Varela
 */
public interface MyLock {

    public void lock(int pid);
    public void unlock(int pid);
}
