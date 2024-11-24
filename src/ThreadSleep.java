public class ThreadSleep implements Runnable {
    private String threadName;

    public ThreadSleep(String name) {
        this.threadName = name;
    }
    public void run() {
        try {
            System.out.println(threadName + " is starting.");
            Thread.sleep(3000);
            System.out.println(threadName + " has finished sleeping.");
        } catch (InterruptedException e) {
            System.out.println(threadName + " was interrupted.");
        }
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread(new ThreadSleep("Thread 1"));
        Thread thread2 = new Thread(new ThreadSleep("Thread 2"));
        Thread thread3 = new Thread(new ThreadSleep("Thread 3"));

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
