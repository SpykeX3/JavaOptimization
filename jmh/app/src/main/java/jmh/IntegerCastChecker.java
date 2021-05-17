package jmh;

public class IntegerCastChecker implements IntegerChecker {
    @Override
    public boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
