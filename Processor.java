import java.util.*;

public class Processor implements Runnable {
    final int QUANTUM = 3;

    final Lock lock;
    String name;

    Queue<Task> readyQueue;
    Queue<Task> waitingQueue;
    Queue<Task> localQueue;
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

    public void FCFS() {

        if (!this.waitingQueue.isEmpty()) {
            processWaitingQueue();
        }

        if (this.currentTask == null && !this.readyQueue.isEmpty()) {

            Queue<Task> localQueue = new PriorityQueue<>(readyQueue);

            Task topTask = localQueue.poll();

            assert topTask != null;

            if (checkResourceAvailability(topTask.type, true)) {
                topTask.setStartRound(this.currentRound);
                topTask.state = TaskState.RUNNING;
                topTask.startRound = currentRound;
                this.currentTask = topTask;
                this.readyQueue.remove(topTask);
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

        } else if (this.currentTask != null) {
            Task topTask = this.currentTask;

            if (topTask.durationOnCpu == topTask.duration) {
                topTask.state = TaskState.FINISHED;
                this.freeResources(topTask.type);
                this.currentTask = null;
            }

            topTask.incrementDurationOnCpu();
        }

    }

    public void HRRN() {

        if (!this.waitingQueue.isEmpty()) {
            processWaitingQueue();
        }

        if (this.currentTask == null && !this.readyQueue.isEmpty()) {

            List<Task> localQueue = new ArrayList<>(readyQueue);

            Comparator<Task> durationCompare = Comparator.comparing(task -> {
                return (currentRound + task.duration) / task.duration;
            });
            Comparator<Task> priorityComparator = Comparator.comparing(task -> task.priority);

            priorityComparator = durationCompare.reversed().thenComparing(priorityComparator.reversed());
            localQueue.sort(priorityComparator);

            Task topTask = localQueue.remove(0);

            assert topTask != null;

            if (checkResourceAvailability(topTask.type, true)) {
                topTask.setStartRound(this.currentRound);
                topTask.state = TaskState.RUNNING;
                topTask.startRound = currentRound;
                this.currentTask = topTask;
                this.readyQueue.remove(topTask);
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

        } else if (this.currentTask != null) {
            Task topTask = this.currentTask;
            if (topTask.durationOnCpu == topTask.duration) {
                topTask.state = TaskState.FINISHED;
                this.freeResources(topTask.type);
                this.currentTask = null;
            }
            topTask.incrementDurationOnCpu();

        }

    }

    public void SJF() {

        if (!this.waitingQueue.isEmpty()) {
            processWaitingQueue();
        }

        if (this.currentTask == null && !this.readyQueue.isEmpty()) {

            List<Task> localQueue = new ArrayList<>(readyQueue);

            Comparator<Task> durationCompare = Comparator.comparing(task -> task.duration);
            Comparator<Task> priorityComparator = Comparator.comparing(task -> task.priority);

            priorityComparator = durationCompare.thenComparing(priorityComparator.reversed());
            localQueue.sort(priorityComparator);

            Task topTask = localQueue.remove(0);

            assert topTask != null;

            if (checkResourceAvailability(topTask.type, true)) {
                topTask.setStartRound(this.currentRound);
                topTask.state = TaskState.RUNNING;
                topTask.startRound = currentRound;
                this.currentTask = topTask;
                this.readyQueue.remove(topTask);
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

        } else if (this.currentTask != null) {
            Task topTask = this.currentTask;
            if (topTask.durationOnCpu == topTask.duration) {
                topTask.state = TaskState.FINISHED;
                this.freeResources(topTask.type);
                this.currentTask = null;
            }
            topTask.incrementDurationOnCpu();

        }

    }

    public void RR() {

        if (!this.waitingQueue.isEmpty()) {
            processWaitingQueue();
        }

        if (this.currentTask == null && !this.readyQueue.isEmpty()) {

            Queue<Task> localQueue = new PriorityQueue<>(readyQueue);
            Task topTask = localQueue.poll();

            assert topTask != null;

            if (checkResourceAvailability(topTask.type, true)) {
                topTask.setStartRound(this.currentRound);
                topTask.state = TaskState.RUNNING;
                this.currentTask = topTask;
                this.readyQueue.remove(topTask);
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

        } else if (this.currentTask != null) {
            Task topTask = this.currentTask;

            if (topTask.durationOnCpu == topTask.duration) {
                topTask.state = TaskState.FINISHED;
                this.freeResources(topTask.type);
                this.currentTask = null;
            } else {
                if (topTask.durationOnCurrentCpu == QUANTUM + 1) {
                    if (topTask.durationOnCpu + 1 == topTask.duration) {
                        topTask.state = TaskState.FINISHED;
                        this.freeResources(topTask.type);
                    } else {
                        topTask.state = TaskState.READY;
                        topTask.durationOnCurrentCpu = 0;
                        this.freeResources(topTask.type);
                        this.readyQueue.offer(topTask);
                    }
                    this.currentTask = null;

                }
                topTask.incrementDurationOnCpu();
            }
        }
    }

    private void processWaitingQueue() {
        Queue<Task> schedulable = new PriorityQueue<>();
        Queue<Task> temp = new LinkedList<>(this.waitingQueue);

        for (int i = 0; i < temp.size(); i++) {
            Task topOnWaiting = temp.poll();
            if (topOnWaiting.durationOnWait >= 4) {
                topOnWaiting.priority++;
            }
            if (checkResourceAvailability(topOnWaiting.type, false)) {
                schedulable.add(topOnWaiting);
            }
        }

        Task topPriority = schedulable.poll();

        if (topPriority != null) {
            this.waitingQueue.remove(topPriority);
            topPriority.priority++;
            topPriority.state = TaskState.READY;
            this.readyQueue.offer(topPriority);
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
                break;
            case Y:
                if (this.availableResources[1] >= 1 && this.availableResources[2] >= 1) {
                    if (allocate) {
                        this.availableResources[1]--;
                        this.availableResources[2]--;
                    }
                    return true;
                }
                break;
            case Z:
                if (this.availableResources[0] >= 1 && this.availableResources[2] >= 1) {
                    if (allocate) {
                        this.availableResources[0]--;
                        this.availableResources[2]--;
                    }
                    return true;
                }
                break;
            default:
                return false;
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

    @Override
    public void run() {
        if (this.currentTask != null && this.currentTask.state == TaskState.FINISHED) {
            this.freeResources(this.currentTask.type);
            this.currentTask = null;
        }
        synchronized (this.lock) {
            this.HRRN();
        }
    }

    @Override
    public String toString() {
        return "Core='" + name + '\'' +
                ", currentTask=" + currentTask +
                "\n";
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

}
