public class Task implements Comparable<Task> {
    public String name;
    public TaskType type;
    public TaskState state = TaskState.NOT_STARTED;
    public int duration;
    public int durationOnCpu;
    public int durationOnWait;
    public int priority;
    public int startRound;

    public Task(String name, char type, int duration) {
        this.name = name;
        this.type = TaskType.taskType(type);
        this.duration = duration;
        this.priority = TaskType.Priority(type);
    }

    @Override
    public String toString() {
        return this.name + " " + this.durationOnWait;
    }

    public void setStartRound(int startRound) {
        this.startRound = startRound;
    }

    public void incrementDurationOnCpu() {
        this.durationOnCpu++;
    }

    public void incrementDurationOnWait() {
        this.durationOnWait++;
    }

    public char getType() {
        return type.toString().charAt(0);
    }


    @Override
    public int compareTo(Task o) {
        return o.priority - this.priority;
    }
}

