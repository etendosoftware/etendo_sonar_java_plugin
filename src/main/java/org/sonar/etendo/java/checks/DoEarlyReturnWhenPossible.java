package org.sonar.etendo.java.checks;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sonar.check.Rule;
import org.sonar.etendo.java.utils.IssueMessages;
import org.sonar.etendo.java.utils.StatementUtils;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.IfStatementTree;
import org.sonar.plugins.java.api.tree.Tree;

@Rule(key = "DoEarlyReturnWhenPossible")
public class DoEarlyReturnWhenPossible extends IssuableSubscriptionVisitor {

  private final Set<Tree> visitedNodes = new HashSet<>();
  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Collections.singletonList(Tree.Kind.IF_STATEMENT);
  }

  @Override
  public void visitNode(Tree tree) {
    /* 'Early-return worthy' statements:
      - Simple if-elses: only two branches and each contains a single return statement
      - Initial error conditions: conditions that would cause the method to halt execution if true
    */
    if (visitedNodes.contains(tree))
      return;
    IfStatementTree ifStatement = (IfStatementTree) tree;
    boolean isSimpleIfElse = StatementUtils.statementIsSimpleIfElse(ifStatement);
    boolean isInitialErrorCondition = StatementUtils.statementHandlesInitialErrorCondOnElse(ifStatement);

    if (isSimpleIfElse || isInitialErrorCondition) {
      reportIssue(ifStatement, IssueMessages.DO_EARLY_RETURN.getMessage());
    }
    visitedNodes.add(tree);
  }
}
