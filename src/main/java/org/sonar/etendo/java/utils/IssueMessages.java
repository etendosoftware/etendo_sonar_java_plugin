package org.sonar.etendo.java.utils;

public enum IssueMessages {
  USE_STRING_UTILS("Use the null-safe StringUtils class for this operation between Strings"),
  USE_UNIQUE("Make this single result retrieval using setMaxResults and uniqueResult"),
  DO_EARLY_RETURN("Consider doing an early-return to avoid an increase in complexity"),
  NO_METHOD_INSIDE_FOR("Extract this expression's method calls to a variable outside the loop"),
  CONTEXT_CHANGED_OUTSIDE_TRY("Move this context mode change inside a try-catch block"),
  CONTEXT_MODE_NOT_RESTORED("Restore the OBContext previous mode in this try block's 'finally' stage"),
  MULTIPLE_STR_CONCAT_NOT_ALLOWED(
      "Multiple string concatenation not allowed. Consider using StringBuilder for better performance"),
  GLOBAL_OBCONTEXT_VARS_NOT_ALLOWED("Move this \"OBContext.getOBContext()\" variable assignment inside a method."),
  HARD_CODED_EXCEPTION_MESSAGE("Exception messages should never be hard-coded. An AD_MESSAGE entry should be created for each message to be thrown.");



  private final String message;

  IssueMessages(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
