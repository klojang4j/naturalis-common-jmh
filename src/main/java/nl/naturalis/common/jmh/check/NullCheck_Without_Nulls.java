package nl.naturalis.common.jmh.check;

import nl.naturalis.common.check.Check;
import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.*;
;import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.random.RandomGenerator;

import static nl.naturalis.common.check.CommonChecks.notNull;

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
public class NullCheck_Without_Nulls {

  public int samples = 1024;

  public String[] strings;

  @Benchmark
  public void manual(Blackhole bh) {
    for (int i = 0; i < samples; ++i) {
      if (strings[i] == null) {
        throw new IllegalArgumentException("arg must not be null");
      }
      bh.consume(strings[i]);
    }
  }

  @Benchmark
  public void staticFactoryMethod(Blackhole bh) {
    for (int i = 0; i < samples; ++i) {
      bh.consume(Check.notNull(strings[i], "arg").ok());
    }
  }

  @Benchmark
  public void notNullPlain(Blackhole bh) {
    for (int i = 0; i < samples; ++i) {
      bh.consume(Check.that(strings[i], "arg").is(notNull()).ok());
    }
  }

  @Benchmark
  public void notNullCustomMsg(Blackhole bh) {
    for (int i = 0; i < samples; ++i) {
      bh.consume(Check.that(strings[i]).is(notNull(), "Not allowed: ${arg}").ok());
    }
  }

  @Benchmark
  public void notNullCustomExc(Blackhole bh) throws IOException {
    for (int i = 0; i < samples; ++i) {
      bh.consume(Check.that(strings[i]).is(notNull(), () -> new IOException()).ok());
    }
  }

  @Setup(Level.Iteration)
  public void setup() {
    strings = new String[samples];
    for (int i = 0; i < samples; ++i) {
      strings[i] = RandomStringUtils.randomAlphabetic(0, 10);
    }
  }

}
