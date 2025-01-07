package org.sonar.etendo.java.checks;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.sonar.check.Rule;
import org.sonar.etendo.java.utils.IssueMessages;
import org.sonar.java.model.ExpressionUtils;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.LiteralTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.NewClassTree;
import org.sonar.plugins.java.api.tree.ThrowStatementTree;
import org.sonar.plugins.java.api.tree.Tree;

/**
 * Rule to ensure that exceptions use ADMessage for their messages.
 */
@Rule(key = "UseADMessageForExceptions")
public class UseADMessageForExceptions extends IssuableSubscriptionVisitor {

  /**
   * Specifies the kinds of nodes to visit.
   *
   * @return a list containing the THROW_STATEMENT kind.
   */
  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Collections.singletonList(Tree.Kind.THROW_STATEMENT);
  }

  /**
   * Visits a node and checks if the exception message is valid.
   *
   * @param tree the tree node to visit.
   */
  @Override
  public void visitNode(Tree tree) {
    ThrowStatementTree throwStatement = (ThrowStatementTree) tree;
    NewClassTree exceptionClass = (NewClassTree) throwStatement.expression();

    if (exceptionClass.arguments().isEmpty()) {
      return;
    }

    // Analyze the first argument
    Tree firstArgument = ExpressionUtils.skipParentheses(exceptionClass.arguments().get(0));
    if (isInvalidException(firstArgument)) {
      reportIssue(
          throwStatement,
          IssueMessages.HARD_CODED_EXCEPTION_MESSAGE.getMessage()
      );
    }
  }

  /**
   * Checks if the exception argument is invalid.
   *
   * @param argument the argument to check.
   * @return true if the argument is invalid, false otherwise.
   */
  private boolean isInvalidException(Tree argument) {
    if (argument instanceof LiteralTree) {
      return true;
    }

    if (argument.is(Tree.Kind.IDENTIFIER)) {
      return StringUtils.equals(
          ((IdentifierTree) argument).symbolType().name(), "String");
    }

    return !containsMessageBDMethod(argument);
  }

  /**
   * Checks if the tree contains a messageBD method invocation.
   *
   * @param tree the tree to check.
   * @return true if the tree contains a messageBD method invocation, false otherwise.
   */
  private boolean containsMessageBDMethod(Tree tree) {
    if (tree instanceof MethodInvocationTree methodInvocation) {
      // Check if the method is messageBD
      if (isMessageBDInvocation(methodInvocation)) {
        return true;
      }

      if (methodInvocation.arguments().isEmpty()) {
        return false;
      }

      return !isInvalidException(methodInvocation.arguments().get(0));
    }
    return false;
  }

  /**
   * Checks if the method invocation is a messageBD invocation.
   *
   * @param methodInvocation the method invocation to check.
   * @return true if the method invocation is a messageBD invocation, false otherwise.
   */
  private boolean isMessageBDInvocation(MethodInvocationTree methodInvocation) {
    MemberSelectExpressionTree memberSelect = (MemberSelectExpressionTree) methodInvocation.methodSelect();
    return StringUtils.equals("messageBD", memberSelect.identifier().name());
  }
}