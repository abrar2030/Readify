package com.readify.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

class BookControllerTest {

  private final BookService bookService = mock(BookService.class);
  private final BookController bookController = new BookController(bookService);

  @Test
  void findPaginated_shouldReturnListOfBooksWhenTermIsNull() {
    Model model = new BindingAwareModelMap();
    Optional<Integer> page = Optional.of(1);
    Optional<Integer> size = Optional.of(10);

    Page<Book> bookPage = new PageImpl<>(Arrays.asList(new Book(), new Book()));
    when(bookService.findPaginated(PageRequest.of(0, 10), null)).thenReturn(bookPage);

    String result = bookController.getAllBooks(model, page, size);

    assertThat(result).isEqualTo("list");
    assertThat(model.asMap().get("bookPage")).isEqualTo(bookPage);
    assertThat(model.asMap().get("pageNumbers")).isEqualTo(List.of(1));
  }

  @Test
  void testSearchBooksWithBlankTerm() {
    String term = "";
    Model model = new BindingAwareModelMap();
    Optional<Integer> page = Optional.empty();
    Optional<Integer> size = Optional.empty();

    String result = bookController.searchBooks(term, model, page, size);

    assertThat("redirect:/book").isEqualTo(result);
  }

  @Test
  void testAddBook() {
    Model model = new BindingAwareModelMap();

    String result = bookController.addBook(model);

    assertThat(result).isEqualTo("form");
    assertThat(model.asMap().get("book")).isNotNull().isInstanceOf(Book.class);
  }

  @Test
  void testSaveBookSuccess() {
    Book book = new Book();
    BindingResult bindingResult = new BeanPropertyBindingResult(book, "book");
    RedirectAttributes redirect = new RedirectAttributesModelMap();

    String result = bookController.saveBook(book, bindingResult, redirect);

    verify(bookService).save(book);
    assertThat(result).isEqualTo("redirect:/book");
    assertThat(redirect.getFlashAttributes().get("successMessage"))
        .isEqualTo("Saved book successfully!");
  }

  @Test
  void testSaveBookFailure() {
    Book book = new Book();
    BindingResult bindingResult = new BeanPropertyBindingResult(book, "book");
    bindingResult.addError(new ObjectError("name", "Name cannot be empty."));
    RedirectAttributes redirect = new RedirectAttributesModelMap();

    String result = bookController.saveBook(book, bindingResult, redirect);

    verify(bookService, never()).save(book);
    assertThat(result).isEqualTo("form");
  }

  @Test
  void testEditBook() {
    Long id = 1L;
    Optional<Book> book = Optional.of(new Book());
    when(bookService.findBookById(id)).thenReturn(book);
    Model model = new BindingAwareModelMap();

    String result = bookController.editBook(id, model);

    verify(bookService).findBookById(id);
    assertThat(result).isEqualTo("form");
    assertThat(model.asMap().get("book")).isNotNull().isEqualTo(book);
  }

  @Test
  void testDeleteBookById() {
    Long id = 1L;
    RedirectAttributes redirect = mock(RedirectAttributes.class);

    String result = bookController.deleteBook(id, redirect);

    verify(bookService).delete(id);
    verify(redirect).addFlashAttribute("successMessage", "Deleted book successfully!");
    assertThat("redirect:/book").isEqualTo(result);
  }
}
