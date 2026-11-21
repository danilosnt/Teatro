package theatricalplays;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StatementPrinter {

    public String print(Invoice invoice, Map<String, Play> plays) {
        return renderPlainText(createStatementData(invoice, plays));
    }

    public String printHtml(Invoice invoice, Map<String, Play> plays) {
        return renderHtml(createStatementData(invoice, plays));
    }

    private StatementData createStatementData(Invoice invoice, Map<String, Play> plays) {
        List<StatementData.PerformanceData> enrichedPerformances = new ArrayList<>();
        for (var perf : invoice.performances) {
            var calculator = PerformanceCalculator.create(perf, playFor(perf, plays));
            enrichedPerformances.add(new StatementData.PerformanceData(
                    calculator.play.name,
                    perf.audience,
                    calculator.amount(),
                    calculator.volumeCredits()));
        }
        int totalAmount = enrichedPerformances.stream().mapToInt(StatementData.PerformanceData::amount).sum();
        int totalVolumeCredits = enrichedPerformances.stream().mapToInt(StatementData.PerformanceData::volumeCredits)
                .sum();
        return new StatementData(invoice.customer, enrichedPerformances, totalAmount, totalVolumeCredits);
    }

    private String renderPlainText(StatementData data) {
        var result = String.format("Statement for %s%n", data.customer());
        for (var perf : data.performances()) {
            result += String.format("  %s: %s (%s seats)%n", perf.playName(), usd(perf.amount()), perf.audience());
        }
        result += String.format("Amount owed is %s%n", usd(data.totalAmount()));
        result += String.format("You earned %s credits%n", data.totalVolumeCredits());
        return result;
    }

    private String renderHtml(StatementData data) {
        var result = new StringBuilder();
        result.append(String.format("<h1>Statement for %s</h1>%n", data.customer()));
        result.append("<table>\n<tr><th>play</th><th>seats</th><th>cost</th></tr>\n");
        for (var perf : data.performances()) {
            result.append(String.format("  <tr><td>%s</td><td>%d</td><td>%s</td></tr>%n",
                    perf.playName(), perf.audience(), usd(perf.amount())));
        }
        result.append("</table>\n");
        result.append(String.format("<p>Amount owed is <em>%s</em></p>%n", usd(data.totalAmount())));
        result.append(String.format("<p>You earned <em>%d</em> credits</p>%n", data.totalVolumeCredits()));
        return result.toString();
    }

    private Play playFor(Performance perf, Map<String, Play> plays) {
        return plays.get(perf.playID);
    }

    private String usd(int amountInCents) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amountInCents / 100.0);
    }
}
