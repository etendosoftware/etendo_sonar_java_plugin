package org.sonar.etendo.java.checks;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.sonar.check.Rule;
import org.sonar.etendo.java.utils.IssueMessages;
import org.sonar.etendo.java.utils.StatementUtils;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.BlockTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.TryStatementTree;

@Rule(key = "RestorePreviousModeInFinally")
public class RestorePreviousModeInFinally extends IssuableSubscriptionVisitor {
  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Collections.singletonList(Tree.Kind.TRY_STATEMENT);
  }

  @Override
  public void visitNode(Tree tree) {
    TryStatementTree tryStatement = (TryStatementTree) tree;
    Optional<MethodInvocationTree> setAdminModeInsideTry = StatementUtils
        .getMethodIfExists(tryStatement.block(), "OBContext", "setAdminMode");
    if (setAdminModeInsideTry.isPresent()) {
      BlockTree finallyStatement = tryStatement.finallyBlock();

      if (finallyStatement == null) {
        reportIssue(setAdminModeInsideTry.get(), IssueMessages.CONTEXT_MODE_NOT_RESTORED);
        return;
      }
      Optional<MethodInvocationTree> restoreMethod = StatementUtils
          .getMethodIfExists(finallyStatement, "OBContext", "restorePreviousMode");
      if (!restoreMethod.isPresent()) {
        reportIssue(setAdminModeInsideTry.get(), IssueMessages.CONTEXT_MODE_NOT_RESTORED);
      }
    }
  }
}
