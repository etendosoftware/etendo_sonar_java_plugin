class StringUtilsTest {

  StringUtilsTest(StringUtilsTest strUtilsTest) {
  }

  public boolean stringsAreEqual1(String str1, String str2) {
    return StringUtils.equals(str1, str2); // Compliant
  }

  public boolean stringsAreEqual2(String str1, String str2) {
    return str1.equals(str2); // Noncompliant [[sc=12;ec=29]] {{Use the null-safe StringUtils class for this operation between Strings}}
  }

  public boolean stringsAreEqual3(String str1, String str2) {
    return str1 == str2; // Noncompliant [[sc=12;ec=24]] {{Use the null-safe StringUtils class for this operation between Strings}}
  }

  public boolean stringIsNullOrEmpty1(String str) {
    return StringUtils.isBlank(str);  // Compliant
  }

  public boolean stringIsNullOrEmpty2(String str) {
    return str.isEmpty(); // Noncompliant [[sc=12;ec=25]] {{Use the null-safe StringUtils class for this operation between Strings}}
  }

  public boolean stringContainsString(String str1, String str2) {
    return str1.contains(str2); // Noncompliant [[sc=12;ec=31]] {{Use the null-safe StringUtils class for this operation between Strings}}
  }

  public boolean stringStartsWithString(String str1, String str2) {
    return str1.startsWith(str2); // Noncompliant [[sc=12;ec=33]] {{Use the null-safe StringUtils class for this operation between Strings}}
  }
}