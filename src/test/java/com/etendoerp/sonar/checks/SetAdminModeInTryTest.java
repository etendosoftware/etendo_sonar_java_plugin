package com.etendoerp.sonar.checks;

import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

class SetAdminModeInTryTest {

  @Test
  void test() {
    CheckVerifier.newVerifier()
        .onFile("src/test/resources/SetAdminModeInTry.java")
        .withCheck(new SetAdminModeInTry())
        .verifyIssues();
  }
}
