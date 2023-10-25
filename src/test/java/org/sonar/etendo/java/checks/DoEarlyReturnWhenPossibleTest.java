package org.sonar.etendo.java.checks;

import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

class DoEarlyReturnWhenPossibleTest {

  @Test
  void test() {
    CheckVerifier.newVerifier()
        .onFile("src/test/files/DoEarlyReturnWhenPossible.java")
        .withCheck(new DoEarlyReturnWhenPossible())
        .verifyIssues();
  }
}
