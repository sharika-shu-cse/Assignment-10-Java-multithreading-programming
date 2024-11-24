import io.reactivex.rxjava3.core.Observable;

public class ReactiveProgrammingWithRxJava {

    public static void main(String[] args) {
        // Create an Observable that emits a stream of integers
        Observable<Integer> observable = Observable.create(emitter -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    emitter.onNext(i); // Emit each integer
                    Thread.sleep(500); // Simulate a delay
                }
                emitter.onComplete(); // Signal that the stream is complete
            } catch (Exception e) {
                emitter.onError(e); // Signal an error if any occurs
            }
        });

        // Create a Subscriber (Observer)
        observable
                .map(item -> item * 2) // Apply an operator to transform the data (double each item)
                .filter(item -> item % 4 == 0) // Filter only items divisible by 4
                .subscribe(
                        item -> System.out.println("Received: " + item), // OnNext
                        error -> System.err.println("Error: " + error), // OnError
                        () -> System.out.println("Stream Completed!") // OnComplete
                );
    }
}
