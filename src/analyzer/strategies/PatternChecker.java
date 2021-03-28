package analyzer.strategies;

public class PatternChecker {
    private Strategy strategy;

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public boolean check(String text, String pattern) {
        return strategy.check(text, pattern);
    }
}
