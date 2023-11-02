package org.sonar.etendo.java.checks;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.sonar.check.Rule;
import org.sonar.etendo.java.utils.IssueMessages;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.BinaryExpressionTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;

@Rule(key = "UseStringUtilsWhenPossible")
public class UseStringUtilsWhenPossible extends IssuableSubscriptionVisitor {

  private static final String JAVA_LANG_STRING = "java.lang.String";

  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Arrays.asList(
        Tree.Kind.METHOD_INVOCATION, Tree.Kind.EQUAL_TO);
  }

  @Override
  public void visitNode(Tree tree) {
    switch (tree.kind()) {
      case METHOD_INVOCATION: // String method invocations. E.g: str1.compare(str2)
        MethodInvocationTree methodInvocation = (MethodInvocationTree) tree;
        if (!methodInvocation.methodSelect().is(Tree.Kind.IDENTIFIER)) { // Only check if it's an expression
          Type methodType = ((MemberSelectExpressionTree) methodInvocation.methodSelect()).expression().symbolType();
          if (StringUtils.equals(methodType.symbol().name(), "String")) {
            reportIssue(methodInvocation, IssueMessages.USE_STRING_UTILS.getMessage());
          }
        }
        break;
      case EQUAL_TO: // Binary expressions using '==' (should never be used for Strings, but the case will be covered anyway)
        BinaryExpressionTree equalsExpression = (BinaryExpressionTree) tree;
        Type leftOp = equalsExpression.leftOperand().symbolType();
        Type rightOp = equalsExpression.rightOperand().symbolType();
        if (StringUtils.equals(leftOp.fullyQualifiedName(), JAVA_LANG_STRING) &&
            StringUtils.equals(rightOp.fullyQualifiedName(), JAVA_LANG_STRING)) {
          reportIssue(equalsExpression, IssueMessages.USE_STRING_UTILS.getMessage());
        }
        break;
      default:
        break;
    }
  }
}
