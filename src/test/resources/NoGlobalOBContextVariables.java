class Test1 {
  private static final Logger log = Logger.getLogger(Test1.class);
  private static final String TEST_STRING = "This is a test";
  private static Client testClient = OBContext.getOBContext().getCurrentClient(); // Noncompliant

  // Do something
}

class Test2 {
  private static final Logger log = Logger.getLogger(Test2.class);
  @Override
  protected void doExecute(ProcessBundle bundle) throws Exception {
    private static Client testClient = OBContext.getOBContext().getCurrentClient(); // Compliant

    // Do something
  }
}

class Test3 {
  private static final String TEST_STRING = "This is a test";
  private static final int TEST_INT = 0000;
  private static final boolean TEST_BOOLEAN = true;

  // Do something
}

class Test4 {
  private static Client testClient = OBContext.getOBContext().getCurrentClient(); // Noncompliant;

  @Override
  protected void doExecute(ProcessBundle bundle) throws Exception {
    // Do something
  }
}