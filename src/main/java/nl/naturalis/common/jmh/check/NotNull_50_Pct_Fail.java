package nl.naturalis.common.jmh.check;

import nl.naturalis.common.check.Check;
import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static nl.naturalis.common.check.CommonChecks.notNull;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms1G", "-Xmx1G"})
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 3, time = 3)
public class NotNull_50_Pct_Fail {

  public static String testVal;

  @Benchmark
  public void manual(Blackhole bh) {
    try {
      if (testVal == null) {
        throw new IllegalArgumentException("arg must not be null");
      }
      bh.consume(testVal);
    } catch (IllegalArgumentException e) {
    }
  }

  @Benchmark
  public void staticFactoryMethod(Blackhole bh) {
    try {
      bh.consume(Check.notNull(testVal, "arg").ok());
    } catch (IllegalArgumentException e) {
    }
  }

  @Benchmark
  public void notNullPlain(Blackhole bh) {
    try {
      bh.consume(Check.that(testVal, "arg").is(notNull()).ok());
    } catch (IllegalArgumentException e) {
    }
  }

  @Benchmark
  public void notNullCustomMsg(Blackhole bh) {
    try {
      bh.consume(Check.that(testVal).is(notNull(), "Not allowed: ${arg}").ok());
    } catch (IllegalArgumentException e) {
    }
  }

  @Benchmark
  public void notNullCustomExc(Blackhole bh) {
    try {
      bh.consume(Check.that(testVal)
          .is(notNull(), () -> new IOException("arg must not be null"))
          .ok());
    } catch (IOException e) {
    }
  }

  ThreadLocal<AtomicInteger> counter = new ThreadLocal<>() {
    @Override
    protected AtomicInteger initialValue() {
      return new AtomicInteger();
    }
  };

  @Setup(Level.Invocation)
  public void setup() {
    int i = counter.get().getAndIncrement();
    if (i % 2 == 0) {
      testVal = null;
    } else {
      testVal = RandomStringUtils.randomAlphabetic(10, 15);
    }
  }

}
