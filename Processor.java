import java.util.Queue;

public class Processor implements Runnable {
    String name;
    Queue<Task> readyQueue;
    Queue<Task> localQueue;
    final Task lock;


    public Processor(String name, Queue<Task> readyQueue, Task lock) {
        this.name = name;
        this.readyQueue = readyQueue;
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (this.lock) {
            readyQueue.add(new Task(name, 'X', TaskState.RUNNING, 5));
            System.out.println(name);
            System.out.println(readyQueue);
            System.out.println();
        }
    }

    public static Task FCFS(Queue<Task> readyQueue) {
        return readyQueue.peek();
    }
}
