Simple Java stock CSV analyzer

What I added:
- `src/com/techstock/Stock.java` - parses a CSV row into typed fields.
- `src/com/techstock/Main.java` - simple analyzer that reads `sample_tech*.csv` and prints summaries.

java -cp out com.techstock.Main

You can also pass a CSV path as the first argument to Main.

Notes:
- The CSV parsing is naive (split on commas) which works for the provided dataset.
- The analyzer prints average PE, top upside to analyst targets, RSI outliers, dividend leaders, and top market caps.
- Statistics Time-Series data analyzer over period of time - structured data with same format can be analyzed to forecast and identify patterns.
