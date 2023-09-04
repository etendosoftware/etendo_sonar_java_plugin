public class TestStringBuilderWhenConcat {
  TestStringBuilderWhenConcat(TestStringBuilderWhenConcat testStrBWhenConc){
  }

  public void test1() {
    String str = "This is an example for a test " + // Noncompliant
        "showing a case where multiple Strings are " +
        "being concatenated in a single variable. " +
        "This specific test case is evaluating whether or " +
        "not the sonar rule created detects this kind of " +
        "String concatenation and reports an issue on it";

    // Do something
  }

  public void test2() { // Compliant
    StringBuilder str = new StringBuilder();
    str.append("This is an example for a test ");
    str.append("showing a case where multiple Strings are ");
    str.append("being concatenated in a single variable, but this time using the StringBuilder class. ");
    str.append("This specific test case is evaluating whether or ");
    str.append("not the sonar rule created detects this kind of ");
    str.append("String concatenation and reports an issue on it");
  }

  public void test3() { // Compliant
    String var1 = "where variables are used";
    String var2 = "to create a message";
    String var3 = "This should not trigger an issue";
    String str = "This is an example of a test " + var1 + " " + var2 + ". " + var3 + ".";

    // Do something
  }

  public void test3() {
    String var1 = "where variables are used";
    String var2 = "to create a message";
    String var3 = "This should not trigger an issue";
    String var4 = "THIS ONE VARIABLE TRIGGERS THE ISSUE";
    String str = "This is an example of a test " + var1 + " " + var2 + ". " + var3 + "." + var4; // Noncompliant

    // Do something
  }
}