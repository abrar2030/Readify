<<<<<<< HEAD
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
=======
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
    // Specify the type for the ArrayList
    List<Book> cart = (List<Book>) this.session.getAttribute("cart");
    if (cart == null) {
      cart = new ArrayList<>(); // Specify type for new ArrayList
    }
    return cart;
  }

  public BigDecimal totalPrice() {
    BigDecimal shipping = new BigDecimal(this.shippingCosts);
    BigDecimal totalPriceWithShipping = new BigDecimal(0);
    List<Book> cart = this.getCart(); // Specify type for cart
    for (Book b : cart) {
      totalPriceWithShipping = totalPriceWithShipping.add(b.getPrice());
    }
    totalPriceWithShipping = totalPriceWithShipping.add(shipping);
    return totalPriceWithShipping;
  }

  public void emptyCart() {
    List<Book> cart = this.getCart(); // Specify type for cart
    cart.clear(); // Use clear method to empty the cart
  }

  public void deleteProductWithId(Long bookId) {
    List<Book> cart = this.getCart(); // Specify type for cart
    for (int i = 0; i < cart.size(); i++) { // Use < instead of <=
      if (cart.get(i).getId().equals(bookId)) { // Use equals for comparison
        cart.remove(i);
        break; // Exit after removing the book
      }
    }
  }

  public String getShippingCosts() { // Corrected method name casing
    return this.shippingCosts;
  }

  public HttpSession getSession() {
    return this.session;
  }
}
>>>>>>> 834bc75 (Update ReadMe File)
