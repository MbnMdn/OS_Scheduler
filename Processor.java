import java.util.*;
import java.util.stream.Stream;

public class Processor implements Runnable {
    final Lock lock;
    String name;

    Queue<Task> readyQueue;
    Queue<Task> waitingQueue;
    int[] availableResources;
    int currentRound;
    Task currentTask;


    public Processor(String name, Queue<Task> readyQueue, Lock lock, int[] availableResources, Queue<Task> waitingQueue) {
        this.name = name;
        this.readyQueue = readyQueue;
        this.lock = lock;
        this.availableResources = availableResources;
        this.waitingQueue = waitingQueue;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public void FCFS() {
        if (this.currentTask == null && !this.readyQueue.isEmpty()) {

            List<Task> localQueue = new ArrayList<>(readyQueue);

            Comparator<Task> durationOnWaitComparator = Comparator.comparing(task -> task.durationOnWait);
            Comparator<Task> priorityComparator = Comparator.comparing(task -> task.priority);
            priorityComparator = priorityComparator.thenComparing(durationOnWaitComparator.reversed());

            localQueue.sort(priorityComparator);

            Collections.reverse(localQueue);
            if(!localQueue.isEmpty()){
                Task topTask = localQueue.remove(0);

                if (checkResourceAvailability(topTask.type, true)) {
                    topTask.setStartRound(this.currentRound);
                    topTask.state = TaskState.RUNNING;
                    this.currentTask = topTask;
                    this.readyQueue.remove(topTask);
                    topTask.incrementDurationOnCpu();
                    if (topTask.durationOnCpu == topTask.duration) {
                        topTask.state = TaskState.FINISHED;
                        this.freeResources(topTask.type);
                        this.currentTask = null;
                    }
                } else {
                    topTask.state = TaskState.WAITING;
                    this.readyQueue.remove(topTask);
                    this.waitingQueue.add(topTask);
                }
            }

        } else if (this.currentTask != null) {
            Task topTask = this.currentTask;
            topTask.incrementDurationOnCpu();

            if (topTask.durationOnCpu == topTask.duration) {
                topTask.state = TaskState.FINISHED;
            }

        } else if (!this.waitingQueue.isEmpty()) {
            processWaitingQueue();
        }
    }

    private void processWaitingQueue() {
        Queue<Task> schedulable = new PriorityQueue<>();
        Queue<Task> temp = new LinkedList<>(this.waitingQueue);

        for (int i = 0; i < temp.size(); i++) {
            Task topOnWaiting = temp.poll();
            if (checkResourceAvailability(topOnWaiting.type, false)) {
                schedulable.add(topOnWaiting);
            }
        }

//        Comparator<Task> durationOnWaitComparator = Comparator.comparing(task -> task.durationOnWait);
//        Comparator<Task> priorityComparator = Comparator.comparing(task -> task.priority);
//        priorityComparator = priorityComparator.thenComparing(durationOnWaitComparator);
//
//        schedulable.sort(priorityComparator);
//        Collections.reverse(schedulable);


        Task topPriority = schedulable.poll();

        if (topPriority != null) {
            this.waitingQueue.remove(topPriority);
            topPriority.priority++;
            topPriority.durationOnWait = currentRound;
            topPriority.state = TaskState.READY;
            this.readyQueue.offer(topPriority);
        }


    }


    @Override
    public String toString() {
        return "Core='" + name + '\'' +
                ", currentTask=" + currentTask +
                "\n";
    }

    @Override
    public void run() {
        if (this.currentTask != null && this.currentTask.state == TaskState.FINISHED) {
            this.freeResources(this.currentTask.type);
            this.currentTask = null;
        }
        synchronized (this.lock) {
            this.FCFS();
        }
    }

    private boolean checkResourceAvailability(TaskType type, boolean allocate) {
        switch (type) {
            case X:
                if (this.availableResources[0] >= 1 && this.availableResources[1] >= 1) {
                    if (allocate) {
                        this.availableResources[0]--;
                        this.availableResources[1]--;
                    }
                    return true;
                }
            case Y:
                if (this.availableResources[1] >= 1 && this.availableResources[2] >= 1) {
                    if (allocate) {
                        this.availableResources[1]--;
                        this.availableResources[2]--;
                    }
                    return true;

                }
            case Z:
                if (this.availableResources[0] >= 1 && this.availableResources[2] >= 1) {
                    if (allocate) {
                        this.availableResources[0]--;
                        this.availableResources[2]--;
                    }
                    return true;

                }
        }

        return false;
    }

    private void freeResources(TaskType type) {
        switch (type) {
            case X:
                this.availableResources[0]++;
                this.availableResources[1]++;
                break;
            case Y:
                this.availableResources[1]++;
                this.availableResources[2]++;
                break;
            case Z:
                this.availableResources[2]++;
                this.availableResources[0]++;
                break;
        }
    }


}
