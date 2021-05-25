package jmh;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class IntegerBenchmark {
    private static IntegerChecker castChecker = new IntegerCastChecker();
    private static IntegerChecker digitChecker = new IntegerIsDigitChecker();
    private static IntegerChecker regexChecker = new IntegerRegexChecker();
    private static IntegerChecker compiledRegexChecker = new CompiledRegexChecker();

    private static String NUM = "123999333";
    private static String NON_NUM1 = "123999333A";
    private static String NON_NUM2 = "A123999333";


    @Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
    @Benchmark
    public void benchmarkCastCheckerInt(Blackhole bh) {
        bh.consume(castChecker.isInteger(NUM));
    }

    @Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
    @Benchmark
    public void benchmarkCastCheckerNint1(Blackhole bh) {
        bh.consume(castChecker.isInteger(NON_NUM1));
    }

    @Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
    @Benchmark
    public void benchmarkCastCheckerNint2(Blackhole bh) {
        bh.consume(castChecker.isInteger(NON_NUM2));
    }

    @Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
    @Benchmark
    public void benchmarkDigitCheckerInt(Blackhole bh) {
        bh.consume(digitChecker.isInteger(NUM));
    }

    @Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
    @Benchmark
    public void benchmarkDigitCheckerNint1(Blackhole bh) {
        bh.consume(digitChecker.isInteger(NON_NUM1));
    }

    @Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
    @Benchmark
    public void benchmarkDigitCheckerNint2(Blackhole bh) {
        bh.consume(digitChecker.isInteger(NON_NUM2));
    }

    @Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
    @Benchmark
    public void benchmarkRegexCheckerInt(Blackhole bh) {
        bh.consume(regexChecker.isInteger(NUM));
    }

    @Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
    @Benchmark
    public void benchmarkRegexCheckerNint1(Blackhole bh) {
        bh.consume(regexChecker.isInteger(NON_NUM1));
    }

    @Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
    @Benchmark
    public void benchmarkRegexCheckerNint2(Blackhole bh) {
        bh.consume(regexChecker.isInteger(NON_NUM2));
    }

    @Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
    @Benchmark
    public void benchmarkCompiledRegexCheckerInt(Blackhole bh) {
        bh.consume(compiledRegexChecker.isInteger(NUM));
    }

    @Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
    @Benchmark
    public void benchmarkCompiledRegexCheckerNint1(Blackhole bh) {
        bh.consume(compiledRegexChecker.isInteger(NON_NUM1));
    }

    @Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
    @Measurement(iterations = 5, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
    @Benchmark
    public void benchmarkCompiledRegexCheckerNint2(Blackhole bh) {
        bh.consume(compiledRegexChecker.isInteger(NON_NUM2));
    }
}

