package com.etendoerp.sonar.checks;

import java.util.Collections;
import java.util.List;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.IfStatementTree;
import org.sonar.plugins.java.api.tree.Tree;

import com.etendoerp.sonar.utils.IssueMessages;
import com.etendoerp.sonar.utils.StatementUtils;

@Rule(key = "DoEarlyReturnWhenPossible")
public class DoEarlyReturnWhenPossible extends IssuableSubscriptionVisitor {
  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Collections.singletonList(Tree.Kind.IF_STATEMENT);
  }

  @Override
  public void visitNode(Tree tree) {
    /* 'Early-return worthy' statements:
      - Simple if-elses: only two branches and each contains a single return statement
      - Initial error conditions: conditions that would cause the method to halt execution if true
      - Loops: search loops, on which finding the desired element results in an immediate return
    */
    IfStatementTree ifStatement = (IfStatementTree) tree;
    boolean isSimpleIfElse = StatementUtils.statementIsSimpleIfElse(ifStatement);
    boolean isInitialErrorCondition = StatementUtils.statementHandlesInitialErrorCond(ifStatement);
    boolean isLoop = StatementUtils.statementIsPartOfSearchLoop(ifStatement);

    if (isSimpleIfElse || isInitialErrorCondition || isLoop) {
      reportIssue(ifStatement, IssueMessages.DO_EARLY_RETURN);
    }
  }
}
