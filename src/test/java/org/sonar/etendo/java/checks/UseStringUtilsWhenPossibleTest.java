package org.sonar.etendo.java.checks;

import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

class UseStringUtilsWhenPossibleTest {

  @Test
  void test() {
    CheckVerifier.newVerifier()
        .onFile("src/test/files/UseStringUtilsWhenPossible.java")
        .withCheck(new UseStringUtilsWhenPossible())
        .verifyIssues();
  }
}
