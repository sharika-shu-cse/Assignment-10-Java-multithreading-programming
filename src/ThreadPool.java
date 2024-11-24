import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Task implements Runnable {
    private final int taskId;

    public Task(int taskId) {
        this.taskId = taskId;
    }

    public void run() {
        System.out.println("Task " + taskId + " is running on thread: " + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Task " + taskId + " completed on thread: " + Thread.currentThread().getName());
    }
}

public class ThreadPool {
    public static void main(String[] args) {
        int numberOfTasks = 10;
        int poolSize = 3;

        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        for (int i = 1; i <= numberOfTasks; i++) {
            executor.submit(new Task(i));
        }

        executor.shutdown();

        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        System.out.println("All tasks finished.");
    }
}
