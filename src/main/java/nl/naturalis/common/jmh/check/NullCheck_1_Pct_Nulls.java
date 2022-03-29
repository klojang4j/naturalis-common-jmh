package nl.naturalis.common.jmh.check;

import nl.naturalis.common.check.Check;
import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static nl.naturalis.common.check.CommonChecks.notNull;

;

/**
 * Compares {@link Check#notNull(Object)} to a manual null-check. Since this is probably the most
 * common precondition check, the {@code notNull} check ought to be just as fast as a manual check
 * ({@code if(arg == null) throw ...}).
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms1G", "-Xmx1G"})
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 3, time = 3)
public class NullCheck_1_Pct_Nulls {

  public String testVal;

  @Benchmark
  public void manual(Blackhole bh) {
    try {
      if (testVal == null) {
        throw new IllegalArgumentException("arg must not be null");
      }
      bh.consume(testVal);
    } catch (IllegalArgumentException e) {
      bh.consume(e);
    }
  }

  @Benchmark
  public void staticFactoryMethod(Blackhole bh) {
    try {
      bh.consume(Check.notNull(testVal, "arg").ok());
    } catch (IllegalArgumentException e) {
      bh.consume(e);
    }
  }

  @Benchmark
  public void notNullPlain(Blackhole bh) {
    try {
      bh.consume(Check.that(testVal, "arg").is(notNull()).ok());
    } catch (IllegalArgumentException e) {
      bh.consume(e);
    }
  }

  @Benchmark
  public void notNullCustomMsg(Blackhole bh) {
    try {
      bh.consume(Check.that(testVal).is(notNull(), "Not allowed: ${arg}").ok());
    } catch (IllegalArgumentException e) {
      bh.consume(e);
    }
  }

  @Benchmark
  public void notNullCustomExc(Blackhole bh) {
    try {
      bh.consume(Check.that(testVal).is(notNull(), () -> new IOException()).ok());
    } catch (IOException e) {
      bh.consume(e);
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
    if (i % 100 == 0) {
      testVal = null;
    } else {
      testVal = RandomStringUtils.randomAlphabetic(10, 15);
    }
  }

}
