package org.sonar.etendo.java.checks;

import java.util.Arrays;
import java.util.List;

import org.sonar.check.Rule;
import org.sonar.etendo.java.utils.IssueMessages;
import org.sonar.etendo.java.utils.StatementUtils;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.DoWhileStatementTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.ForEachStatement;
import org.sonar.plugins.java.api.tree.ForStatementTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.WhileStatementTree;

@Rule(key = "NoListMethodsInLoopClause")
public class NoListMethodsInLoopClause extends IssuableSubscriptionVisitor {
  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Arrays.asList(Tree.Kind.FOR_STATEMENT, Tree.Kind.WHILE_STATEMENT, Tree.Kind.DO_STATEMENT,
        Tree.Kind.FOR_EACH_STATEMENT);
  }

  @Override
  public void visitNode(Tree tree) {
    ExpressionTree loopCondition;

    // Check which kind of loop we're dealing with, and extract the exit condition from it
    switch (tree.kind()) {
      case FOR_STATEMENT:
        ForStatementTree forLoop = (ForStatementTree) tree;
        loopCondition = forLoop.condition();
        break;
      case FOR_EACH_STATEMENT:
        ForEachStatement foreachLoop = (ForEachStatement) tree;
        loopCondition = foreachLoop.expression();
        break;
      case WHILE_STATEMENT:
        WhileStatementTree whileLoop = (WhileStatementTree) tree;
        loopCondition = whileLoop.condition();
        break;
      case DO_STATEMENT:
        DoWhileStatementTree doLoop = (DoWhileStatementTree) tree;
        loopCondition = doLoop.condition();
        break;
      default:
        return;
    }

    if (loopCondition != null && StatementUtils.hasListMethodInvocation(loopCondition)) {
      reportIssue(loopCondition, IssueMessages.NO_METHOD_INSIDE_FOR.getMessage());
    }
  }

  public void testIssueFromSonar() {
    OBCriteria<OrderHistoryItem> criteria = OBDal.getInstance().createCriteria(OrderHistoryItem.class);
    for (OrderHistory item : criteria.list()) {
      // ...
    }
  }
}
