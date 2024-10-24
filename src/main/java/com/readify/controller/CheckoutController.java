<<<<<<< HEAD
package com.readify.controller;

import com.readify.model.Customer;
import com.readify.service.BillingService;
import com.readify.service.EmailService;
import com.readify.service.ShoppingCartService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = {"/checkout"})
public class CheckoutController {
  private final BillingService billingService;
  private final EmailService emailService;
  private final ShoppingCartService shoppingCartService;

  public CheckoutController(
      BillingService billingService,
      EmailService emailService,
      ShoppingCartService shoppingCartService) {
    this.billingService = billingService;
    this.emailService = emailService;
    this.shoppingCartService = shoppingCartService;
  }

  @GetMapping(value = {"", "/"})
  public String checkout(Model model) {
    List cart = this.shoppingCartService.getCart();
    if (cart.isEmpty()) {
      return "redirect:/cart";
    }
    model.addAttribute("customer", (Object) new Customer());
    model.addAttribute("productsInCart", (Object) cart);
    model.addAttribute("totalPrice", (Object) this.shoppingCartService.totalPrice().toString());
    model.addAttribute("shippingCosts", (Object) this.shoppingCartService.getshippingCosts());
    return "checkout";
  }

  @PostMapping(value = {"/placeOrder"})
  public String placeOrder(
      @Valid Customer customer, BindingResult result, RedirectAttributes redirect) {
    if (result.hasErrors()) {
      return "/checkout";
    }
    this.billingService.createOrder(customer, this.shoppingCartService.getCart());
    this.emailService.sendEmail(
        customer.getEmail(), "bookstore - Order Confirmation", "Your order has been confirmed.");
    this.shoppingCartService.emptyCart();
    redirect.addFlashAttribute(
        "successMessage", (Object) "The order is confirmed, check your email.");
    return "redirect:/cart";
  }
}
=======
package com.readify.controller;

import com.readify.model.Book; // Make sure to import the Book model
import com.readify.model.Customer;
import com.readify.service.BillingService;
import com.readify.service.EmailService;
import com.readify.service.ShoppingCartService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = {"/checkout"})
public class CheckoutController {
  private final BillingService billingService;
  private final EmailService emailService;
  private final ShoppingCartService shoppingCartService;

  public CheckoutController(
          BillingService billingService,
          EmailService emailService,
          ShoppingCartService shoppingCartService) {
    this.billingService = billingService;
    this.emailService = emailService;
    this.shoppingCartService = shoppingCartService;
  }

  @GetMapping(value = {"", "/"})
  public String checkout(Model model) {
    List<Book> cart = this.shoppingCartService.getCart(); // Specify type for cart
    if (cart.isEmpty()) {
      return "redirect:/cart";
    }
    model.addAttribute("customer", new Customer());
    model.addAttribute("productsInCart", cart); // Remove unnecessary cast to Object
    model.addAttribute("totalPrice", this.shoppingCartService.totalPrice().toString());
    model.addAttribute("shippingCosts", this.shoppingCartService.getShippingCosts()); // Update method name
    return "checkout";
  }

  @PostMapping(value = {"/placeOrder"})
  public String placeOrder(
          @Valid Customer customer, BindingResult result, RedirectAttributes redirect) {
    if (result.hasErrors()) {
      return "checkout"; // Redirect to the same page
    }
    this.billingService.createOrder(customer, this.shoppingCartService.getCart());
    this.emailService.sendEmail(
            customer.getEmail(), "Bookstore - Order Confirmation", "Your order has been confirmed.");
    this.shoppingCartService.emptyCart();
    redirect.addFlashAttribute("successMessage", "The order is confirmed, check your email."); // Remove unnecessary cast to Object
    return "redirect:/cart";
  }
}
>>>>>>> 834bc75 (Update ReadMe File)
