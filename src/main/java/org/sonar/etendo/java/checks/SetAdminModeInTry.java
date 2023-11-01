package org.sonar.etendo.java.checks;

import java.util.Collections;
import java.util.List;

import org.sonar.check.Rule;
import org.sonar.etendo.java.utils.IssueMessages;
import org.sonar.etendo.java.utils.StatementUtils;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.Tree;

@Rule(key = "SetAdminModeInTry")
public class SetAdminModeInTry extends IssuableSubscriptionVisitor {

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Collections.singletonList(Tree.Kind.MEMBER_SELECT);
  }

  @Override
  public void visitNode(Tree tree) {
    MemberSelectExpressionTree methodInvocation = (MemberSelectExpressionTree) tree;
    Tree currentLocation = methodInvocation;

    if (methodInvocation.firstToken() != null &&
        StatementUtils.methodIsCalledFromAndLastCalls(methodInvocation, "OBContext", "setAdminMode")) {
      while (currentLocation.parent() != null && !(currentLocation.parent().is(Tree.Kind.TRY_STATEMENT))) {
        currentLocation = currentLocation.parent();
      }
      if (currentLocation.parent() == null) {
        reportIssue(tree, IssueMessages.CONTEXT_CHANGED_OUTSIDE_TRY.getMessage());
      }
    }
  }

  public void testIssueFromSonar() {
    // ...
    OBContext.setAdminMode(true);
    try {
      // ...
    } catch (Exception e) {
      // ...
    }
  }
}
