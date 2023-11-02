package org.sonar.etendo.java.utils;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.sonar.plugins.java.api.tree.Arguments;
import org.sonar.plugins.java.api.tree.BinaryExpressionTree;
import org.sonar.plugins.java.api.tree.BlockTree;
import org.sonar.plugins.java.api.tree.ExpressionStatementTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.IfStatementTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.StatementTree;
import org.sonar.plugins.java.api.tree.Tree;

public class StatementUtils {
  private StatementUtils() {
  }

  /**
   * Check if a given expression is executing a list() method invocation in it
   *
   * @param expression
   *     the expression to be evaluated
   * @return true if a list() method invocation exists in the expression
   */
  public static boolean hasListMethodInvocation(ExpressionTree expression) {
    if (expression.is(Tree.Kind.METHOD_INVOCATION)) {
      ExpressionTree methodExpression = ((MethodInvocationTree) expression).methodSelect();

      if (methodExpression.is(Tree.Kind.MEMBER_SELECT)) {
        ExpressionTree memberSelectExpression = ((MemberSelectExpressionTree) methodExpression).expression();
        IdentifierTree memberSelectIdentifier = ((MemberSelectExpressionTree) methodExpression).identifier();

        // If method select identifier is list(), return true
        if (StringUtils.equals("list", memberSelectIdentifier.name())) {
          return true;
        }

        // If method select expression is another method invocation, recursion over expression
        if (memberSelectExpression.is(Tree.Kind.METHOD_INVOCATION)) {
          return hasListMethodInvocation(memberSelectExpression);
        }
        // If not of form expression.method and doesn't have arguments, return false
      } else if (((MethodInvocationTree) expression).arguments().isEmpty()) {
        return false;
      }

      // Recursion over the method's arguments, to see if any is list()
      Arguments methodArgs = ((MethodInvocationTree) expression).arguments();
      return methodArgs.stream().anyMatch(StatementUtils::hasListMethodInvocation);
    }

    if (!(expression instanceof BinaryExpressionTree)) { // Did not find a method invocation, and can't go deeper
      return false;
    }

    // Recursive step for binary expressions, going over both operands
    BinaryExpressionTree binaryExpression = (BinaryExpressionTree) expression;
    return hasListMethodInvocation(binaryExpression.leftOperand()) || hasListMethodInvocation(
        binaryExpression.rightOperand());
  }

  /**
   * Check whether an 'if' statement is handling a method's initial error condition.
   * The type of initial error condition that will be evaluated is the one that's handled with 'throw' statements.
   *
   * @param ifStatement
   *     the statement to be evaluated
   * @return true if the statement contains a 'throw' statement, handling an error condition
   */
  public static boolean statementHandlesInitialErrorCondOnElse(IfStatementTree ifStatement) {
    StatementTree thenStatement = ifStatement.thenStatement();
    StatementTree elseStatement = ifStatement.elseStatement();


    boolean thenStatementIsHandlingError;
    if (thenStatement.is(Tree.Kind.BLOCK)) {
      thenStatementIsHandlingError = ((BlockTree) thenStatement).body().stream()
          .anyMatch(t -> t.is(Tree.Kind.THROW_STATEMENT));
    } else {
      thenStatementIsHandlingError = thenStatement.is(Tree.Kind.THROW_STATEMENT);
    }

    if (thenStatementIsHandlingError) { // if the 'then' statement already handles the error, then we might be dealing with an already early-returned condition
      return false;
    }
    if (elseStatement == null) // No exception being handled
      return false;
    if (!elseStatement.is(Tree.Kind.IF_STATEMENT)) {
      BlockTree elseBlock = (BlockTree) elseStatement;
      return elseBlock.body().stream().anyMatch(t -> t.is(Tree.Kind.THROW_STATEMENT));
    }
    return false;
  }

