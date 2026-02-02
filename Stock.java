package com.techstock;

import java.util.Map;

public class Stock {
    public final String symbol;
    public final String company;
    public final double price;
    public final double week52High;
    public final double week52Low;
    public final double beta;
    public final double eps;
    public final double peRatio;
    public final double marketCapB;
    public final double revenueGrowthYoY;
    public final double profitMargin;
    public final double roe;
    public final double debtToEquity;
    public final double currentRatio;
    public final String analystRating;
    public final double priceTarget;
    public final double dividendYield;
    public final double volumeM;
    public final double rsi;
    public final double movingAvg50;
    public final double movingAvg200;

    public Stock(String[] tokens, Map<String,Integer> idx) {
        this.symbol = getStr(tokens, idx, "Symbol");
        this.company = getStr(tokens, idx, "Company");
        this.price = getDouble(tokens, idx, "Price");
        this.week52High = getDouble(tokens, idx, "52_Week_High");
        this.week52Low = getDouble(tokens, idx, "52_Week_Low");
        this.beta = getDouble(tokens, idx, "Beta");
        this.eps = getDouble(tokens, idx, "EPS");
        this.peRatio = getDouble(tokens, idx, "PE_Ratio");
        this.marketCapB = getDouble(tokens, idx, "Market_Cap_B");
        this.revenueGrowthYoY = getDouble(tokens, idx, "Revenue_Growth_YoY");
        this.profitMargin = getDouble(tokens, idx, "Profit_Margin");
        this.roe = getDouble(tokens, idx, "ROE");
        this.debtToEquity = getDouble(tokens, idx, "Debt_to_Equity");
        this.currentRatio = getDouble(tokens, idx, "Current_Ratio");
        this.analystRating = getStr(tokens, idx, "Analyst_Rating");
        this.priceTarget = getDouble(tokens, idx, "Price_Target");
        this.dividendYield = getDouble(tokens, idx, "Dividend_Yield");
        this.volumeM = getDouble(tokens, idx, "Volume_M");
        this.rsi = getDouble(tokens, idx, "RSI");
        this.movingAvg50 = getDouble(tokens, idx, "Moving_Avg_50");
        this.movingAvg200 = getDouble(tokens, idx, "Moving_Avg_200");
    }

    private static String getStr(String[] tokens, Map<String,Integer> idx, String key) {
        Integer i = idx.get(key);
        if (i == null || i >= tokens.length) return "";
        return tokens[i].trim();
    }

    private static double getDouble(String[] tokens, Map<String,Integer> idx, String key) {
        String s = getStr(tokens, idx, key);
        if (s == null || s.isEmpty()) return Double.NaN;
        try {
            return Double.parseDouble(s.replaceAll("\\s",""));
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s): price=%.2f, marketCapB=%.2f", symbol, company, price, marketCapB);
    }
}
