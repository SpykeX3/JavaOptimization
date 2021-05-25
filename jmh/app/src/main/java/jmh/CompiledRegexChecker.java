package jmh;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class CompiledRegexChecker implements IntegerChecker {

    static private Predicate<String> predicate = Pattern.compile("^\\d+$").asPredicate();

    @Override
    public boolean isInteger(String str) {
        return predicate.test(str);
    }
}
