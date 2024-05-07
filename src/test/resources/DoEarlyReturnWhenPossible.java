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

  /*
    `getReport` method from `InfComision` class. Found in 'Drivania' project. Testing rule's behaviour when the conditional 'else'
    statement to be analyzed is a single expression, instead of a block of code (enclosed in braces)
   */
  private String getReport(ConnectionProvider cp, VariablesSecureApp vars, String strDateFrom, String strDateTo,
      String strcBpartnerId, String strOrg, String strAgentId) throws Exception {
    String partner = (strcBpartnerId == null || strcBpartnerId.isEmpty()) ? null : strcBpartnerId;
    String agent = (strAgentId == null || strAgentId.isEmpty()) ? null : strAgentId;
    String org = (strOrg == null || strOrg.isEmpty()) ? null : strOrg;

    PrintInvoicesData[] data = null;
    if (null != agent) {
      return agent;
    } else {
      data = PrintInvoicesData.getAgentConPartner(cp, strDateFrom, strDateTo, partner, org);
    }
    if (0 != data.length) { // Noncompliant
      String result = "";
      for (int i = 0; i < data.length; i++) {
        result = result + data[i].agent + ",";
      }
      return result;
    } else
      throw new Exception("No hay datos.");
  }
}