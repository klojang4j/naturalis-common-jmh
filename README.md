Performance tests some critical parts of the naturalis-common utilities library.

### How to run

- Clone this repository
- Run: mvn clean package
- Run: java -jar target/benchmarks.jar <name_of_test>

For example:

java -jar target/benchmarks.jar CheckNotNull

### CheckNotNull

Compares performance (average time) of Check.notNull(arg) and Check.that(arg).is(notNull()) to manually coded null
check. In the 1st pass all tested objects are in fact non-null. In the 2nd and 3rd pass 1 resp. 10 percent are null,
thus causing an IllegalArgumentException to be thrown.

```
Benchmark                                        (sampleSize)  Mode  Cnt   Score    Error  Units
CheckNotNull.t00_all_pass__CheckNotNull                100000  avgt   16   0.162 ±  0.002  ms/op
CheckNotNull.t02_all_pass__CheckThatIsNotNull          100000  avgt   16   0.161 ±  0.001  ms/op
CheckNotNull.t03_all_pass__manual                      100000  avgt   16   0.162 ±  0.002  ms/op
CheckNotNull.t04_99pct_pass__CheckNotNull              100000  avgt   16   1.891 ±  0.009  ms/op
CheckNotNull.t05_99pct_pass__CheckThatIsNotNull        100000  avgt   16   1.854 ±  0.014  ms/op
CheckNotNull.t06_99pct_pass__manual                    100000  avgt   16   1.565 ±  0.027  ms/op
CheckNotNull.t07_90pct_pass__CheckNotNull              100000  avgt   16  16.389 ±  0.272  ms/op
CheckNotNull.t08_90pct_pass__CheckThatIsNotNull        100000  avgt   16  16.229 ±  0.181  ms/op
CheckNotNull.t09_90pct_pass__manual                    100000  avgt   16  14.584 ±  0.431  ms/op
```

### CheckLt

Compares performance of Check.that(arg).is(lt(), 100) to manually coded less-than check.

