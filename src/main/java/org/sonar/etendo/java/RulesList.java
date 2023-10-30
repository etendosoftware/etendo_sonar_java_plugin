package org.sonar.etendo.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.sonar.etendo.java.checks.DoEarlyReturnWhenPossible;
import org.sonar.etendo.java.checks.NoCriteriaListForUniqueRule;
import org.sonar.etendo.java.checks.NoGlobalOBContextVariables;
import org.sonar.etendo.java.checks.NoListMethodsInLoopClause;
import org.sonar.etendo.java.checks.RestorePreviousModeInFinally;
import org.sonar.etendo.java.checks.SetAdminModeInTry;
import org.sonar.etendo.java.checks.UseStringBuilderInsteadOfConcat;
import org.sonar.etendo.java.checks.UseStringUtilsWhenPossible;
import org.sonar.plugins.java.api.JavaCheck;

public final class RulesList {
  private RulesList() {
  }

  public static List<Class<? extends JavaCheck>> getChecks() {
    List<Class<? extends JavaCheck>> checks = new ArrayList<>();
    checks.addAll(getJavaChecks());
    checks.addAll(getJavaTestChecks());
    return Collections.unmodifiableList(checks);
  }

  /**
   * These rules are going to target MAIN code only
   */
  public static List<Class<? extends JavaCheck>> getJavaChecks() {
    return Arrays.asList(
        DoEarlyReturnWhenPossible.class,
        NoCriteriaListForUniqueRule.class,
        NoGlobalOBContextVariables.class,
        NoListMethodsInLoopClause.class,
        RestorePreviousModeInFinally.class,
        SetAdminModeInTry.class,
        UseStringBuilderInsteadOfConcat.class,
        UseStringUtilsWhenPossible.class);
  }

  /**
   * These rules are going to target TEST code only
   */
  public static List<Class<? extends JavaCheck>> getJavaTestChecks() {
    return Collections.emptyList();
  }
}
