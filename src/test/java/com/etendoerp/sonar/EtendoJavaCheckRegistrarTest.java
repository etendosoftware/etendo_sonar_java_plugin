package com.etendoerp.sonar;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.sonar.plugins.java.api.CheckRegistrar;

class EtendoJavaCheckRegistrarTest {

  @Test
  void checkNumberRules() {
    CheckRegistrar.RegistrarContext context = new CheckRegistrar.RegistrarContext();

    EtendoJavaCheckRegistrar registrar = new EtendoJavaCheckRegistrar();
    registrar.register(context);

    assertThat(context.checkClasses()).hasSize(7);
    assertThat(context.testCheckClasses()).hasSize(0);
  }
}
