import java.util.concurrent.*;
import java.util.*;

public class ConcurrentDataStructures {

    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

        Thread writerThread = new Thread(() -> {
            for (int i = 1; i <= 5; i++) {
                map.put("Key" + i, i);
                System.out.println(Thread.currentThread().getName() + " added: Key" + i + " -> " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread readerThread = new Thread(() -> {
            while (map.size() < 5) {
                map.forEach((key, value) -> System.out.println(Thread.currentThread().getName() + " read: " + key + " -> " + value));
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        writerThread.start();
        readerThread.start();

        writerThread.join();
        readerThread.join();

        System.out.println("\nFinal Map: " + map);

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

        Thread producerThread = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    queue.put(i);
                    System.out.println(Thread.currentThread().getName() + " produced: " + i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumerThread = new Thread(() -> {
            try {
                while (true) {
                    Integer item = queue.take();
                    System.out.println(Thread.currentThread().getName() + " consumed: " + item);
                    if (item == 5) break;
                    Thread.sleep(150);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });


        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();
    }
}
