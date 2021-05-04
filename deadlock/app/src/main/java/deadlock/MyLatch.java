package deadlock;

public class MyLatch {
    private int countDown;

    public MyLatch(int countDown) {
        this.countDown = countDown;
    }

    synchronized public void await() throws InterruptedException {
        while(countDown!=0){
            this.wait();
        }
    }

    synchronized public void dec(){
        countDown--;
        this.notifyAll();
    }
}
