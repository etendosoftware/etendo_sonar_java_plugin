class EarlyReturnTest {
  public EarlyReturnTest(EarlyReturnTest earlyRetTest) {
  }

  public boolean isPositiveNoncomp(int number) {
    if (number > 0) { // Noncompliant
      return true;
    } else {
      return false;
    }
  }

  public boolean isPositiveComp(int number) {
    if (number <= 0) // Compliant
      return false;
    return true;
  }

  public boolean containsNoncomp(int[] arr, int el) {
    boolean found = false;
    for (int i : arr) {
      if (i == el) { // Noncompliant
        found = true;
        break;
      }
    }
    return found;
  }

  public boolean containsComp(int[] arr, int el) {
    for (int i : arr) {
      if (i == el) // Compliant
        return true;
    }
    return false;
  }

  public boolean isNonEmptyString(String str) {
    if (str != null) {
      if (!str.trim().isEmpty()) { // Noncompliant
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }
}