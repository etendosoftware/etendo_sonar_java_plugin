package org.sonar.etendo.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

public class NoGlobalOBContextVariablesTest {
  @Test
  public void checkRule() {
    CheckVerifier.newVerifier()
        .onFile("src/test/resources/NoGlobalOBContextVariables.java")
        .withCheck(new NoGlobalOBContextVariables())
        .verifyIssues();
  }
}
