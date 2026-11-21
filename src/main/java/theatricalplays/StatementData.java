package theatricalplays;

import java.util.List;

public record StatementData(
        String customer,
        List<PerformanceData> performances,
        int totalAmount,
        int totalVolumeCredits) {
    public record PerformanceData(
            String playName,
            int audience,
            int amount,
            int volumeCredits) {
    }
}