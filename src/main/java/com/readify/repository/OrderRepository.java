package com.readify.repository;

import com.readify.model.Order;
import java.time.LocalDate;
import java.util.ArrayList;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
  public ArrayList<Order> findByOrderDate(LocalDate var1);

  public ArrayList<Order> findOrdersById(Long var1);
}
