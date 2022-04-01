package nl.naturalis.common.jmh.check;

import nl.naturalis.common.check.Check;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static nl.naturalis.common.check.CommonChecks.instanceOf;

;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms1G", "-Xmx1G"})
@Warmup(iterations = 4, time = 3)
@Measurement(iterations = 3, time = 3500, timeUnit = TimeUnit.MILLISECONDS)
public class InstanceOf_All_Pass {

  public Object testVal;
  public Class testClass;

  @Benchmark
  public void manual(Blackhole bh) {
    if (testClass.isInstance(testVal)) {
      String fmt = "value must be instance of collection (was %s)";
      String msg = String.format(fmt, testVal.getClass().getName());
      throw new IllegalArgumentException(msg);
    }
    bh.consume(testVal);
  }

  @Benchmark
  public void instanceOfPlain(Blackhole bh) {
    bh.consume(Check.that(testVal, "value").is(instanceOf(), testClass).ok());
  }

  @Benchmark
  public void notNullCustomMsg(Blackhole bh) {
    bh.consume(Check.that(testVal).is(instanceOf(), testClass, "Illegal type: ${type}").ok());
  }

  @Benchmark
  public void notNullCustomExc(Blackhole bh) throws IOException {
    bh.consume(Check.that(testVal)
        .is(instanceOf(),
            testClass,
            () -> new IOException("Illegal type: " + testVal.getClass().getName()))
        .ok());
  }

  public ThreadLocal<AtomicInteger> counter0 = new ThreadLocal<>() {
    @Override
    protected AtomicInteger initialValue() {
      return new AtomicInteger();
    }
  };

  public ThreadLocal<AtomicInteger> counter1 = new ThreadLocal<>() {
    @Override
    protected AtomicInteger initialValue() {
      return new AtomicInteger();
    }
  };

  @Setup(Level.Invocation)
  public void setup() {
    // Ridiculous code to confuse the compiler as much as possible
    int i0 = counter0.get().getAndIncrement();
    testVal = switch (i0 % 3) {
      case 0 -> new ArrayList<>();
      case 1 -> new HashSet<>();
      case 2 -> Collections.singleton("FOO");
      default -> Set.of(1, 2);
    };
    int i1 = counter1.get().getAndIncrement();
    testClass = switch (i1 % 2) {
      case 0 -> i0 % 3 == 0 ? List.class : Set.class;
      case 1 -> i0 % 3 == 1 ? Serializable.class : Iterable.class;
      default -> Collection.class;
    };
  }

}
