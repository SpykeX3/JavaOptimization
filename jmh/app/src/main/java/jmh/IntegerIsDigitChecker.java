package jmh;

public class IntegerIsDigitChecker implements IntegerChecker {
    @Override
    public boolean isInteger(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return str.length() > 0;
    }
}
