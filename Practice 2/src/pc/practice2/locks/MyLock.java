package pc.practice2.locks;

public interface MyLock {

    public void lock(int pid);
    public void unlock(int pid);
}
