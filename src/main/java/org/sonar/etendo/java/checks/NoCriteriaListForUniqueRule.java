package org.sonar.etendo.java.checks;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;

import org.sonar.check.Rule;
import org.sonar.etendo.java.utils.IssueMessages;
import org.sonar.java.model.ExpressionUtils;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

@Rule(key = "NoCriteriaListForUniqueRule")
public class NoCriteriaListForUniqueRule extends IssuableSubscriptionVisitor {

  private final Deque<ExpressionTree> invocationChain = new ArrayDeque<>();

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Collections.singletonList(Kind.METHOD_INVOCATION);
  }

  @Override
  public void visitNode(Tree tree) {
    MethodInvocationTree rootMethodInvocation = (MethodInvocationTree) tree;
    invocationChain.push(rootMethodInvocation);
    ExpressionTree methodInvocation = rootMethodInvocation;

    while (methodInvocation.is(Kind.MEMBER_SELECT)) {
      invocationChain.push(methodInvocation);
      methodInvocation = (ExpressionTree) methodInvocation.parent();
      // The loop traverses the method invocation chain correctly.
    }

    IdentifierTree rootMethodName = ExpressionUtils.methodName(rootMethodInvocation);
    HashSet<String> fullInvocation = new HashSet<>();
    for (ExpressionTree method : invocationChain) {
      if (method.is(Kind.METHOD_INVOCATION)) {
        fullInvocation.add(ExpressionUtils.methodName((MethodInvocationTree) method).name());
      } else if (method.is(Kind.MEMBER_SELECT)) {
        fullInvocation.add(((IdentifierTree) ((MethodInvocationTree) method).methodSymbol()).name());
      }
    }

    if (new HashSet<>(fullInvocation).containsAll(Arrays.asList("list", "get"))) {
      reportIssue(rootMethodName, IssueMessages.USE_UNIQUE);
    }
  }

  @Override
  public void leaveNode(Tree tree) {
    invocationChain.clear();
  }
}