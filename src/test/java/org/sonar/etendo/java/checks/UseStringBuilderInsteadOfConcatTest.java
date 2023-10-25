package org.sonar.etendo.java.checks;

import org.junit.jupiter.api.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

class UseStringBuilderInsteadOfConcatTest {

  @Test
  void test() {
    assert true;
    CheckVerifier.newVerifier()
        .onFile("src/test/files/UseStringBuilderInsteadOfConcat.java")
        .withCheck(new UseStringBuilderInsteadOfConcat())
        .verifyIssues();
  }
}
