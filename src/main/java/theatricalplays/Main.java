package theatricalplays;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("==================================================");
        System.out.println("  SISTEMA DE FATURAMENTO TEATRAL (REFATORADO)     ");
        System.out.println("==================================================\n");

        // 1. Dados de exemplo das peças (Catálogo)
        Map<String, Play> plays = Map.of(
                "hamlet",  new Play("Hamlet", "tragedy"),
                "as-like", new Play("As You Like It", "comedy"),
                "othello", new Play("Othello", "tragedy")
        );

        // 2. Fatura do cliente BigCo
        Invoice invoice = new Invoice("BigCo", List.of(
                new Performance("hamlet", 55),
                new Performance("as-like", 35),
                new Performance("othello", 40)
        ));

        // 3. Execução da regra de negócio refatorada
        StatementPrinter printer = new StatementPrinter();
        String faturaTexto = printer.print(invoice, plays);
        String faturaHtml = printer.printHtml(invoice, plays);

        // 4. Exibição do resultado em Texto Puro
        System.out.println("--- SAIDA EM TEXTO PURO ---");
        System.out.println(faturaTexto);

        // 5. Exibição do resultado em HTML e geração do arquivo
        System.out.println("--- SAIDA EM HTML ---");
        System.out.println(faturaHtml);

        Path htmlPath = Path.of("fatura.html");
        Files.writeString(htmlPath, faturaHtml);
        System.out.println("==================================================");
        System.out.println("Arquivo HTML gerado em: " + htmlPath.toAbsolutePath());
        System.out.println("==================================================");
    }
}
