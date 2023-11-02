package org.sonar.etendo.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

public class UseStringUtilsWhenPossibleTest {

  @Test
  public void checkRule() {
    CheckVerifier.newVerifier()
        .onFile("src/test/resources/UseStringUtilsWhenPossible.java")
        .withCheck(new UseStringUtilsWhenPossible())
        .verifyIssues();
  }
}
