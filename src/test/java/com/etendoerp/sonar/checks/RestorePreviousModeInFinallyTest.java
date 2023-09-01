package com.etendoerp.sonar.checks;

import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

class RestorePreviousModeInFinallyTest {

  @Test
  void test() {
    CheckVerifier.newVerifier()
        .onFile("src/test/files/RestorePreviousModeInFinally.java")
        .withCheck(new RestorePreviousModeInFinally())
        .verifyIssues();
  }
}
