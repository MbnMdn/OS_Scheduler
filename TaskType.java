public enum TaskType {
    X, Y, Z;

    public static TaskType taskType(char type) {
        return switch (type) {
            case 'X' -> TaskType.X;
            case 'Y' -> TaskType.Y;
            case 'Z' -> TaskType.Z;
            default -> null;
        };
    }

    public static int Priority(char type) {
        return switch (type) {
            case 'X' -> 1;
            case 'Y' -> 2;
            case 'Z' -> 3;
            default -> 0;
        };
    }
}
