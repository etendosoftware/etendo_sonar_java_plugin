package org.sonar.etendo.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

public class RestorePreviousModeInFinallyTest {

  @Test
  public void checkRule() {
    CheckVerifier.newVerifier()
        .onFile("src/test/resources/RestorePreviousModeInFinally.java")
        .withCheck(new RestorePreviousModeInFinally())
        .verifyIssues();
  }
}
