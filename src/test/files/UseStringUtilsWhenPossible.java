import java.math.BigDecimal;
import java.util.HashMap;

class StringUtilsTest {

  StringUtilsTest(StringUtilsTest strUtilsTest) {
  }

  public boolean stringsAreEqual1(String str1, String str2) {
    return StringUtils.equals(str1, str2); // Compliant
  }

  public boolean stringsAreEqual2(String str1, String str2) {
    return str1.equals( // Noncompliant
        str2);
  }

  public boolean stringsAreEqual3(String str1, String str2) {
    return str1 == str2; // Noncompliant
  }

  public boolean stringIsNullOrEmpty1(String str) {
    return StringUtils.isBlank(str);  // Compliant
  }

  public boolean stringIsNullOrEmpty2(String str) {
    return str.isEmpty(); // Noncompliant
  }

  public boolean stringContainsString(String str1, String str2) {
    return str1.contains( // Noncompliant
        str2);
  }

  public boolean stringStartsWithString(String str1, String str2) {
    return str1.startsWith( // Noncompliant
        str2);
  }

  //////////////////////// TEST CASES FROM CORE CODE SNIPPETS //////////////////////

  /*
  `initialize` method of Etendo Core's `TaxesTestData7` class
   */
  public void initialize() { // Compliant

    // Header info
    setTaxDocumentLevel(true);
    setPriceIncludingTaxes(false);

    // Line info
    TaxesLineTestData line = new TaxesLineTestData();
    line.setProductId(ProductDataConstants.RAW_MATERIAL_A);
    line.setQuantity(BigDecimal.ONE);
    line.setPrice(new BigDecimal("3"));
    line.setQuantityUpdated(new BigDecimal("2"));
    line.setPriceUpdated(new BigDecimal("3"));
    line.setTaxid(TaxDataConstants.TAX_EXEMPT_10);

    // Taxes for line level are provided
    // taxID - {taxableAmtDraftAfterInsert, taxAmtDraftAfterInsert, taxableAmtCompletedAfterInsert,
    // taxAmtCompletedAfterInsert, taxableAmtDraftAfterUpdate, taxAmtDraftAfterUpdate,
    // taxableAmtCompletedAfterUpdate, taxAmtCompletedAfterUpdate}
    HashMap<String, String[]> lineTaxes = new HashMap<String, String[]>();
    lineTaxes.put(TaxDataConstants.TAX_EXEMPT_10,
        new String[]{ "3", "0", "3", "0", "6", "0", "6", "0" });
    line.setLinetaxes(lineTaxes);

    // Amounts for line level are provided
    // {totalGrossDraftAfterInsert, totalNetDraftAfterInsert, totalGrossCompletedAfterInsert,
    // totalNetCompletedAfterInsert, totalGrossDraftAfterUpdate, totalNetDraftAfterUpdate,
    // totalGrossCompletedAfterUpdate, totalNetCompletedAfterUpdate}
    String[] lineAmounts = new String[]{ "3", "3", "3", "3", "6", "6", "6", "6" };
    line.setLineAmounts(lineAmounts);

    // Add lines
    setLinesData(new TaxesLineTestData[]{ line });

    // Taxes for document level are provided
    // taxID - {taxableAmtDraftAfterInsert, taxAmtDraftAfterInsert, taxableAmtCompletedAfterInsert,
    // taxAmtCompletedAfterInsert, taxableAmtDraftAfterUpdate, taxAmtDraftAfterUpdate,
    // taxableAmtCompletedAfterUpdate, taxAmtCompletedAfterUpdate}
    HashMap<String, String[]> taxes = new HashMap<String, String[]>();
    taxes.put(TaxDataConstants.TAX_EXEMPT_10,
        new String[]{ "3", "0", "3", "0", "6", "0", "6", "0" });
    setDoctaxes(taxes);

    // Amounts for document level are provided
    // {totalGrossDraftAfterInsert, totalNetDraftAfterInsert, totalGrossCompletedAfterInsert,
    // totalNetCompletedAfterInsert, totalGrossDraftAfterUpdate, totalNetDraftAfterUpdate,
    // totalGrossCompletedAfterUpdate, totalNetCompletedAfterUpdate}
    String[] amounts = new String[]{ "3", "3", "3", "3", "6", "6", "6", "6" };
    setDocAmounts(amounts);
  }
}