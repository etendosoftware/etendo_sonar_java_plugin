public class TestAdminModeInTry {

  public TestAdminModeInTry(TestAdminModeInTry TestAdminInTry) {
  }

  public test1() {
    OBContext.setAdminMode(true); // Noncompliant
  }

  public test2() {
    try {
      OBContext.setAdminMode(true); // Compliant
      // Do something
    } catch (Exception e) {
      // Do something
    }
  }

  public test3() {
    OBContext.setAdminMode(true); // Noncompliant
    try {
      // Do something
    } catch (Exception e) {
      // Do something
    }
  }
}