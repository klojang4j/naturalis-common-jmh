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
  compares the performance of the check framework's null check with a hand-coded
  null check. The test iterations are carried out with a value that is never
  null.
- [NotNull_1_Pct_Fail](/src/main/java/nl/naturalis/common/jmh/check/NotNull_1_Pct_Fail.java)
  compares the performance of the check framework's null check with a hand-coded
  null check. The test iterations are carried out with a value that is null in
  one percent of the cases.
- [NotNull_50_Pct_Fail](/src/main/java/nl/naturalis/common/jmh/check/NotNull_50_Pct_Fail.java)
  compares the performance of the check framework's null check with a hand-coded
  null check. The test iterations are carried out with a value that is null in
  50 percent of the cases.

**nl.naturalis.common.invoke**

- [ReaderComparison](/src/main/java/nl/naturalis/common/jmh/invoke/ReaderComparison.java)
  compares the performance of BeanReader and AnyBeanReader with non-dynamical
  reads on a simple Person object with three properties.
