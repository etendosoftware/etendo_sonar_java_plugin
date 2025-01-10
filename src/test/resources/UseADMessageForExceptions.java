import org.openbravo.utils.OBMessageUtils;
import java.text.ParseException;

public class UseADMessageForExceptions {
  public UseADMessageForExceptions(UseADMessageForExceptions useADMessageForExceptions) {
  }

  public test1() {
    throw new IllegalStateException(String.format( // Noncompliant - It's a String literal
        "Wrong selector definition in parameter %s of standard process %s, missing @additional_filters@ clause: %s",
        parameter.getName(), parameter.getObuiappProcess().getName()), e);
    //^[sc=1;el=+2;ec=75]@-2
  }

  public test2() {
    throw new ParseException("Error creating instance"); // Noncompliant - It's a String literal
  }

  public test3() {
    throw new OBException("Error creating instance"); // Noncompliant - It's a String literal
  }

  public test4() {
    throw new OBException(
        String.format(Utility.messageBD(conn, "ETFRA_ErrorGettingBusinessProduct", language), e)); // Compliant
  }

  public test5() {
    throw new IllegalArgumentException(
        OBMessageUtils.messageBD("SMFSWS_InvalidToken")); // Compliant
  }

  public test6() {
    String message = "Error creating instance";
    throw new OBException(message); // Noncompliant - It's a String variable
  }

  public test7() {
    throw new Exception(); // Compliant
  }

  public test8() {
    try {
      // Do something
    } catch (Exception e) {
      throw new Exception(e); // Compliant
    }
  }

  public test9() {
    try {
      // Do something
    } catch (Exception e) {
      throw new OBException(e.getMessage(), e); // Noncompliant - It's not a messageBD method call
    }
  }
}
