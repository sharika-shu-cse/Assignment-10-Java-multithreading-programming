class SharedCounter {
    private int counter = 0;

    public synchronized void increment() {
        counter++;
        System.out.println(Thread.currentThread().getName() + " incremented counter to: " + counter);
    }

    public int getCounter() {
        return counter;
    }
}

class CounterTask implements Runnable {
    private final SharedCounter sharedCounter;

    public CounterTask(SharedCounter sharedCounter) {
        this.sharedCounter = sharedCounter;
    }
    public void run() {
        for (int i = 0; i < 5; i++) {
            sharedCounter.increment();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class MediumThreadSynchronization {
    public static void main(String[] args) {
        SharedCounter sharedCounter = new SharedCounter();

        Thread thread1 = new Thread(new CounterTask(sharedCounter), "Thread-1");
        Thread thread2 = new Thread(new CounterTask(sharedCounter), "Thread-2");

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final counter value: " + sharedCounter.getCounter());
    }
}

