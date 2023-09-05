package com.etendoerp.sonar.utils;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.sonar.java.model.expression.MemberSelectExpressionTreeImpl;
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
   * Check whether an 'if' statement is handling a method's initial error condition
   * @param ifStatement the statement to be evaluated
   * @return true if the statements has a 'throw' statement, handling an error condition
   */
  public static boolean statementHandlesInitialErrorCond(IfStatementTree ifStatement) {
    StatementTree elseStatement = ifStatement.elseStatement();

    // The type of initial error condition that will be evaluated is the one that's handled with 'throw' statements
    if (elseStatement == null)
      return false;
    BlockTree elseBlock = (BlockTree) elseStatement;
    return elseBlock.body().stream().anyMatch(t -> t.is(Tree.Kind.THROW_STATEMENT));
  }

  /**
   * Check whether an 'if' statement is a simple if-else. That is, an if-else statement composed of only one sentence
   * on each block
   * @param ifStatement the statement to be evaluated
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
   * @param thenStatement the statement to be evaluated
   * @param kind the kind that will be looked for in the statement
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
   * Check whether an 'if' statement is part of a simple search loop. This is, if the statement's purpose is only to
   * change a variable's state after achieving a condition, and exit the loop
   * @param ifStatement the statement to be evaluated
   * @return true if the statement is part of a search loop
   */
  public static boolean statementIsPartOfSearchLoop(IfStatementTree ifStatement) {
    StatementTree thenStatement = ifStatement.thenStatement();
    Tree parent = ifStatement.parent();

    if (!(parent != null && parent.is(Tree.Kind.BLOCK)))
      return false;
    if (!(parent.parent() != null && parent.parent().is(Tree.Kind.FOR_STATEMENT, Tree.Kind.FOR_EACH_STATEMENT,
        Tree.Kind.WHILE_STATEMENT, Tree.Kind.DO_STATEMENT)))
      return false;

    return StatementUtils.statementShouldContainKind(thenStatement, Tree.Kind.BREAK_STATEMENT);
  }

  /**
   * Search inside a block statement for the specified method's occurrences. If not found, return null
   * @param block the block to be analyzed
   * @param methodExpressionName the method's expression name. That would be the expression from where the method is
   * invoked. E.g: on 'x.method()' the expression would be 'x'
   * @param methodIdentifierName the method's identifier name. That would be the name of the method being invoked.
   * E.g: on 'x.method()' the identifier would be 'method'
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

          if (methodMatchesExpressionAndIdentifier(methodSelect, methodExpressionName, methodIdentifierName)) {
            return Optional.of(methodInvocation);
          }
        }
      }
    }
    return Optional.empty();
  }

  /**
   * Check if a given method's expression and identifier match with the specified names
   * @param method the method being analyzed
   * @param expression the expression's name
   * @param identifier the identifier's name
   * @return true if the method's expression and identifier match the values
   */
  public static boolean methodMatchesExpressionAndIdentifier(ExpressionTree method, String expression,
      String identifier) {
    MemberSelectExpressionTreeImpl memberSelect = (MemberSelectExpressionTreeImpl) method;
    ExpressionTree statementExpression = memberSelect.expression();
    IdentifierTree statementIdentifier = memberSelect.identifier();
    if (statementExpression.is(Tree.Kind.IDENTIFIER)) {
      String statementExpressionName = ((IdentifierTree) statementExpression).name();
      String statementIdentifierName = statementIdentifier.name();

      return StringUtils.equals(statementExpressionName, expression) &&
          StringUtils.equals(statementIdentifierName, identifier);
    }
    return false;
  }
}
