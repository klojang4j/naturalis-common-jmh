Microbenchmarks for some critical parts of the naturalis-common utilities library.

### How to run

- Clone this repository
- Run: mvn clean package
- Run: java -jar target/benchmarks.jar <name_of_test>

For example:

java -jar target/benchmarks.jar CheckNotNull

### Tests

**nl.naturalis.common.check**

- **CheckNotNull** Compares the performance of Check.notNull(arg) and Check.that(arg).is(notNull()) to a manually coded
  null check. In the 1st round all 100000 test objects pass the test (they are non-null). In the 2nd and 3rd round 1
  resp. 10 percent of the test objects are null, thus causing an IllegalArgumentException to be thrown.
- **CheckLt** Compares performance of Check.that(arg).is(lt(), 100) to manually coded less-than check.


