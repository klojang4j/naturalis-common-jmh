package nl.naturalis.common.jmh.check;

import nl.naturalis.common.check.Check;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.random.RandomGenerator;

/**
 * Compares {@link Check#notNull(Object)} to a manual null-check. Since this is probably the most
 * common precondition check, the {@code notNull} check ought to be just as fast as the manual
 * check. We expect this to be the case if the argument passes the test. If not we expect the {@code
 * notNull} to be slower, because the generation of the error message is more elaborate. But that's
 * OK ... who wants to continue after this?
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(
    value = 2,
    jvmArgs = {"-Xms2G", "-Xmx2G"})
// @Warmup(iterations = 3)
// @Measurement(iterations = 8)
public class CheckNotNull {

  @Param({"10000"})
  private int sampleSize;

  public List<Object> zeroNullsInThisList = new ArrayList<>(sampleSize);
  public List<Object> onePercentNullsInThisList = new ArrayList<>(sampleSize);
  public List<Object> tenPercentNullsInThisList = new ArrayList<>(sampleSize);

  @Benchmark
  public void t00_checkNotNull___all_pass(Blackhole bh) {
    for (int i = 0; i < sampleSize; ++i) {
      try {
        Object obj = Check.notNull(zeroNullsInThisList.get(i), "foo").ok();
        bh.consume(obj);
      } catch (IllegalArgumentException e) {
        bh.consume(e.getMessage());
      }
    }
  }

  @Benchmark
  public void t01_manualNullCheck___all_pass(Blackhole bh) {
    for (int i = 0; i < sampleSize; ++i) {
      try {
        Object obj = zeroNullsInThisList.get(i);
        if (obj == null) {
          throw new IllegalArgumentException("foo must not be null");
        }
        bh.consume(obj);
      } catch (IllegalArgumentException e) {
        bh.consume(e.getMessage());
      }
    }
  }

  @Benchmark
  public void t02_checkNotNull___1pct_fail(Blackhole bh) {
    for (int i = 0; i < sampleSize; ++i) {
      try {
        Object obj = Check.notNull(onePercentNullsInThisList.get(i), "foo").ok();
        bh.consume(obj);
      } catch (IllegalArgumentException e) {
        bh.consume(e.getMessage());
      }
    }
  }

  @Benchmark
  public void t03_manualNullCheck___1pct_fail(Blackhole bh) {
    for (int i = 0; i < sampleSize; ++i) {
      try {
        Object obj = onePercentNullsInThisList.get(i);
        if (obj == null) {
          throw new IllegalArgumentException("foo must not be null");
        }
        bh.consume(obj);
      } catch (IllegalArgumentException e) {
        bh.consume(e.getMessage());
      }
    }
  }

  @Benchmark
  public void t04_checkNotNull___10pct_fail(Blackhole bh) {
    for (int i = 0; i < sampleSize; ++i) {
      try {
        Object obj = Check.notNull(tenPercentNullsInThisList.get(i), "foo").ok();
        bh.consume(obj);
      } catch (IllegalArgumentException e) {
        bh.consume(e.getMessage());
      }
    }
  }

  @Benchmark
  public void t05_manualNullCheck___10pct_null(Blackhole bh) {
    for (int i = 0; i < sampleSize; ++i) {
      try {
        Object obj = tenPercentNullsInThisList.get(i);
        if (obj == null) {
          throw new IllegalArgumentException("foo must not be null");
        }
        bh.consume(obj);
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
    RandomGenerator g = RandomGenerator.of("L64X128MixRandom");
    AtomicReference<Object> obj0 = new AtomicReference<>(new Object());
    AtomicReference<Object> obj1 = new AtomicReference<>(new Object());
    g.ints(sampleSize, 0, 100)
        .forEach(
            i -> {
              // Lots of really daft code to confuse the compiler
              if (i % 233 == 0) {
                obj0.setPlain(null);
                obj1.setPlain(new Object());
              } else if (i % 577 == 0) {
                obj0.setPlain(new Object());
                obj1.setPlain(null);
              }
              zeroNullsInThisList.add(
                  obj0.getPlain() == null
                      ? obj1.getPlain() == null ? new Object() : obj1.getPlain()
                      : obj0.getPlain());
              if (i % 100 == 0) {
                onePercentNullsInThisList.add(null);
              } else {
                onePercentNullsInThisList.add(
                    obj0.getPlain() == null
                        ? obj1.getPlain() == null ? new Object() : obj1.getPlain()
                        : obj0.getPlain());
              }
              if (i % 10 == 0) {
                tenPercentNullsInThisList.add(null);
              } else {
                tenPercentNullsInThisList.add(
                    obj0.getPlain() == null
                        ? obj1.getPlain() == null ? new Object() : obj1.getPlain()
                        : obj0.getPlain());
              }
            });
  }
}
