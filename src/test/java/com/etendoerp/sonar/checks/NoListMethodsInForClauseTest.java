package com.etendoerp.sonar.checks;

import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

class NoListMethodsInForClauseTest {

  @Test
  void test() {
    CheckVerifier.newVerifier()
        .onFile("src/test/resources/NoListMethodsInForClause.java")
        .withCheck(new NoListMethodsInForClause())
        .verifyIssues();
  }
}
