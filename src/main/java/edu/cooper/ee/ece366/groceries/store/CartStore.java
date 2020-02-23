package edu.cooper.ee.ece366.groceries.store;

import edu.cooper.ee.ece366.groceries.model.Cart;
import edu.cooper.ee.ece366.groceries.model.Item;
import edu.cooper.ee.ece366.groceries.model.User;

public interface CartStore {

  /**
   * @return a map from the {@link Item} to the Integer quantity.
   */
  Cart getCart(User user);

  void addItem(User user, Item item);

  void addItem(User user, Item item, Integer quantity);

  Long checkout();
}
