public class EasyBasicThreadCreation implements Runnable {
    public void run() {
        System.out.println("Thread is running! This is a simple message.");
    }
    public static void main(String[] args) {
        EasyBasicThreadCreation runnable = new EasyBasicThreadCreation();
        Thread thread = new Thread(runnable);
        thread.start();
    }
}

