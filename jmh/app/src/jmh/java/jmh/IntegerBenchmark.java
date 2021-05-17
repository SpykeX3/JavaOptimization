package jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class IntegerBenchmark {
    private static IntegerChecker castChecker = new IntegerCastChecker();
    private static IntegerChecker digitChecker = new IntegerIsDigitChecker();
    private static IntegerChecker regexChecker = new IntegerRegexChecker();

    private static String NUM = "123999333";
    private static String NON_NUM = "123999333A";


    @Benchmark
    public void benchmarkCastChecker(Blackhole bh) {
        bh.consume(castChecker.isInteger(NUM));
        bh.consume(castChecker.isInteger(NON_NUM));
    }

    @Benchmark
    public void benchmarkDigitChecker(Blackhole bh) {
        bh.consume(digitChecker.isInteger(NUM));
        bh.consume(digitChecker.isInteger(NON_NUM));
    }

    @Benchmark
    public void benchmarkRegexChecker(Blackhole bh) {
        bh.consume(regexChecker.isInteger(NUM));
        bh.consume(regexChecker.isInteger(NON_NUM));
    }

}

