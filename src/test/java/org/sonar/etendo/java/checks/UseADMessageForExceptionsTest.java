package org.sonar.etendo.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

/**
 * Test class for the UseADMessageForExceptions rule.
 */
public class UseADMessageForExceptionsTest {

  /**
   * Tests the UseADMessageForExceptions rule.
   */
  @Test
  public void checkRule() {
    CheckVerifier.newVerifier()
        .onFile("src/test/resources/UseADMessageForExceptions.java")
        .withCheck(new UseADMessageForExceptions())
        .verifyIssues();
  }
}