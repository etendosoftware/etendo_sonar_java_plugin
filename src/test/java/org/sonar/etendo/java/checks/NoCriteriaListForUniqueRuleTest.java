package org.sonar.etendo.java.checks;

import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

class NoCriteriaListForUniqueRuleTest {

  @Test
  void test() {
    CheckVerifier.newVerifier()
        .onFile("src/test/files/NoCriteriaListForUniqueRule.java")
        .withCheck(new NoCriteriaListForUniqueRule())
        .verifyIssues();
  }
}
