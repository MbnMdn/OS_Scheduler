public class Task implements Comparable<Task> {
    public String name;
    public TaskType type;
    public TaskState state;
    public int duration;
    public int durationOnCpu;
    public int priority;

    public Task(String name, char type, TaskState state, int duration) {
        this.name = name;
        this.type = TaskType.taskType(type);
        this.state = state;
        this.duration = duration;
    }

    public Task() {

    }

    @Override
    public String toString() {
        return this.name;
    }

    public void incrementDurationOnCpu() {
        this.durationOnCpu++;
    }

    public char getType() {
        return type.toString().charAt(0);
    }


    @Override
    public int compareTo(Task o) {
        return Character.compare(o.getType(), this.getType());
    }


}

