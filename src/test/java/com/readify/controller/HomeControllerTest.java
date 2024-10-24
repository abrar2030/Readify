package com.readify.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.readify.model.Book;
import com.readify.service.BookService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

class HomeControllerTest {

  private final BookService bookService = mock(BookService.class);
  private final HomeController homeController = new HomeController(bookService);

  @Test
  void findPaginated_shouldReturnPaginatedBooksWhenTermIsNull() {
    Model model = new BindingAwareModelMap();
    Optional<Integer> page = Optional.of(1);
    Optional<Integer> size = Optional.of(10);

    Page<Book> bookPage = new PageImpl<>(Arrays.asList(new Book(), new Book()));
    when(bookService.findPaginated(PageRequest.of(0, 10), null)).thenReturn(bookPage);

    String result = homeController.listBooks(model, page, size);

    assertThat(result).isEqualTo("index");
    assertThat(model.asMap().get("bookPage")).isEqualTo(bookPage);
    assertThat(model.asMap().get("pageNumbers")).isEqualTo(List.of(1));
  }

  @Test
  void testSearchBooksWithBlankTerm() {
    String term = "";
    Model model = new BindingAwareModelMap();
    Optional<Integer> page = Optional.empty();
    Optional<Integer> size = Optional.empty();

    String result = homeController.searchBooks(term, model, page, size);

    assertThat("redirect:/").isEqualTo(result);
  }
}
