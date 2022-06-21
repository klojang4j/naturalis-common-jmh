package nl.naturalis.common.jmh.check;

import nl.naturalis.common.check.Check;
import nl.naturalis.common.function.IntRelation;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static nl.naturalis.common.check.CommonChecks.lt;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms1G", "-Xmx1G", "-XX:-StackTraceInThrowable"})
@Warmup(iterations = 4, time = 3500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 3, time = 3500, timeUnit = TimeUnit.MILLISECONDS)
public class LessThan_1_Pct_Fail {

  public int subject;
  public int object;

  public Random rand = new Random();

  public static IntRelation anonymize(IntRelation intRelation) {
    return (i, j) -> intRelation.exists(i, j);
  }

  //@Benchmark
  public void handCoded(Blackhole bh) {
    try {
      if (subject < object) {
        bh.consume(subject);
      }
      throw new IllegalArgumentException("arg must be < "
          + object
          + " (was "
          + subject
          + ")");
    } catch (IllegalArgumentException t) {
    }
  }

  @Benchmark
  public void prefabMessage(Blackhole bh) {
    try {
      bh.consume(Check.that(subject).is(anonymize(lt()), object).ok());
    } catch (IllegalArgumentException e) {
    }
  }

  //@Benchmark
  public void customMessageWithMsgArgs(Blackhole bh) {
    try {
      bh.consume(Check.that(subject, "arg")
          .is(lt(), object, "${name} must be < ${obj} (was ${arg})")
          .ok());
    } catch (IllegalArgumentException e) {
    }
  }

  //@Benchmark
  public void customMessageNoMsgArgs(Blackhole bh) {
    try {
      bh.consume(Check.that(subject)
          .is(lt(),
              object,
              "arg must be < " + object + " (was " + subject + ")",
              null)
          .ok());
    } catch (IllegalArgumentException e) {
    }
  }

  //@Benchmark
  public void customException(Blackhole bh) {
    try {
      bh.consume(Check.that(subject)
          .is(lt(),
              object,
              () -> new IOException("arg must be < "
                  + object
                  + " (was "
                  + subject
                  + ")"))
          .ok());
    } catch (IOException e) {
    }
  }

  public ThreadLocal<AtomicInteger> counter = new ThreadLocal<>() {
    @Override
    protected AtomicInteger initialValue() {
      return new AtomicInteger();
    }
  };

  @Setup(Level.Invocation)
  public void setup() {
    subject = rand.nextInt(-1000_001, 1000_001);
    int i = rand.nextInt(-1001, 1001);
    int j = counter.get().incrementAndGet();
    if (j % 100 == 0) {
      if (i < 0) {
        object = subject + Math.abs(i);
      } else {
        object = subject + i + 3;
      }
    } else {
      if (i < 0) {
        object = subject - Math.abs(i);
      } else {
        object = subject - i + 4;
      }
    }
  }

}
