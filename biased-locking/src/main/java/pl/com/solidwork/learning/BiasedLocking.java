package pl.com.solidwork.learning;

import org.openjdk.jol.info.ClassLayout;

/**
 * Hello world!
 *
 */
public class BiasedLocking
{
    public static final String GREEN = "\033[0;32m"; // Green
    public static final String RESET = "\033[0m";  // Reset to default color

    private final Object lock = new Object();

    public void run() throws InterruptedException {

        // this should show biasable / unlocked object
        System.out.println(GREEN + "before acquiring any lock" + RESET);
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());

        // this should create biased lock
        synchronized (lock) {
            System.out.println(GREEN + "thread acquired the lock" + RESET);
            System.out.println(ClassLayout.parseInstance(lock).toPrintable());
        }

        // the lock is still present in the MarkWord
        System.out.println(GREEN + "thread released the lock" + RESET);
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());

        // second thread
        Thread threadTwo = new Thread(() -> {
            synchronized (lock) {
                System.out.println(GREEN + "thread 2 acquire the lock" + RESET);
                System.out.println(ClassLayout.parseInstance(lock).toPrintable());
            }
        });

        threadTwo.start();
        threadTwo.join();

        System.out.println(GREEN + "thread 2 released the lock" + RESET);
        System.out.println(ClassLayout.parseInstance(lock).toPrintable());

        synchronized (lock) {
            System.out.println(GREEN + "thread one try acquire the lock again" + RESET);
            System.out.println(ClassLayout.parseInstance(lock).toPrintable());
            Thread.sleep(200);
        }
    }

    /**
     * This application requires JVM version from 6 to 14 (inclusive).
     */
    public static void main(String[] args) throws InterruptedException {

        BiasedLocking biasedLocking = new BiasedLocking();
        biasedLocking.run();
    }
}
