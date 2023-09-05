package com.etendoerp.sonar;

import org.sonar.api.Plugin;

import com.etendoerp.sonar.checks.DoEarlyReturnWhenPossible;
import com.etendoerp.sonar.checks.NoCriteriaListForUniqueRule;
import com.etendoerp.sonar.checks.NoListMethodsInForClause;
import com.etendoerp.sonar.checks.SetAdminModeInTry;
import com.etendoerp.sonar.checks.UseStringBuilderInsteadOfConcat;
import com.etendoerp.sonar.checks.UseStringUtilsWhenPossible;

/**
 * This class is the entry point for all extensions
 */
public class EtendoJavaPlugin implements Plugin {

  @Override
  public void define(Context context) {
    // server extensions -> objects are instantiated during server startup
    context.addExtension(NoCriteriaListForUniqueRule.class);
    context.addExtension(DoEarlyReturnWhenPossible.class);
    context.addExtension(NoListMethodsInForClause.class);
    context.addExtension(SetAdminModeInTry.class);
    context.addExtension(UseStringBuilderInsteadOfConcat.class);
    context.addExtension(UseStringUtilsWhenPossible.class);

    // Batch extensions -> Objects are instantiated during code analysis
    context.addExtension(EtendoJavaCheckRegistrar.class);

  }
}
