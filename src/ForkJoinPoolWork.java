import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

class ArraySumTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 10;
    private final int[] array;
    private final int start;
    private final int end;

    public ArraySumTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }


    protected Long compute() {
        if (end - start <= THRESHOLD) {

            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        } else {

            int mid = (start + end) / 2;
            ArraySumTask leftTask = new ArraySumTask(array, start, mid);
            ArraySumTask rightTask = new ArraySumTask(array, mid, end);

            leftTask.fork();
            rightTask.fork();

            long leftResult = leftTask.join();
            long rightResult = rightTask.join();
            return leftResult + rightResult;
        }
    }
}

public class ForkJoinPoolWork {
    public static void main(String[] args) {

        int[] array = new int[100_000];
        for (int i = 0; i < array.length; i++) {
            array[i] = i + 1;
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ArraySumTask task = new ArraySumTask(array, 0, array.length);

        long startTime = System.currentTimeMillis();
        long parallelSum = forkJoinPool.invoke(task);
        long endTime = System.currentTimeMillis();
        System.out.println("Parallel Sum: " + parallelSum);
        System.out.println("Parallel Execution Time: " + (endTime - startTime) + " ms");


        startTime = System.currentTimeMillis();
        long singleThreadSum = 0;
        for (int value : array) {
            singleThreadSum += value;
        }
        endTime = System.currentTimeMillis();
        System.out.println("Single-Threaded Sum: " + singleThreadSum);
        System.out.println("Single-Threaded Execution Time: " + (endTime - startTime) + " ms");
    }
}

