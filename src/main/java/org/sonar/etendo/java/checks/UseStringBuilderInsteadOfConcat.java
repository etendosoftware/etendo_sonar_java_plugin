package org.sonar.etendo.java.checks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.etendo.java.utils.IssueMessages;
import org.sonar.java.model.ExpressionUtils;
import org.sonar.java.model.LiteralUtils;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.BinaryExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.LiteralTree;
import org.sonar.plugins.java.api.tree.Tree;

@Rule(key = "UseStringBuilderInsteadOfConcat")
public class UseStringBuilderInsteadOfConcat extends IssuableSubscriptionVisitor {

  public static final String THRESHOLD_PARAM = "Threshold";
  public static final int THRESHOLD_DEFAULT_VALUE = 5;
  public static final String THRESHOLD_DESCRIPTION = "Number of concatenations (literals and variables) that have to be made to trigger an issue";
  public static final String STR_LEN_PARAM = "Min String Length";
  public static final int STR_LEN_DEFAULT_VALUE = 19;
  public static final String STR_LEN_DESCRIPTION = "The minimum size a String should be to be taken into account when counting concatenations";
  private final Set<Tree> visitedNodes = new HashSet<>();
  private final List<Tree> concats = new ArrayList<>();
  @RuleProperty(
      key = THRESHOLD_PARAM,
      defaultValue = "" + THRESHOLD_DEFAULT_VALUE,
      description = THRESHOLD_DESCRIPTION)
  protected int threshold = THRESHOLD_DEFAULT_VALUE;

  @RuleProperty(
      key = STR_LEN_PARAM,
      defaultValue = "" + STR_LEN_DEFAULT_VALUE,
      description = STR_LEN_DESCRIPTION)
  protected int minStrLen = STR_LEN_DEFAULT_VALUE;

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Collections.singletonList(Tree.Kind.PLUS);
  }

  @Override
  public void visitNode(Tree tree) {
    if (visitedNodes.contains(tree))
      return;
    boolean stringsAreConcatenated = concatStringLiteralsOrVars(concats, tree);
    if (stringsAreConcatenated && concats.size() >= threshold)
      reportIssue(concats.get(0), IssueMessages.MULTIPLE_STR_CONCAT_NOT_ALLOWED.getMessage());
    concats.clear();
  }

  private boolean concatStringLiteralsOrVars(List<Tree> concats, Tree tree) {
    if (tree.is(Tree.Kind.PLUS)) {
      BinaryExpressionTree binaryExpression = (BinaryExpressionTree) tree;
      visitedNodes.add(binaryExpression);
      return concatStringLiteralsOrVars(concats, ExpressionUtils.skipParentheses(binaryExpression.leftOperand())) &&
          concatStringLiteralsOrVars(concats, ExpressionUtils.skipParentheses(binaryExpression.rightOperand()));
    } else if (tree instanceof LiteralTree) {
      String treeValue = LiteralUtils.getAsStringValue(((LiteralTree) tree));
      if (treeValue.length() >= minStrLen)
        concats.add(tree);
      return true;
    } else if (tree instanceof IdentifierTree) {
      concats.add(tree);
      return true;
    }
    return false;
  }

  public void testIssueFromSonar() {
    String variable = "trigger an Issue here because the rule ";
    String str = "This is an example for a " +
        "case where multiple Strings are " +
        "being concatenated in a single variable. " +
        "Normal String concatenation using + will " +
        variable +
        "is designed to report this kind of behaviour";
    // ...
  }
}
