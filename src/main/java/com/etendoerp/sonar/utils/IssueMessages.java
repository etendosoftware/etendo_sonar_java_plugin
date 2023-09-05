package com.etendoerp.sonar.utils;

public class IssueMessages {
  public static final String USE_STRING_UTILS = "Use the null-safe StringUtils class for this operation between Strings";
  public static final String USE_UNIQUE = "Make this single result retrieval using setMaxResults and uniqueResult";
  public static final String DO_EARLY_RETURN = "Consider doing an early-return to avoid an increase in complexity";
  public static final String NO_METHOD_INSIDE_FOR = "Extract this expression's method calls to a variable outside the loop";
  public static final String CONTEXT_CHANGED_OUTSIDE_TRY = "Move this context mode change inside a try-catch block";
  public static final String CONTEXT_MODE_NOT_RESTORED = "Restore the OBContext previous mode in this try block's 'finally' stage";
  public static final String MULTIPLE_STR_CONCAT_NOT_ALLOWED = "Multiple string concatenation not allowed. Consider using StringBuilder for better performance";
  private IssueMessages() {
  }
}
