package nl.naturalis.common.jmh.check;

import nl.naturalis.common.check.Check;
import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.*;
;import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static nl.naturalis.common.check.CommonChecks.notNull;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms1G", "-Xmx1G", "-XX:-StackTraceInThrowable"})
@Warmup(iterations = 4, time = 3)
@Measurement(iterations = 3, time = 3500, timeUnit = TimeUnit.MILLISECONDS)
public class NotNull_All_Pass {

  public String testVal;

  @Benchmark
  public void handCoded(Blackhole bh) {
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
  public void prefabMessage(Blackhole bh) {
    bh.consume(Check.that(testVal, "arg").is(notNull()).ok());
  }

  @Benchmark
  public void customMessage(Blackhole bh) {
    bh.consume(Check.that(testVal).is(notNull(), "Not allowed: ${arg}").ok());
  }

  @Benchmark
  public void customException(Blackhole bh) throws IOException {
    bh.consume(Check.that(testVal)
        .is(notNull(), () -> new IOException("arg must not be null"))
        .ok());
  }

  @Setup(Level.Invocation)
  public void setup() {
    testVal = RandomStringUtils.randomAlphabetic(10, 15);
  }

}
