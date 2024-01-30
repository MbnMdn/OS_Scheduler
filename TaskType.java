public enum TaskType {
    X, Y, Z;

    public static TaskType taskType(char type){
        return switch (type) {
            case 'X' -> TaskType.X;
            case 'Y' -> TaskType.Y;
            case 'Z' -> TaskType.Z;
            default -> null;
        };
    }
}
