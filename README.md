Simple Java stock CSV analyzer

What I added:
- `src/com/techstock/Stock.java` - POJO that parses a CSV row into typed fields.
- `src/com/techstock/Main.java` - simple analyzer that reads `sample_tech_stocks.csv` and prints summaries.

Build & run (Windows, cmd.exe):

1) Compile
   javac -d out src/com/techstock/*.java

2) Run (project root must contain `sample_tech_stocks.csv`)
   java -cp out com.techstock.Main

You can also pass a CSV path as the first argument to Main.

Notes:
- The CSV parsing is naive (split on commas) which works for the provided dataset.
- The analyzer prints average PE, top upside to analyst targets, RSI outliers, dividend leaders, and top market caps.
