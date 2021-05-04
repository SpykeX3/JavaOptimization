/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package deadlock;

public class App {
    static final Object lock1 = new Object();
    static final Object lock2 = new Object();
    static final MyLatch latch = new MyLatch(2);


    public static void main(String[] args) throws InterruptedException {
        synchronized (lock1){
            new Thread(() -> {
                synchronized (lock2){
                    latch.dec();
                    try {
                        latch.await();
                        System.out.println("Thread is starting");
                    } catch (InterruptedException e) {
                        return;
                    }
                    synchronized (lock1){
                        System.out.println("I am a child thread!");
                    }
                }
            }).start();
            latch.dec();
            try {
                latch.await();
                System.out.println("Main is starting");

            } catch (InterruptedException e) {
                return;
            }            synchronized (lock2){
                System.out.println("Main is here!");
            }
        }
    }
}
