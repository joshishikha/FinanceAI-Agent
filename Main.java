package com.techstock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    // Simple contract:
    // - Input: CSV file path (first arg). If omitted uses sample_tech_stocks.csv in project root.
    // - Output: Console summary: counts, averages, top by upside to price target, high RSI, dividend leaders.

    public static void main(String[] args) throws Exception {
        Path root = new File(System.getProperty("user.dir")).toPath();
        String csvPath;
        if (args.length > 0) csvPath = args[0];
        else csvPath = root.resolve("sample_tech_stocks.csv").toString();

        File csv = new File(csvPath);
        if (!csv.exists()) {
            System.err.println("CSV file not found: " + csvPath);
            System.exit(2);
        }

        List<Stock> stocks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
            String header = br.readLine();
            if (header == null) {
                System.err.println("Empty CSV");
                System.exit(2);
            }
            String[] columns = header.split(",");
            Map<String,Integer> idx = new HashMap<>();
            for (int i = 0; i < columns.length; i++) idx.put(columns[i].trim(), i);

            String line;
            while ((line = br.readLine()) != null) {
                // naive CSV split: this dataset has no quotes or commas in fields
                String[] tokens = line.split(",");
                if (tokens.length < 3) continue;
                stocks.add(new Stock(tokens, idx));
            }
        }

        if (stocks.isEmpty()) {
            System.out.println("No stocks parsed.");
            return;
        }

        System.out.println("Parsed " + stocks.size() + " stocks\n");

        // Average PE (skip NaN and negative unreasonable values)
        double avgPE = stocks.stream()
            .map(s -> s.peRatio)
            .filter(d -> !Double.isNaN(d) && d > 0 && d < 1000)
            .mapToDouble(d -> d).average().orElse(Double.NaN);

        double avgMarketCap = stocks.stream()
            .mapToDouble(s -> s.marketCapB)
            .filter(d -> !Double.isNaN(d))
            .average().orElse(Double.NaN);

        System.out.printf("Average PE Ratio: %.2f\n", avgPE);
        System.out.printf("Average Market Cap (B): %.2f\n\n", avgMarketCap);

        // Top 5 by upside to price target
        List<Stock> withTarget = stocks.stream()
            .filter(s -> !Double.isNaN(s.priceTarget) && s.priceTarget > 0)
            .collect(Collectors.toList());

        List<Stock> topUpside = withTarget.stream()
            .sorted(Comparator.comparingDouble(s -> -((s.priceTarget - s.price)/s.price)))
            .limit(5)
            .collect(Collectors.toList());

        System.out.println("Top 5 by upside to analyst target:");
        for (Stock s : topUpside) {
            double upside = (s.priceTarget - s.price)/s.price * 100.0;
            System.out.printf("%s: price=%.2f target=%.2f upside=%.1f%%\n", s.symbol, s.price, s.priceTarget, upside);
        }

        System.out.println();

        // High RSI (overbought) and low RSI (oversold)
        List<Stock> overbought = stocks.stream().filter(s -> !Double.isNaN(s.rsi) && s.rsi >= 70).collect(Collectors.toList());
        List<Stock> oversold = stocks.stream().filter(s -> !Double.isNaN(s.rsi) && s.rsi <= 30).collect(Collectors.toList());

        System.out.println("Overbought (RSI >= 70): " + overbought.stream().map(s -> s.symbol).collect(Collectors.joining(", ")));
        System.out.println("Oversold (RSI <= 30): " + oversold.stream().map(s -> s.symbol).collect(Collectors.joining(", ")));

        System.out.println();

        // Dividend yield leaders
        List<Stock> dividendLeaders = stocks.stream()
            .filter(s -> !Double.isNaN(s.dividendYield) && s.dividendYield > 0)
            .sorted(Comparator.comparingDouble(s -> -s.dividendYield))
            .limit(5)
            .collect(Collectors.toList());

        System.out.println("Top dividend yields:");
        for (Stock s : dividendLeaders) {
            System.out.printf("%s: dividendYield=%.2f%%\n", s.symbol, s.dividendYield);
        }

        System.out.println();

        // Print a tiny table of the most valuable stocks by market cap
        System.out.println("Top 5 by Market Cap (B):");
        stocks.stream()
            .sorted(Comparator.comparingDouble(s -> -s.marketCapB))
            .limit(5)
            .forEach(s -> System.out.printf("%s: MarketCap=%.1fB price=%.2f\n", s.symbol, s.marketCapB, s.price));
    }
}
