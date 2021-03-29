package analyzer.strategies;

public class PatternChecker {
    private Strategy strategy;

    public PatternChecker() {
        this.strategy = new NaiveStrategy();
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public boolean check(String text, String pattern) {
        return strategy.check(text, pattern);
    }
}
