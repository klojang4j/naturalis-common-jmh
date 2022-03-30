package nl.naturalis.common.jmh.check;

import nl.naturalis.common.check.Check;
import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.*;
;import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
public class NotNull_All_Pass {

  public int samples = 1024;

  public String testVal;

  @Benchmark
  public void manual(Blackhole bh) {
    if (testVal == null) {
      throw new IllegalArgumentException("arg must not be null");
    }
    bh.consume(testVal);
  }

  @Benchmark
  public void staticFactoryMethod(Blackhole bh) {
    bh.consume(Check.notNull(testVal, "arg").ok());
  }

  @Benchmark
  public void notNullPlain(Blackhole bh) {
    bh.consume(Check.that(testVal, "arg").is(notNull()).ok());
  }

  @Benchmark
  public void notNullCustomMsg(Blackhole bh) {
    bh.consume(Check.that(testVal).is(notNull(), "Not allowed: ${arg}").ok());
  }

  @Benchmark
  public void notNullCustomExc(Blackhole bh) throws IOException {
    bh.consume(Check.that(testVal)
        .is(notNull(), () -> new IOException("arg must not be null"))
        .ok());
  }

  @Setup(Level.Invocation)
  public void setup() {
    testVal = RandomStringUtils.randomAlphabetic(10, 15);
  }

}
