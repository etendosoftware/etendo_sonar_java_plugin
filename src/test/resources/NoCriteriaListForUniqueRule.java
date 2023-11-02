class CriteriaTest {
  CriteriaTest(CriteriaTest ct) {
  }

  public Product getProductUsingUnique(String prodId) {
    OBCriteria<Product> prodCriteria = OBDal.getInstance().createCriteria(Product.class);
    prodCriteria.add(Restrictions.eq(Product.PROPERTY_ID, prodId));
    prodCriteria.setMaxResults(1);

    return prodCriteria.uniqueResult(); // Compliant - uniqueResult is being used here
  }

  public Product getInvoiceUsingList(String invoiceId) {
    OBCriteria<Invoice> invCriteria = OBDal.getInstance().createCriteria(Invoice.class);
    invCriteria.add(Restrictions.eq(Invoice.PROPERTY_ID, invoiceId));

    return invCriteria.list().get(0); // Noncompliant [[sc=24;ec=28]]
  }

  public List<Product> getInvoicesMatchingDate(Date date) {
    OBCriteria<Invoice> invCriteria = OBDal.getInstance().createCriteria(Invoice.class);
    invCriteria.add(Restrictions.eq(Invoice.PROPERTY_CREATED, date));

    return invCriteria.list(); // Compliant - This method is meant to retrieve a list of results!
  }

  public Invoice testReallyLongAndConvolutedInvocationChain(String invoiceId) {
    return OBDal.getInstance().createCriteria(Invoice.class).add(Restrictions.eq(Invoice.PROPERTY_ID, invoiceId)).list().get(0); // Noncompliant [[sc=115;ec=119]]
  }
}