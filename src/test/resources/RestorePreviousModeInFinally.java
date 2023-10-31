public class RestoreInFinally {

  public RestoreInFinally(RestoreInFinally restInFin) {
  }

  public test1() {
    try {
      OBContext.setAdminMode(true); // Compliant
    } catch (Exception e) {
      // Do something
    } finally {
      OBContext.restorePreviousMode();
    }
  }

  public test2() {
    try {
      OBContext.setAdminMode(true); // Noncompliant
      // Do something
    } catch (Exception e) {
      // Do something
    }
  }

  public test3() {
    OBContext.setAdminMode(true);
    try { // Compliant
      // Do something
    } catch (Exception e) {
      // Do something
    } finally {
      OBContext.restorePreviousMode();
    }
  }

  public test4() {
    try {
      OBContext.setAdminMode(true); // Noncompliant
    } catch (Exception e) {
      // Do something
    }
    OBContext.restorePreviousMode();
  }

  public test5() {
    try { // Compliant
      // Do something
    } catch (Exception e) {
      // Do something
    } finally {
      // Do something
    }
  }
}