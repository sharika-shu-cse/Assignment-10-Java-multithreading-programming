import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class DistributedLocking {

    private static RedissonClient redisson;

    // Initialize Redisson client
    private static void initializeRedisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379"); // Adjust Redis address if needed
        redisson = Redisson.create(config);
    }

    // Acquire lock and perform work
    private static void acquireLockAndDoWork(String lockName) {
        RLock lock = redisson.getLock(lockName); // Get distributed lock
        try {
            // Try to acquire lock with a timeout
            if (lock.tryLock()) {
                System.out.println(Thread.currentThread().getName() + " acquired lock: " + lockName);
                // Simulate some work
                Thread.sleep(2000);
            } else {
                System.out.println(Thread.currentThread().getName() + " could not acquire lock: " + lockName);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + " released lock: " + lockName);
            }
        }
    }

    public static void main(String[] args) {
        // Initialize Redisson client
        initializeRedisson();

        // Create multiple threads simulating work
        for (int i = 0; i < 5; i++) {
            new Thread(() -> acquireLockAndDoWork("distributed-lock")).start();
        }
    }
}
