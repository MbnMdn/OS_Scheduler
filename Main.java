import java.io.File;
import java.io.IOException;
import java.util.*;

/*
3   4   5
4
t1  X   5
t2  Z   8
t3  Y   4
t4  Z   2
*/

public class Main {
    static final int PROCESSOR_COUNT = 4;

    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList<Integer> resources = new ArrayList<>();
        Queue<Task> readyQueue = new LinkedList<Task>();
        Queue<Task> waitingQueue = new LinkedList<Task>();

        File file = new File("/Users/mbina/Desktop/Uni/Network/OS_Scheduler/testcases/input2.txt");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            for (int i = 0; i < 3; i++) {
                resources.add(scanner.nextInt());
            }

            int R1 = resources.get(0);
            int R2 = resources.get(1);
            int R3 = resources.get(2);


            int n = scanner.nextInt();
            for (int i = 0; i < n; i++) {
                String name = scanner.next();
                char type = scanner.next().charAt(0);
                int duration = scanner.nextInt();
                readyQueue.add(new Task(name, type, TaskState.READY, duration));
            }

//            System.out.println(readyQueue);

            ArrayList<Processor> processors = new ArrayList<>();
            Thread[] processorThreads = new Thread[PROCESSOR_COUNT];
            Task lock = new Task();
            for (int i = 0; i < PROCESSOR_COUNT; i++) {
                Processor processor = new Processor("CPU" + (i + 1), waitingQueue, lock);
                processors.add(processor);
                Thread thread = new Thread(processor);
                processorThreads[i] = thread;
                thread.start();
            }
            for (int i = 0; i < PROCESSOR_COUNT; i++) {
                processorThreads[i].join();
            }
        }

//        System.out.println(waitingQueue);

    }


}
