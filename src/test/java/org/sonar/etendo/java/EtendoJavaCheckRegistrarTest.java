package org.sonar.etendo.java;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.sonar.plugins.java.api.CheckRegistrar;

public class EtendoJavaCheckRegistrarTest {

  @Test
  public void checkNumberRules() {
    CheckRegistrar.RegistrarContext context = new CheckRegistrar.RegistrarContext();

    EtendoJavaCheckRegistrar registrar = new EtendoJavaCheckRegistrar();
    registrar.register(context);

    assertThat(context.checkClasses()).hasSize(8);
    assertThat(context.testCheckClasses()).hasSize(0); // NOSONAR
  }
}
