package org.sonar.etendo.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

public class DoEarlyReturnWhenPossibleTest {

  @Test
  public void test() {
    CheckVerifier.newVerifier()
        .onFile("src/test/resources/DoEarlyReturnWhenPossible.java")
        .withCheck(new DoEarlyReturnWhenPossible())
        .verifyIssues();
  }
}
