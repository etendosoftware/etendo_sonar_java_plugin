package org.sonar.etendo.java.checks;

import org.junit.Test;
import org.sonar.java.checks.verifier.CheckVerifier;

public class NoListMethodsInLoopClauseTest {

  @Test
  public void test() {
    CheckVerifier.newVerifier()
        .onFile("src/test/resources/NoListMethodsInLoopClause.java")
        .withCheck(new NoListMethodsInLoopClause())
        .verifyIssues();
  }
}
