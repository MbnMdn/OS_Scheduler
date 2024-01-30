public class Task {
    public String name;
    public TaskType type;
    public TaskState state;
    public int duration;
    public int durationOnCpu;

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
        return "Task{" +
                "name='" + name + '\'' +
//                ", type=" + type +
//                ", state=" + state +
//                ", durationOnCpu=" + durationOnCpu +
//                ", duration=" + duration +
                '}';
    }
}

