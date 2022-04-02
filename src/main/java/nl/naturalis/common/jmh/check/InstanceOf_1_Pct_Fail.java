package nl.naturalis.common.jmh.check;

import nl.naturalis.common.check.Check;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static nl.naturalis.common.check.CommonChecks.instanceOf;

;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms1G", "-Xmx1G", "-XX:-StackTraceInThrowable"})
@Warmup(iterations = 4, time = 3)
@Measurement(iterations = 3, time = 3500, timeUnit = TimeUnit.MILLISECONDS)
public class InstanceOf_1_Pct_Fail {

  public Object testVal;
  public Class testClass;

  @Benchmark
  public void handCoded(Blackhole bh) {
    try {
      if (testClass.isInstance(testVal)) {
        throw new IllegalArgumentException(testVal.getClass() + " must be a " + testClass);
      }
      bh.consume(testVal);
    } catch (IllegalArgumentException e) {
    }
  }

  @Benchmark
  public void prefabMessage(Blackhole bh) {
    try {
      bh.consume(Check.that(testVal, "value").is(instanceOf(), testClass).ok());
    } catch (IllegalArgumentException e) {
    }
  }

  @Benchmark
  public void customMessageWithMsgArgs(Blackhole bh) {
    try {
      bh.consume(Check.that(testVal).is(instanceOf(), testClass, "${type} must be ${obj}").ok());
    } catch (IllegalArgumentException e) {
    }
  }

  @Benchmark
  public void customMessageNoMsgArgs(Blackhole bh) {
    try {
      bh.consume(Check.that(testVal)
          .is(instanceOf(), testClass, testVal.getClass() + " must be a " + testClass)
          .ok());
    } catch (IllegalArgumentException e) {
    }
  }

  @Benchmark
  public void customException(Blackhole bh) {
    try {
      bh.consume(Check.that(testVal)
          .is(instanceOf(),
              testClass,
              () -> new IOException(testVal.getClass() + " must be a " + testClass))
          .ok());
    } catch (IOException e) {
    }
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
    if (i1 % 100 == 0) {
      testClass = OutputStream.class;
    } else {
      testClass = Collection.class;
    }
  }

}
