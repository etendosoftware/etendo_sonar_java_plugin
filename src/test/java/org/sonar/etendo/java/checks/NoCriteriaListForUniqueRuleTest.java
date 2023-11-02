package org.sonar.etendo.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

public class NoCriteriaListForUniqueRuleTest {

  @Test
  public void checkRule() {
    CheckVerifier.newVerifier()
        .onFile("src/test/resources/NoCriteriaListForUniqueRule.java")
        .withCheck(new NoCriteriaListForUniqueRule())
        .verifyIssues();
  }
}
