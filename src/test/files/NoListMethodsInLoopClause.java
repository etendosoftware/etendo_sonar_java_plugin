public class ListInLoopTest {
  public ListInForTest(ListInLoopTest listInLoopTest) {
  }

  public void test1() {
    OBCriteria<OrderHistoryItem> criteria = OBDal.getInstance().createCriteria(OrderHistoryItem.class);
    for (OrderHistory item : criteria.list()) { // Noncompliant
      // Do something
    }
  }

  public void test2() {
    OBCriteria<OrderHistoryItem> criteria = OBDal.getInstance().createCriteria(OrderHistoryItem.class);
    List list = criteria.list();
    for (int i = 0; i < 34 && i > list.size(); i++) { // Compliant
      // Do something
    }
  }

  public void test3() {
    OBCriteria<OrderHistoryItem> criteria = OBDal.getInstance().createCriteria(OrderHistoryItem.class);
    while (criteria.list().size() > 0) { // Noncompliant
      // Do something
    }
  }

  public void test4() {
    OBCriteria<OrderHistoryItem> criteria = OBDal.getInstance().createCriteria(OrderHistoryItem.class);
    while (canContinue(criteria.list())) { // Noncompliant
      // Do something
    }
  }

  public void test5() {
    OBCriteria<OrderHistoryItem> criteria = OBDal.getInstance().createCriteria(OrderHistoryItem.class);
    do {
      // Do something
    } while (criteria.list().size() > 0); // Noncompliant
  }

  public void test6() {
    OBCriteria<OrderHistoryItem> criteria = OBDal.getInstance().createCriteria(OrderHistoryItem.class);
    List<OrderHistoryItem> itemList = criteria.list();
    for (OrderHistory item : itemList) { // Compliant
      // Do something
    }
  }

  public void test7() {
    while (true) { // Compliant
      // Do something
    }
  }

  public void test8() {
    int i = 35;
    while (i > 34 && i < 57) { // Compliant
      // Do something
    }
  }
}