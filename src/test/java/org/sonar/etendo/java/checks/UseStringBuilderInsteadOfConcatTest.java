package org.sonar.etendo.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

public class UseStringBuilderInsteadOfConcatTest {

  @Test
  public void checkRule() {
    CheckVerifier.newVerifier()
        .onFile("src/test/resources/UseStringBuilderInsteadOfConcat.java")
        .withCheck(new UseStringBuilderInsteadOfConcat())
        .verifyIssues();
  }
}
