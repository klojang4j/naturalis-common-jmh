package nl.naturalis.common.jmh.check;

import nl.naturalis.common.check.Check;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

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
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(
    value = 2,
    jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3)
@Measurement(iterations = 8)
public class CheckNotNull {

  @Param({"100000"})
  private static int sampleSize = 100000;

  public List<Object> zeroNullsInThisList = new ArrayList<>(sampleSize);
  public List<Object> onePercentNullsInThisList = new ArrayList<>(sampleSize);
  public List<Object> tenPercentNullsInThisList = new ArrayList<>(sampleSize);

  @Benchmark // Using Check.notNull
  public void t00_all_pass__CheckNotNull(Blackhole bh) {
    check1(zeroNullsInThisList, bh);
  }

  @Benchmark // Using Check.that(arg).id(notNull());
  public void t02_all_pass__CheckThatIsNotNull(Blackhole bh) {
    check2(zeroNullsInThisList, bh);
  }

  @Benchmark // Manual coding of the same check
  public void t03_all_pass__manual(Blackhole bh) {
    manual(zeroNullsInThisList, bh);
  }

  @Benchmark
  public void t04_99pct_pass__CheckNotNull(Blackhole bh) {
    check1(onePercentNullsInThisList, bh);
  }

  @Benchmark
  public void t05_99pct_pass__CheckThatIsNotNull(Blackhole bh) {
    check1(onePercentNullsInThisList, bh);
  }

  @Benchmark
  public void t06_99pct_pass__manual(Blackhole bh) {
    manual(onePercentNullsInThisList, bh);
  }

  @Benchmark
  public void t07_90pct_pass__CheckNotNull(Blackhole bh) {
    check1(tenPercentNullsInThisList, bh);
  }

  @Benchmark
  public void t08_90pct_pass__CheckThatIsNotNull(Blackhole bh) {
    check1(tenPercentNullsInThisList, bh);
  }

  @Benchmark
  public void t09_90pct_pass__manual(Blackhole bh) {
    manual(tenPercentNullsInThisList, bh);
  }

  private static void check1(List<Object> objs, Blackhole bh) {
    for (int i = 0; i < sampleSize; ++i) {
      try {
        Object obj = Check.notNull(objs.get(i), "foo").ok();
        bh.consume(obj);
      } catch (IllegalArgumentException e) {
        bh.consume(e.getMessage());
      }
    }
  }

  private static void check2(List<Object> objs, Blackhole bh) {
    for (int i = 0; i < sampleSize; ++i) {
      try {
        Object obj = Check.that(objs.get(i), "foo").is(notNull()).ok();
        bh.consume(obj);
      } catch (IllegalArgumentException e) {
        bh.consume(e.getMessage());
      }
    }
  }

  private static void manual(List<Object> objs, Blackhole bh) {
    for (int i = 0; i < sampleSize; ++i) {
      try {
        Object obj = objs.get(i);
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
              // Lots of silly code to confuse the compiler
              if (i % 233 == 0) {
                obj0.setPlain(null);
                obj1.setPlain(new Object());
              } else if (i % 577 == 0) {
                obj0.setPlain(new Object());
                obj1.setPlain(null);
              }
              zeroNullsInThisList.add(
                  obj0.getPlain() == null
                      ? obj1.getPlain() == null ? Collections.emptyList() : obj1.getPlain()
                      : obj0.getPlain() == null ? Collections.emptyList() : Collections.emptySet());
              if (i % 100 == 0) {
                onePercentNullsInThisList.add(null);
              } else {
                onePercentNullsInThisList.add(
                    obj0.getPlain() == null
                        ? obj1.getPlain() == null ? Collections.emptyList() : obj1.getPlain()
                        : obj0.getPlain() == null ? List.of() : obj0.getPlain());
              }
              if (i % 10 == 0) {
                tenPercentNullsInThisList.add(null);
              } else {
                Object x = obj1.getPlain() == null ? DayOfWeek.MONDAY : obj1.getPlain();
                Object y = obj0.getPlain() == null ? x : BigDecimal.ONE;
                tenPercentNullsInThisList.add(y);
              }
            });
  }
}
