package nl.naturalis.common.jmh.collection;

public class TypeMap_Get {

  //@formatter:off
  public interface Interface_0 {}
  public interface Interface_1 {}
  public interface Interface_2 {}
  public interface Interface_3 {}

  public interface Interface_1_0 extends Interface_1 {}
  public interface Interface_2_0 extends Interface_2 {}
  public interface Interface_2_1 extends Interface_2 {}
  public interface Interface_3_0 extends Interface_3 {}
  public interface Interface_3_1 extends Interface_3 {}
  public interface Interface_3_2 extends Interface_3 {}

  public interface Interface_2_0_0 extends Interface_2_0 {}
  public interface Interface_2_0_1 extends Interface_2_0 {}
  public interface Interface_2_1_0 extends Interface_2_1 {}
  public interface Interface_2_1_1 extends Interface_2_1 {}

  public interface Interface_3_0_0 extends Interface_3_0 {}
  public interface Interface_3_0_1 extends Interface_3_0 {}
  public interface Interface_3_0_2 extends Interface_3_0 {}
  public interface Interface_3_1_0 extends Interface_3_1 {}
  public interface Interface_3_1_1 extends Interface_3_1 {}
  public interface Interface_3_1_2 extends Interface_3_1 {}

  public interface Interface_3_0_0_0 extends Interface_3_0_0 {}
  public interface Interface_3_0_0_1 extends Interface_3_0_0 {}
  public interface Interface_3_0_0_2 extends Interface_3_0_0 {}
  public interface Interface_3_0_0_3 extends Interface_3_0_0 {}

  public class Class_0 {}
  public class Class_1 {}
  public class Class_2 {}
  public class Class_3 implements Interface_3 {}
  public class Class_4 implements Interface_2, Interface_3_1 {}

  public class Class_0_0 extends Class_0 {}
  public class Class_0_1 extends Class_0 implements Interface_3 {}
  public class Class_0_2 extends Class_0 implements Interface_3, Interface_2_0 {}
  public class Class_0_3 extends Class_0 implements Interface_2_0_0 {}

  public class Class_1_0 extends Class_1 {}
  public class Class_1_1 extends Class_1 implements Interface_2_0_0 {}
  public class Class_1_2 extends Class_1 implements Interface_3_2, Interface_2_0 {}
  public class Class_1_3 extends Class_1 implements Interface_3_2, Interface_2_0 {}
  public class Class_1_4 extends Class_1 implements Interface_2_0_0 {}

  public class Class_2_0 extends Class_2 {}
  public class Class_2_1 extends Class_2 {}
  public class Class_2_2 extends Class_2 {}
  public class Class_2_3 extends Class_2 implements Interface_1_0, Interface_2_0 {}
  public class Class_2_4 extends Class_2 implements Interface_2_1 {}

  public class Class_2_0_0 extends Class_2_0 {}
  public class Class_2_0_1 extends Class_2_0 {}
  public class Class_2_0_2 extends Class_2_0 implements Interface_2 {}


  //@formatter:on

  public static void main(String[] args) {
    Class[] members = TypeMap_Get.class.getClasses();
    for (Class c : members) {
      System.out.println(c.getSimpleName());
    }
  }

}
