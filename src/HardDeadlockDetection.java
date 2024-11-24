import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

class Resource {
    private final String name;

    public Resource(String name) {
        this.name = name;
    }

    public synchronized void use(Resource other) {
        System.out.println(Thread.currentThread().getName() + " locked " + this.name);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(Thread.currentThread().getName() + " attempting to lock " + other.name);
        synchronized (other) {
            System.out.println(Thread.currentThread().getName() + " locked " + other.name);
        }
    }
}

public class HardDeadlockDetection {
    public static void main(String[] args) {
        Resource resource1 = new Resource("Resource1");
        Resource resource2 = new Resource("Resource2");

        Thread thread1 = new Thread(() -> resource1.use(resource2), "Thread1");

        Thread thread2 = new Thread(() -> resource2.use(resource1), "Thread2");

        thread1.start();
        thread2.start();

        Thread deadlockDetector = new Thread(() -> {
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            while (true) {
                long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
                if (deadlockedThreads != null) {
                    System.out.println("Deadlock detected!");
                    for (long threadId : deadlockedThreads) {
                        System.out.println("Thread involved in deadlock: " +
                                threadMXBean.getThreadInfo(threadId).getThreadName());
                    }
                    System.exit(1);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        deadlockDetector.setDaemon(true);
        deadlockDetector.start();
    }
}
