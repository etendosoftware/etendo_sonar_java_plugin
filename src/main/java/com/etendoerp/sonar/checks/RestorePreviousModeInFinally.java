package com.etendoerp.sonar.checks;

import java.util.Collections;
import java.util.List;

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.BlockTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.TryStatementTree;

import com.etendoerp.sonar.utils.IssueMessages;
import com.etendoerp.sonar.utils.StatementUtils;

@Rule(key = "RestorePreviousModeInFinally")
public class RestorePreviousModeInFinally extends IssuableSubscriptionVisitor {
  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Collections.singletonList(Tree.Kind.TRY_STATEMENT);
  }

  @Override
  public void visitNode(Tree tree) {
    TryStatementTree tryStatement = (TryStatementTree) tree;
    MethodInvocationTree setAdminModeInsideTry = StatementUtils.getMethodIfExists(tryStatement.block(), "OBContext",
        "setAdminMode");
    if (setAdminModeInsideTry != null) {
      BlockTree finallyStatement = tryStatement.finallyBlock();

      if (finallyStatement != null) {
        MethodInvocationTree restoreMethod = StatementUtils.getMethodIfExists(finallyStatement, "OBContext",
            "restorePreviousMode");
        if (restoreMethod != null) {
          return;
        }
      }

      reportIssue(setAdminModeInsideTry, IssueMessages.CONTEXT_MODE_NOT_RESTORED);
    }
  }
}
