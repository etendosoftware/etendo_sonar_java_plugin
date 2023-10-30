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

  //////////////////////// TEST CASES FROM CORE CODE SNIPPETS //////////////////////

  /* Test case found when running a sonar scan in Etendo Core environment.
  The rule threw an exception for this code on its first implementation,
  because it did not support multiple method calls.
   */
  public void testET001(@Observes EntityNewEvent event) {
    if (!isValidEvent(event)) {
      return;
    }
    ConnectionProvider conn = new DalConnectionProvider(false);
    String language = OBContext.getOBContext().getLanguage().getLanguage(); // Compliant - it's not a setAdminMode method call
    OrderDiscount orderdiscount = (OrderDiscount) event.getTargetInstance();
    if (orderdiscount.getSalesOrder().isProcessed()
        || orderdiscount.getSalesOrder().getPosted().equals("Y")) {
      throw new OBException(Utility.messageBD(conn, "20501", language));
    }
  }
}