  /**
   * Check whether an 'if' statement is a simple if-else. That is, an if-else statement composed of only one sentence
   * on each block
   *
   * @param ifStatement
   *     the statement to be evaluated
   * @return true if the statement is a simple if-else statement
   */
  public static boolean statementIsSimpleIfElse(IfStatementTree ifStatement) {
    StatementTree thenStatement = ifStatement.thenStatement();
    StatementTree elseStatement = ifStatement.elseStatement();

    if (elseStatement == null)
      return false;
    boolean thenStatementIsOneSentence = !thenStatement.is(Tree.Kind.BLOCK)
        || ((BlockTree) thenStatement).body().size() == 1;
    boolean elseStatementIsOneSentence = !elseStatement.is(Tree.Kind.BLOCK)
        || ((BlockTree) elseStatement).body().size() == 1;

    return statementShouldContainKind(thenStatement, Tree.Kind.RETURN_STATEMENT) &&
        statementShouldContainKind(elseStatement, Tree.Kind.RETURN_STATEMENT) &&
        thenStatementIsOneSentence && elseStatementIsOneSentence;
  }

  /**
   * Check if a statement block contains the specified kind
   *
   * @param thenStatement
   *     the statement to be evaluated
   * @param kind
   *     the kind that will be looked for in the statement
   * @return true if the kind is found inside the statement block
   */
  public static boolean statementShouldContainKind(StatementTree thenStatement, Tree.Kind kind) {
    boolean statementHasOnlyKindStatement;
    if (thenStatement.is(Tree.Kind.BLOCK)) {
      statementHasOnlyKindStatement = ((BlockTree) thenStatement).body().stream()
          .anyMatch(t -> t.is(kind));
    } else {
      statementHasOnlyKindStatement = thenStatement.is(kind);
    }
    return statementHasOnlyKindStatement;
  }

  /**
   * Search inside a block statement for the specified method's occurrences. If not found, return null
   *
   * @param block
   *     the block to be analyzed
   * @param methodExpressionName
   *     the method's expression name. That would be the expression from where the method is
   *     invoked. E.g: on 'x.method()' the expression would be 'x'
   * @param methodIdentifierName
   *     the method's identifier name. That would be the name of the method being invoked.
   *     E.g: on 'x.method()' the identifier would be 'method'
   * @return the MethodInvocationInstance for the method, if found. Else null
   */
  public static Optional<MethodInvocationTree> getMethodIfExists(BlockTree block, String methodExpressionName,
      String methodIdentifierName) {
    List<StatementTree> blockStatements = block.body();

    for (StatementTree statement : blockStatements) {
      if (statement.is(Tree.Kind.EXPRESSION_STATEMENT)) {
        ExpressionTree expression = ((ExpressionStatementTree) statement).expression();

        if (expression.is(Tree.Kind.METHOD_INVOCATION)) {
          MethodInvocationTree methodInvocation = (MethodInvocationTree) expression;
          ExpressionTree methodSelect = methodInvocation.methodSelect();
          if (!methodSelect.is(Tree.Kind.MEMBER_SELECT)) {
            continue;
          }

          if (methodIsCalledFromAndLastCalls(methodSelect, methodExpressionName, methodIdentifierName)) {
            return Optional.of(methodInvocation);
          }
        }
      }
    }
    return Optional.empty();
  }

  /**
   * Check if a given method's first token and last method call match the given parameters
   *
   * @param method
   *     the method being analyzed
   * @param firstToken
   *     the first token's name
   * @param identifier
   *     the last method call name
   * @return true if the method's first token and last method call match the values
   */
  public static boolean methodIsCalledFromAndLastCalls(ExpressionTree method, String firstToken,
      String identifier) {
    MemberSelectExpressionTree memberSelect = (MemberSelectExpressionTree) method;
    if (memberSelect.firstToken() != null) {
      String statementFirstToken = memberSelect.firstToken().text();
      String statementIdentifier = memberSelect.identifier().name();

      return StringUtils.equals(statementFirstToken, firstToken) &&
          StringUtils.equals(statementIdentifier, identifier);
    }
    return false;
  }
}
