package nl.naturalis.common.jmh.check;

import nl.naturalis.common.check.Check;
import nl.naturalis.common.util.MutableInt;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;

import static nl.naturalis.common.check.CommonChecks.lt;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(
    value = 2,
    jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3)
@Measurement(iterations = 6)
public class CheckLt {

  @Param({"10000"})
  private int sampleSize = 10000;

  public int[] noFailures = new int[sampleSize];
  public int[] onePercentFailures = new int[sampleSize];
  public int[] tenPercentFailures = new int[sampleSize];

  @Benchmark // Using Check.notNull
  public void t00_check___all_pass(Blackhole bh) {
    for (int i = 0; i < sampleSize; ++i) {
      try {
        int x = Check.that(noFailures[i], "foo").is(lt(), 100).intValue();
        bh.consume(x);
      } catch (IllegalArgumentException e) {
        bh.consume(e.getMessage());
      }
    }
  }

  @Benchmark // Manual check
  public void t01_manual___all_pass(Blackhole bh) {
    for (int i = 0; i < sampleSize; ++i) {
      try {
        int x = noFailures[i];
        if (x < 100) {
          bh.consume(x);
        } else {
          throw new IllegalArgumentException("foo must be < 100 (was " + x + ")");
        }
      } catch (IllegalArgumentException e) {
        bh.consume(e.getMessage());
      }
    }
  }

  @Benchmark
  public void t02_check___1pct_failures(Blackhole bh) {
    for (int i = 0; i < sampleSize; ++i) {
      try {
        int x = Check.that(onePercentFailures[i], "foo").is(lt(), 100).intValue();
        bh.consume(x);
      } catch (IllegalArgumentException e) {
        bh.consume(e.getMessage());
      }
    }
  }

  @Benchmark
  public void t03_manual___1pct_failures(Blackhole bh) {
    for (int i = 0; i < sampleSize; ++i) {
      try {
        int x = onePercentFailures[i];
        if (x < 100) {
          bh.consume(x);
        } else {
          throw new IllegalArgumentException("foo must be < 100 (was " + x + ")");
        }
      } catch (IllegalArgumentException e) {
        bh.consume(e.getMessage());
      }
    }
  }

  @Benchmark
  public void t04_check___10pct_failures(Blackhole bh) {
    for (int i = 0; i < sampleSize; ++i) {
      try {
        int x = Check.that(tenPercentFailures[i], "foo").is(lt(), 100).intValue();
        bh.consume(x);
      } catch (IllegalArgumentException e) {
        bh.consume(e.getMessage());
      }
    }
  }

  @Benchmark
  public void t05_manual___10pct_failures(Blackhole bh) {
    for (int i = 0; i < sampleSize; ++i) {
      try {
        int x = tenPercentFailures[i];
        if (x < 100) {
          bh.consume(x);
        } else {
          throw new IllegalArgumentException("foo must be < 100 (was " + x + ")");
        }
      } catch (IllegalArgumentException e) {
        bh.consume(e.getMessage());
      }
    }
  }

  @Setup
  public void setup() {
    createTestData();
  }

  public void createTestData() {
    int int87 = 87;
    int int103 = 103;
    RandomGenerator g = RandomGenerator.of("L64X128MixRandom");
    MutableInt mi = new MutableInt();
    g.ints(sampleSize, 0, 200)
        .forEach(
            // Lots of daft code to confuse the compiler
            i -> {
              if (i < 100) {
                noFailures[mi.get()] = i % 3 == 0 ? i - 2 : int87;
              } else {
                noFailures[mi.get()] = i - 50 < 100 ? int87 : i - 201;
              }
              if (i % 100 == 1) {
                onePercentFailures[mi.get()] = i > 100 ? i * i : int103 + noFailures[mi.get()] * 2;
              } else {
                onePercentFailures[mi.get()] = i > 100 ? i - 1000 : int87;
              }
              if (i % 10 == 0) {
                tenPercentFailures[mi.get()] = i > 100 ? i + noFailures[mi.get()] : int103;
              } else {
                tenPercentFailures[mi.get()] = i < 100 ? i - 13 : noFailures[mi.get()] - 7;
              }
              mi.pp();
            });
  }
}
