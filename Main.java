import java.io.File;
import java.io.IOException;
import java.util.*;


public class Main {
    static final int PROCESSOR_COUNT = 4;

    public static void main(String[] args) throws IOException, InterruptedException {
        int[] resources = new int[3];
        List<Task> tasks = new ArrayList<>();
        Queue<Task> readyQueue = new LinkedList<>();
        Queue<Task> waitingQueue = new LinkedList<Task>();

        File file = new File("/Users/mbina/Desktop/Uni/Network/OS_Scheduler/testcases/input3.txt");
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            for (int i = 0; i < 3; i++) {
                resources[i] = (scanner.nextInt());
            }

            int n = scanner.nextInt();
            for (int i = 0; i < n; i++) {
                String name = scanner.next();
                char type = scanner.next().charAt(0);
                int duration = scanner.nextInt();
                Task newTask = new Task(name, type, duration);
                readyQueue.add(newTask);
                tasks.add(newTask);
            }
        }

        ArrayList<Processor> processors = new ArrayList<>();

        Lock lock = new Lock();

        for (int i = 0; i < PROCESSOR_COUNT; i++) {
            Processor processor = new Processor("CPU" + (i + 1), readyQueue, lock, resources, waitingQueue);
            processors.add(processor);
        }

        int Round = 0;
        while (true) {
            Round++;
            Thread[] processorThreads = new Thread[PROCESSOR_COUNT];

            for (int i = 0; i < PROCESSOR_COUNT; i++) {
                processors.get(i).setCurrentRound(Round);
                Thread thread = new Thread(processors.get(i));
                processorThreads[i] = thread;
                processorThreads[i].start();
            }

            for (int i = 0; i < PROCESSOR_COUNT; i++) {
                processorThreads[i].join();
            }

            boolean breakOrNot = true;

            System.out.println("R1: " + resources[0] + " R2: " + resources[1] + " R3: " + resources[2]);
            System.out.println("Ready Queue : " + readyQueue);
            System.out.println("Waiting Queue : " + waitingQueue);
            System.out.println(printResult(processors, Round));

            for (Task e : waitingQueue) {
                e.durationOnWait++;
            }

            for (Task e :
                    tasks) {
                if (e.state != TaskState.FINISHED) {
                    breakOrNot = false;
                }
            }

            if (breakOrNot) break;

        }

        double sumWaiting = 0;
        for (Task e :
                tasks) {
            sumWaiting += e.durationOnWait;
        }
        System.out.println("Average waiting: " + sumWaiting / tasks.size());


    }

    public static String printResult(ArrayList<Processor> arrayList, int Round) {
        StringBuilder result = new StringBuilder();

        for (Processor core : arrayList) {
            result.append("Core: ").append(core.name);
            result.append(", Time: ").append(Round);
            result.append(", Task: ").append(core.currentTask != null ? core.currentTask.toString() : "IDLE").append("\n");
        }


        return result.toString();
    }
}
