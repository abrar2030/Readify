package com.readify.service;

import com.readify.model.Book;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {
  @Value(value = "${shipping.costs}")
  private String shippingCosts;

  private HttpSession session;

  public ShoppingCartService(HttpSession session) {
    this.session = session;
  }

  public List<Book> getCart() {
    ArrayList cart = (ArrayList) this.session.getAttribute("cart");
    if (cart == null) {
      cart = new ArrayList();
    }
    return cart;
  }

  public BigDecimal totalPrice() {
    BigDecimal shipping = new BigDecimal(this.shippingCosts);
    BigDecimal totalPriceWithShipping = new BigDecimal(0);
    List cart = this.getCart();
    for (Book b : cart) {
      totalPriceWithShipping = totalPriceWithShipping.add(b.getPrice());
    }
    totalPriceWithShipping = totalPriceWithShipping.add(shipping);
    return totalPriceWithShipping;
  }

  public void emptyCart() {
    List cart = this.getCart();
    cart.removeAll(cart);
  }

  public void deleteProductWithId(Long bookId) {
    List cart = this.getCart();
    for (int i = 0; i <= cart.size(); ++i) {
      if (((Book) cart.get(i)).getId() != bookId) continue;
      cart.remove(cart.get(i));
    }
  }

  public String getshippingCosts() {
    return this.shippingCosts;
  }

  public HttpSession getSession() {
    return this.session;
  }
}
