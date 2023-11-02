package org.sonar.etendo.java;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.Param;
import org.sonar.api.server.rule.RulesDefinition.Repository;
import org.sonar.api.server.rule.RulesDefinition.Rule;
import org.sonar.etendo.java.checks.UseStringBuilderInsteadOfConcat;

public class EtendoJavaRulesDefinitionTest {

  private static final String CONCAT_RULE = "UseStringBuilderInsteadOfConcat";

  private static void assertParameterProperties(Repository repository) {
    Param threshold = repository.rule(CONCAT_RULE).param(UseStringBuilderInsteadOfConcat.THRESHOLD_PARAM);
    assertThat(threshold).isNotNull();
    String defaultThreshold = String.valueOf(UseStringBuilderInsteadOfConcat.THRESHOLD_DEFAULT_VALUE);
    assertThat(threshold.defaultValue()).isEqualTo(defaultThreshold);
    assertThat(threshold.description()).isEqualTo(
        UseStringBuilderInsteadOfConcat.THRESHOLD_DESCRIPTION);
    assertThat(threshold.type()).isEqualTo(RuleParamType.INTEGER);

    Param min = repository.rule(CONCAT_RULE).param(UseStringBuilderInsteadOfConcat.STR_LEN_PARAM);
    assertThat(min).isNotNull();
    String defaultMinLen = String.valueOf(UseStringBuilderInsteadOfConcat.STR_LEN_DEFAULT_VALUE);
    assertThat(min.defaultValue()).isEqualTo(defaultMinLen);
    assertThat(min.description()).isEqualTo(
        UseStringBuilderInsteadOfConcat.STR_LEN_DESCRIPTION);
    assertThat(min.type()).isEqualTo(RuleParamType.INTEGER);
  }

  private static void assertRuleProperties(Repository repository) {
    Rule rule = repository.rule(CONCAT_RULE);
    assertThat(rule).isNotNull();
    assertThat(rule.name()).isEqualTo("Massive String concatenation should be made using StringBuilder");
    assertThat(rule.type()).isEqualTo(RuleType.CODE_SMELL);
  }

  private static void assertAllRuleParametersHaveDescription(Repository repository) {
    for (Rule rule : repository.rules()) {
      for (Param param : rule.params()) {
        assertThat(param.description()).as("description for " + param.key()).isNotEmpty();
      }
    }
  }

  @Test
  public void checkRulesDefinitionLoads() {
    EtendoJavaRulesDefinition rulesDefinition = new EtendoJavaRulesDefinition(
        new EtendoJavaPluginTest.MockedSonarRuntime());
    RulesDefinition.Context context = new RulesDefinition.Context();
    rulesDefinition.define(context);
    RulesDefinition.Repository repository = context.repository(EtendoJavaRulesDefinition.REPOSITORY_KEY);

    assertThat(repository.name()).isEqualTo(EtendoJavaRulesDefinition.REPOSITORY_NAME);
    assertThat(repository.language()).isEqualTo("java");
    assertThat(repository.rules()).hasSize(RulesList.getChecks().size());
    assertThat(repository.rules().stream().filter(Rule::template)).isEmpty();

    assertRuleProperties(repository);
    assertParameterProperties(repository);
    assertAllRuleParametersHaveDescription(repository);
  }
}
