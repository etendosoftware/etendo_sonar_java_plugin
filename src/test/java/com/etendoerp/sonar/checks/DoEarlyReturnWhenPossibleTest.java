package com.etendoerp.sonar.checks;

import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

class DoEarlyReturnWhenPossibleTest {

  @Test
  void test() {
    CheckVerifier.newVerifier()
        .onFile("src/test/resources/DoEarlyReturnWhenPossible.java")
        .withCheck(new DoEarlyReturnWhenPossible())
        .verifyIssues();
  }
}
