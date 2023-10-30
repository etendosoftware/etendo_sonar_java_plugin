class EarlyReturnTest {
  public EarlyReturnTest(EarlyReturnTest earlyRetTest) {
  }

  public boolean isPositiveNoncomp(int number) {
    if (number > 0) { // Noncompliant
      return true;
    } else {
      return false;
    }
  }

  public boolean isPositiveComp(int number) {
    if (number <= 0) // Compliant
      return false;
    return true;
  }

  public boolean isNonEmptyString(String str) {
    if (str != null) {
      if (!str.trim().isEmpty()) { // Noncompliant
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  //////////////////////// TEST CASES FROM CORE CODE SNIPPETS //////////////////////

  /*
    `getValue` method from `OBProviderConfigReader` Etendo Core class. Testing rule's behaviour when the conditional
    to be analyzed is of the form 'if -> else if -> else if -> ...'
   */
  public String getValue(Element parentElem, String name, boolean mandatory) { // Compliant
    final Element valueElement = parentElem.element(name);
    if (mandatory) {
      Check.isNotNull(valueElement, "Element with name " + name + " not found");
    } else if (valueElement == null) {
      return null;
    }
    return valueElement.getText();
  }
}