package org.sonar.etendo.java;

import org.sonar.api.Plugin;

/**
 * This class is the entry point for all extensions
 */
public class EtendoJavaPlugin implements Plugin {

  @Override
  public void define(Context context) {
    // server extensions -> objects are instantiated during server startup
    context.addExtension(EtendoJavaRulesDefinition.class);


    // Batch extensions -> Objects are instantiated during code analysis
    context.addExtension(EtendoJavaCheckRegistrar.class);

  }
}
