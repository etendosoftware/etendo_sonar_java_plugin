package org.sonar.etendo.java.checks;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.sonar.check.Rule;
import org.sonar.etendo.java.utils.IssueMessages;
import org.sonar.java.model.expression.MethodInvocationTreeImpl;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.ClassTree;
import org.sonar.plugins.java.api.tree.ExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.SyntaxToken;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

@Rule(key = "NoGlobalOBContextVariables")
public class NoGlobalOBContextVariables extends IssuableSubscriptionVisitor {
  @Override
  public List<Tree.Kind> nodesToVisit() {
    return Collections.singletonList(Tree.Kind.CLASS);
  }

  @Override
  public void visitNode(Tree tree) {
    ClassTree classTree = (ClassTree) tree;
    List<Tree> classVars = classTree.members().stream().filter(t -> t.is(Tree.Kind.VARIABLE)).collect(
        Collectors.toList());

    for (Tree classVarTree : classVars) {
      VariableTree classVar = (VariableTree) classVarTree;
      ExpressionTree varInitializer = classVar.initializer();
      if (varInitializer != null && varInitializer.is(Tree.Kind.METHOD_INVOCATION)) {
        SyntaxToken initializerFstToken = ((MethodInvocationTree) varInitializer).methodSelect().firstToken();
        if (StringUtils.equals("OBContext", initializerFstToken.text())) {
          reportIssue(classVar, IssueMessages.GLOBAL_OBCONTEXT_VARS_NOT_ALLOWED.getMessage());
        }
      }
    }
  }
}
