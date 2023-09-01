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

    return invCriteria.list().get(0); // Noncompliant {{Make this single result retrieval using setMaxResults and uniqueResult}}
  }

  public List<Product> getInvoicesMatchingDate(Date date) {
    OBCriteria<Invoice> invCriteria = OBDal.getInstance().createCriteria(Invoice.class);
    invCriteria.add(Restrictions.eq(Invoice.PROPERTY_CREATED, date));

    return invCriteria.list(); // Compliant - This method is meant to retrieve a list of results!
  }
}