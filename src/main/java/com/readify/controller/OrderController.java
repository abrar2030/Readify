<<<<<<< HEAD
package com.readify.controller;

import com.readify.model.Customer;
import com.readify.model.CustomerBooks;
import com.readify.service.BillingService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = {"/orders"})
public class OrderController {
  private BillingService billingService;

  public OrderController(BillingService billingService) {
    this.billingService = billingService;
  }

  @GetMapping(value = {"", "/"})
  public String getAllOrders(
      Model model,
      @RequestParam(value = "page") Optional<Integer> page,
      @RequestParam(value = "size") Optional<Integer> size) {
    return this.page(null, model, page, size);
  }

  @GetMapping(value = {"/search"})
  public String searchOrders(
      @RequestParam(value = "term") String term,
      Model model,
      @RequestParam(value = "page") Optional<Integer> page,
      @RequestParam(value = "size") Optional<Integer> size) {
    if (term.isBlank()) {
      return "redirect:/orders";
    }
    return this.page(term, model, page, size);
  }

  @GetMapping(value = {"/{id}"})
  public String showSpecificOrder(@PathVariable(value = "id") Long id, Model model) {
    List customerBooks = this.billingService.findOrdersByCustomerId(id);
    Customer customer = null;
    List books = null;
    for (CustomerBooks c : customerBooks) {
      customer = c.getCustomer();
      books = c.getBooks();
    }
    model.addAttribute("customer", customer);
    model.addAttribute("books", books);
    return "order";
  }

  private String page(
      @RequestParam(value = "term") String term,
      Model model,
      @RequestParam(value = "page") Optional<Integer> page,
      @RequestParam(value = "size") Optional<Integer> size) {
    int currentPage = page.orElse(1);
    int pageSize = size.orElse(10);
    Page orderPage =
        term == null
            ? this.billingService.findPaginated(
                (Pageable) PageRequest.of((int) (currentPage - 1), (int) pageSize), null)
            : this.billingService.findPaginated(
                (Pageable) PageRequest.of((int) (currentPage - 1), (int) pageSize), term);
    model.addAttribute("orderPage", (Object) orderPage);
    int totalPages = orderPage.getTotalPages();
    if (totalPages > 0) {
      List pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    return "orders";
  }
}
=======
package com.readify.controller;

import com.readify.model.Book;
import com.readify.model.Customer;
import com.readify.model.CustomerBooks;
import com.readify.service.BillingService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = {"/orders"})
public class OrderController {
  private final BillingService billingService;

  public OrderController(BillingService billingService) {
    this.billingService = billingService;
  }

  @GetMapping(value = {"", "/"})
  public String getAllOrders(
          Model model,
          @RequestParam(value = "page", defaultValue = "1") Optional<Integer> page,
          @RequestParam(value = "size", defaultValue = "10") Optional<Integer> size) {
    return this.page(null, model, page, size);
  }

  @GetMapping(value = {"/search"})
  public String searchOrders(
          @RequestParam(value = "term") String term,
          Model model,
          @RequestParam(value = "page", defaultValue = "1") Optional<Integer> page,
          @RequestParam(value = "size", defaultValue = "10") Optional<Integer> size) {
    if (term.isBlank()) {
      return "redirect:/orders";
    }
    return this.page(term, model, page, size);
  }

  @GetMapping(value = {"/{id}"})
  public String showSpecificOrder(@PathVariable(value = "id") Long id, Model model) {
    List<CustomerBooks> customerBooks = this.billingService.findOrdersByCustomerId(id);
    Customer customer = null;
    List<Book> books = null;

    if (!customerBooks.isEmpty()) {
      CustomerBooks cb = customerBooks.get(0); // Assuming the first entry contains the needed information
      customer = cb.getCustomer();
      books = cb.getBooks();
    }

    model.addAttribute("customer", customer);
    model.addAttribute("books", books);
    return "order";
  }

  private String page(
          @RequestParam(value = "term", required = false) String term,
          Model model,
          @RequestParam(value = "page") Optional<Integer> page,
          @RequestParam(value = "size") Optional<Integer> size) {
    int currentPage = page.orElse(1);
    int pageSize = size.orElse(10);
    Page<CustomerBooks> orderPage =
            term == null
                    ? this.billingService.findPaginated(PageRequest.of(currentPage - 1, pageSize), null)
                    : this.billingService.findPaginated(PageRequest.of(currentPage - 1, pageSize), term);

    model.addAttribute("orderPage", orderPage);
    int totalPages = orderPage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    return "orders";
  }
}
>>>>>>> 834bc75 (Update ReadMe File)
