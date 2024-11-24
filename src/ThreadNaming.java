public class ThreadNaming implements Runnable {
    public void run() {
        System.out.println("Thread is running: " + Thread.currentThread().getName());
    }
    public static void main(String[] args) {
        ThreadNaming task = new ThreadNaming();

        Thread thread1 = new Thread(task, "Worker-1");
        Thread thread2 = new Thread(task, "Worker-2");
        Thread thread3 = new Thread(task, "Worker-3");

        thread1.start();
        thread2.start();
        thread3.start();
    }
}

