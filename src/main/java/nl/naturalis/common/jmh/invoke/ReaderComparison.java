package nl.naturalis.common.jmh.invoke;

import nl.naturalis.common.invoke.AnyBeanReader;
import nl.naturalis.common.invoke.BeanReader;
import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(
    value = 2,
    jvmArgs = {"-Xms512M", "-Xmx512M"})
@Warmup(iterations = 3)
@Measurement(iterations = 3)
public class ReaderComparison {

  public static void main(String[] args) {
    List<Person> persons = createTestData();
    BeanReader<Person> br = new BeanReader<>(Person.class);
    for (Person p : persons) {
      System.out.println(br.read(p, "id") + " +++ " + br.read(p, "name") + " +++ " + br.read(p,
          "birthDate"));
    }
  }

  public static class Person {
    private int id;
    private String name;
    private LocalDate birthDate;

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public LocalDate getBirthDate() {
      return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
      this.birthDate = birthDate;
    }
  }

  private List<Person> persons;
  private BeanReader<Person> br = new BeanReader<>(Person.class);
  private AnyBeanReader abr = new AnyBeanReader();

  @Benchmark
  public void nonDynamic(Blackhole bh) {
    for (Person p : persons) {
      bh.consume(p.getId());
      bh.consume(p.getName());
      bh.consume(p.getBirthDate());
    }
  }

  @Benchmark
  public void beanReader(Blackhole bh) {
    for (Person p : persons) {
      bh.consume(br.read(p, "id"));
      bh.consume(br.read(p, "name"));
      bh.consume(br.read(p, "birthDate"));
    }
  }

  @Benchmark
  public void anyBeanReader(Blackhole bh) {
    for (Person p : persons) {
      bh.consume(abr.read(p, "id"));
      bh.consume(abr.read(p, "name"));
      bh.consume(abr.read(p, "birthDate"));
    }
  }

  @Setup
  public void setup() {
    persons = createTestData();
  }

  private static List<Person> createTestData() {
    Random rand = new Random();
    List<Person> persons = new ArrayList<>(1024);
    for (int i = 0; i < 1024; ++i) {
      Person p = new Person();
      p.setId(rand.nextInt(1, Integer.MAX_VALUE));
      p.setName(RandomStringUtils.randomAlphabetic(5, 10));
      p.setBirthDate(LocalDate.of(rand.nextInt(1930, 2020),
          rand.nextInt(1, 13),
          rand.nextInt(1, 29)));
      persons.add(p);
    }
    return persons;
  }

}
