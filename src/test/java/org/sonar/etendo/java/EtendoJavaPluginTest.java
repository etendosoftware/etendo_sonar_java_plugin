package org.sonar.etendo.java;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarProduct;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.utils.Version;

public class EtendoJavaPluginTest {

  @Test
  public void checkPluginLoadsCorrectly() {
    Plugin.Context context = new Plugin.Context(new MockedSonarRuntime());

    new EtendoJavaPlugin().define(context);

    assertThat(context.getExtensions())
        .extracting(ext -> ((Class) ext).getSimpleName())
        .containsExactlyInAnyOrder(
        "EtendoJavaRulesDefinition",
            "EtendoJavaCheckRegistrar"
        );
  }

  public static class MockedSonarRuntime implements SonarRuntime {

    @Override
    public Version getApiVersion() {
      return Version.create(9, 9);
    }

    @Override
    public SonarProduct getProduct() {
      return SonarProduct.SONARQUBE;
    }

    @Override
    public SonarQubeSide getSonarQubeSide() {
      return SonarQubeSide.SCANNER;
    }

    @Override
    public SonarEdition getEdition() {
      return SonarEdition.COMMUNITY;
    }
  }
}
