Microbenchmarks using JMH for some critical parts of the naturalis-common.

### How to run

- Clone this repository
- Run: mvn clean package
- Run: java -jar target/benchmarks.jar <name_of_test>

For example:

java -jar target/benchmarks.jar NotNull_All_Pass

### Tests

**nl.naturalis.common.check**

- [NotNull_All_Pass](/src/main/java/nl/naturalis/common/jmh/check/NotNull_All_Pass.java)
  compares the performance of the check framework's notNull() check with a
  hand-coded null check. The test iterations are carried out with a value that
  is always non-null.
- [NotNull_1_Pct_Fail](/src/main/java/nl/naturalis/common/jmh/check/NotNull_1_Pct_Fail.java)
  compares the performance of the check framework's notNull() check with a
  hand-coded null check. The test iterations are carried out with a value that
  is null in one percent of the cases.
- [NotNull_50_Pct_Fail](/src/main/java/nl/naturalis/common/jmh/check/NotNull_50_Pct_Fail.java)
  compares the performance of the check framework's notNull() check with a
  hand-coded null check. The test iterations are carried out with a value that
  is null in 50 percent of the cases.
- [InstanceOf_All_Pass](/src/main/java/nl/naturalis/common/jmh/check/InstanceOf_All_Pass.java)
  compares the performance of the check framework's instanceOf() check with a
  hand-coded type check. The test iterations are carried out with a value that
  always has the expected type.

**nl.naturalis.common.invoke**

- [ReaderComparison](/src/main/java/nl/naturalis/common/jmh/invoke/ReaderComparison.java)
  compares the performance of BeanReader and AnyBeanReader with non-dynamical
  reads on a simple Person object with three properties.

### Results

N.B. All tests were carried out on a Dell Latitude laptop with a Pentium i5
octo-core and 16 GB of RAM.

**nl.naturalis.common.check**

**NotNull_All_Pass**

```
Benchmark                             Mode  Cnt   Score   Error  Units
NotNull_All_Pass.manual               avgt    6  13.382 ± 0.199  ns/op
NotNull_All_Pass.notNullCustomExc     avgt    6  13.236 ± 0.112  ns/op
NotNull_All_Pass.notNullCustomMsg     avgt    6  13.263 ± 0.115  ns/op
NotNull_All_Pass.notNullPlain         avgt    6  13.325 ± 0.129  ns/op
NotNull_All_Pass.staticFactoryMethod  avgt    6  13.271 ± 0.184  ns/op
```

**NotNull_1_Pct_Fail**

```
Benchmark                               Mode  Cnt   Score   Error  Units
NotNull_1_Pct_Fail.manual               avgt    6  20.155 ± 0.401  ns/op
NotNull_1_Pct_Fail.notNullCustomExc     avgt    6  20.321 ± 0.382  ns/op
NotNull_1_Pct_Fail.notNullCustomMsg     avgt    6  22.889 ± 1.118  ns/op
NotNull_1_Pct_Fail.notNullPlain         avgt    6  22.804 ± 0.590  ns/op
NotNull_1_Pct_Fail.staticFactoryMethod  avgt    6  22.798 ± 0.415  ns/op
```

**NotNull_50_Pct_Fail**

```
Benchmark                                Mode  Cnt    Score    Error  Units
NotNull_50_Pct_Fail.manual               avgt    6  350.095 ±  8.751  ns/op
NotNull_50_Pct_Fail.notNullCustomExc     avgt    6  363.533 ±  2.848  ns/op
NotNull_50_Pct_Fail.notNullCustomMsg     avgt    6  428.559 ±  5.337  ns/op
NotNull_50_Pct_Fail.notNullPlain         avgt    6  520.175 ± 31.411  ns/op
NotNull_50_Pct_Fail.staticFactoryMethod  avgt    6  356.488 ± 13.843  ns/op
```

**InstanceOf_All_Pass**

```
Benchmark                             Mode  Cnt   Score   Error  Units
InstanceOf_All_Pass.instanceOfPlain   avgt    6  31.441 ± 0.945  ns/op
InstanceOf_All_Pass.notNullCustomExc  avgt    6  31.459 ± 0.756  ns/op
InstanceOf_All_Pass.notNullCustomMsg  avgt    6  31.038 ± 0.968  ns/op
```

