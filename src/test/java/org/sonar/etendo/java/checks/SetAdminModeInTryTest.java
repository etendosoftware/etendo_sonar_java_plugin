package org.sonar.etendo.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

public class SetAdminModeInTryTest {

  @Test
  public void test() {
    CheckVerifier.newVerifier()
        .onFile("src/test/resources/SetAdminModeInTry.java")
        .withCheck(new SetAdminModeInTry())
        .verifyIssues();
  }
}
