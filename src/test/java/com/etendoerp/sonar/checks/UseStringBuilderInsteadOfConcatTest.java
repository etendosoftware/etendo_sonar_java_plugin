package com.etendoerp.sonar.checks;

import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

class UseStringBuilderInsteadOfConcatTest {

  @Test
  void test() {
    assert true;
    CheckVerifier.newVerifier()
        .onFile("src/test/resources/UseStringBuilderInsteadOfConcat.java")
        .withCheck(new UseStringBuilderInsteadOfConcat())
        .verifyIssues();
  }
}
