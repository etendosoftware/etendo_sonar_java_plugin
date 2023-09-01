package com.etendoerp.sonar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.sonar.plugins.java.api.JavaCheck;

import com.etendoerp.sonar.checks.DoEarlyReturnWhenPossible;
import com.etendoerp.sonar.checks.NoCriteriaListForUniqueRule;
import com.etendoerp.sonar.checks.NoListMethodsInForClause;
import com.etendoerp.sonar.checks.RestorePreviousModeInFinally;
import com.etendoerp.sonar.checks.SetAdminModeInTry;
import com.etendoerp.sonar.checks.UseStringBuilderInsteadOfConcat;
import com.etendoerp.sonar.checks.UseStringUtilsWhenPossible;

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
    return Arrays.asList(NoCriteriaListForUniqueRule.class,
        DoEarlyReturnWhenPossible.class,
        NoListMethodsInForClause.class,
        SetAdminModeInTry.class,
        RestorePreviousModeInFinally.class,
        UseStringUtilsWhenPossible.class);
  }

  /**
   * These rules are going to target TEST code only
   */
  public static List<Class<? extends JavaCheck>> getJavaTestChecks() {
    return Collections.emptyList();
  }
}
