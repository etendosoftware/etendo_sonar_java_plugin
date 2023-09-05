package com.etendoerp.sonar.checks;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.Tree;

import com.etendoerp.sonar.utils.IssueMessages;
import com.etendoerp.sonar.utils.StatementUtils;

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

    IdentifierTree methodExpression = (IdentifierTree) methodInvocation.expression();
    IdentifierTree methodIdentifier = methodInvocation.identifier();

    if (StatementUtils.methodMatchesExpressionAndIdentifier(methodInvocation, methodExpression.name(),
        methodIdentifier.name())) {
      while (currentLocation.parent() != null && !(currentLocation.parent().is(Tree.Kind.TRY_STATEMENT))) {
        currentLocation = currentLocation.parent();
      }
      if (currentLocation.parent() == null) {
        reportIssue(tree, IssueMessages.CONTEXT_CHANGED_OUTSIDE_TRY);
      }
    }
  }
}
