package theatricalplays;

public abstract class PerformanceCalculator {
    protected final Performance performance;
    protected final Play play;

    public PerformanceCalculator(Performance performance, Play play) {
        this.performance = performance;
        this.play = play;
    }

    public static PerformanceCalculator create(Performance performance, Play play) {
        return switch (play.type) {
            case "tragedy" -> new TragedyCalculator(performance, play);
            case "comedy" -> new ComedyCalculator(performance, play);
            default -> throw new Error("unknown type: %s".formatted(play.type));
        };
    }

    public abstract int amount();

    public int volumeCredits() {
        return Math.max(performance.audience - 30, 0);
    }
}