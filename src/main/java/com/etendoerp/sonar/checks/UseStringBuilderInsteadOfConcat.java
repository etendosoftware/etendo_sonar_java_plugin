package com.etendoerp.sonar.checks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.java.model.ExpressionUtils;
import org.sonar.java.model.LiteralUtils;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.BinaryExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.LiteralTree;
import org.sonar.plugins.java.api.tree.Tree;

import com.etendoerp.sonar.utils.IssueMessages;

@Rule(key = "UseStringBuilderInsteadOfConcat")
public class UseStringBuilderInsteadOfConcat extends IssuableSubscriptionVisitor {

  public static final int STR_LEN_DEFAULT_VALUE = 19;
  private static final int DEFAULT_VALUE = 5;
  private final Set<Tree> visitedNodes = new HashSet<>();
  private final List<Tree> concats = new ArrayList<>();
  @RuleProperty(
      key = "threshold",
      defaultValue = "" + DEFAULT_VALUE,
      description = "Number of concatenations that have to be made to trigger an issue")
  protected int threshold = DEFAULT_VALUE;

  @RuleProperty(
      key = "str_length",
      defaultValue = "" + STR_LEN_DEFAULT_VALUE,
      description = "The minimum size a String should be to be taken into account when counting concatenations")
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
      reportIssue(concats.get(0), IssueMessages.MULTIPLE_STR_CONCAT_NOT_ALLOWED);
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
}
