import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CustomThreadPool {
    private final BlockingQueue<Runnable> taskQueue;
    private WorkerThread[] workerThreads;
    private volatile boolean isShutdown;

    public CustomThreadPool(int initialSize) {
        taskQueue = new LinkedBlockingQueue<>();
        workerThreads = new WorkerThread[initialSize];
        isShutdown = false;


        for (int i = 0; i < initialSize; i++) {
            workerThreads[i] = new WorkerThread(taskQueue);
            workerThreads[i].start();
        }
    }


    public void submit(Runnable task) {
        if (!isShutdown) {
            taskQueue.offer(task);
        } else {
            throw new IllegalStateException("Thread pool is shutting down. Cannot accept new tasks.");
        }
    }

    public synchronized void resize(int newSize) {
        if (newSize > workerThreads.length) {

            WorkerThread[] newWorkers = new WorkerThread[newSize];
            System.arraycopy(workerThreads, 0, newWorkers, 0, workerThreads.length);

            for (int i = workerThreads.length; i < newSize; i++) {
                newWorkers[i] = new WorkerThread(taskQueue);
                newWorkers[i].start();
            }
            workerThreads = newWorkers;
        } else if (newSize < workerThreads.length) {

            for (int i = newSize; i < workerThreads.length; i++) {
                workerThreads[i].stopWorker();
            }
            WorkerThread[] newWorkers = new WorkerThread[newSize];
            System.arraycopy(workerThreads, 0, newWorkers, 0, newSize);
            workerThreads = newWorkers;
        }
    }

    public synchronized void shutdown() {
        isShutdown = true;
        for (WorkerThread worker : workerThreads) {
            worker.stopWorker();
        }
    }


    private static class WorkerThread extends Thread {
        private final BlockingQueue<Runnable> taskQueue;
        private volatile boolean isRunning;

        public WorkerThread(BlockingQueue<Runnable> taskQueue) {
            this.taskQueue = taskQueue;
            this.isRunning = true;
        }

        public void run() {
            while (isRunning) {
                try {
                    Runnable task = taskQueue.take();
                    task.run();
                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                }
            }
        }

        public void stopWorker() {
            isRunning = false;
            this.interrupt();
        }
    }


    public static void main(String[] args) throws InterruptedException {

        CustomThreadPool threadPool = new CustomThreadPool(3);


        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            threadPool.submit(() -> {
                System.out.println("Task " + taskId + " is running on thread " + Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Task " + taskId + " is completed on thread " + Thread.currentThread().getName());
            });
        }

        System.out.println("Resizing thread pool to 5 threads...");
        threadPool.resize(5);

        for (int i = 11; i <= 15; i++) {
            final int taskId = i;
            threadPool.submit(() -> {
                System.out.println("Task " + taskId + " is running on thread " + Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Task " + taskId + " is completed on thread " + Thread.currentThread().getName());
            });
        }


        Thread.sleep(5000);
        System.out.println("Shutting down thread pool...");
        threadPool.shutdown();
    }
}
