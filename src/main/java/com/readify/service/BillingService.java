package com.readify.service;

import com.readify.model.Book;
import com.readify.model.Customer;
import com.readify.model.CustomerBooks;
import com.readify.model.Order;
import com.readify.repository.BillingRepository;
import com.readify.repository.OrderRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BillingService {
  private final OrderRepository orderRepository;
  private final BillingRepository billingRepository;

  public BillingService(OrderRepository orderRepository, BillingRepository billingRepository) {
    this.orderRepository = orderRepository;
    this.billingRepository = billingRepository;
  }

  public Page<CustomerBooks> findPaginated(Pageable pageable, String term) {
    return this.page(pageable, term);
  }

  private Page<CustomerBooks> page(Pageable pageable, String term) {
    List<CustomerBooks> list; // Specify type
    int pageSize = pageable.getPageSize();
    int currentPage = pageable.getPageNumber();
    int startItem = currentPage * pageSize;
    List<Order> orders; // Specify type
    if (term == null) {
      orders = (List<Order>) this.orderRepository.findAll(); // No need for casting
    } else {
      LocalDate date = LocalDate.parse(term);
      orders = this.orderRepository.findByOrderDate(date);
    }

    Map<Customer, List<Book>> customerBooksMap =
        orders.stream()
            .collect(
                Collectors.groupingBy(
                    Order::getCustomer, Collectors.mapping(Order::getBook, Collectors.toList())));

    list =
        customerBooksMap.entrySet().stream()
            .map(entry -> new CustomerBooks(entry.getKey(), entry.getValue())) // No need for casts
            .collect(Collectors.toList());

    if (list.size() < startItem) {
      return new PageImpl<>(Collections.emptyList(), pageable, list.size());
    }

    int toIndex = Math.min(startItem + pageSize, list.size());
    List<CustomerBooks> subList = list.subList(startItem, toIndex);
    return new PageImpl<>(subList, pageable, list.size());
  }

  @Transactional
  public void createOrder(Customer customer, List<Book> books) {
    this.billingRepository.save(customer); // No casting
    for (Book b : books) {
      Order order = new Order();
      order.setCustomer(customer);
      order.setOrderDate(LocalDate.now());
      order.setBook(b);
      this.orderRepository.save(order); // No casting
    }
  }

  public List<CustomerBooks> findOrdersByCustomerId(Long id) {
    List<Order> orders = (List<Order>) this.orderRepository.findAll(); // Specify type
    Map<Customer, List<Book>> customerBooksMap =
        orders.stream()
            .collect(
                Collectors.groupingBy(
                    Order::getCustomer, Collectors.mapping(Order::getBook, Collectors.toList())));

    List<CustomerBooks> customerBooks =
        customerBooksMap.entrySet().stream()
            .map(entry -> new CustomerBooks(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());

    return customerBooks.stream()
        .filter(c -> c.getCustomer().getId().equals(id))
        .collect(Collectors.toList());
  }
}