package com.readify.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.readify.model.Book;
import com.readify.repository.BookRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class BookServiceTest {

  private final Long ID_1 = 1L;
  private final String NAME_1 = "The Lord of the Rings";
  private final BigDecimal PRICE_1 = new BigDecimal("99.99");
  private final String AUTHORS_1 = "J. R. R. Tolkien";
  private final String ISBN_1 = "978-0-261-10320-7";
  private final String PUBLISHER_1 = "Allen & Unwin";
  private final LocalDate DOB_1 = LocalDate.of(1954, 7, 29);

  private final Long ID_2 = 2L;
  private final String NAME_2 = "The Da Vinci Code";
  private final BigDecimal PRICE_2 = new BigDecimal("250.89");
  private final String AUTHORS_2 = "Dan Brown";
  private final String ISBN_2 = "0-385-50420-9";
  private final String PUBLISHER_2 = "Doubleday";
  private final LocalDate DOB_2 = LocalDate.of(2003, 4, 2);

  private final BookRepository bookRepository = mock(BookRepository.class);
  private final BookService bookService = new BookService(bookRepository);

  @Test
  void findPaginated_shouldReturnPaginatedBooksSearched() {
    String term = "The";
    Pageable pageable = PageRequest.of(0, 2);
    Book book1 = new Book(ID_1, NAME_1, PRICE_1, AUTHORS_1, ISBN_1, PUBLISHER_1, DOB_1);
    Book book2 = new Book(ID_2, NAME_2, PRICE_2, AUTHORS_2, ISBN_2, PUBLISHER_2, DOB_2);
    ArrayList<Book> books = new ArrayList<>(Arrays.asList(book1, book2));

    when(bookRepository.findByNameContaining(term)).thenReturn(books);

    Page<Book> bookPage = bookService.findPaginated(pageable, term);

    verify(bookRepository).findByNameContaining(term);
    assertThat(bookPage.getTotalElements()).isEqualTo(2);
    assertThat(bookPage.getContent()).containsExactly(book1, book2);
  }

  @Test
  void findPaginated_shouldReturnPaginatedBooksWhenTermIsNull() {
    Pageable pageable = PageRequest.of(0, 2);
    Book book1 = new Book(ID_1, NAME_1, PRICE_1, AUTHORS_1, ISBN_1, PUBLISHER_1, DOB_1);
    Book book2 = new Book(ID_2, NAME_2, PRICE_2, AUTHORS_2, ISBN_2, PUBLISHER_2, DOB_2);
    ArrayList<Book> books = new ArrayList<>(Arrays.asList(book1, book2));

    when(bookRepository.findAll()).thenReturn(books);

    Page<Book> bookPage = bookService.findPaginated(pageable, null);

    verify(bookRepository).findAll();
    assertThat(bookPage.getTotalElements()).isEqualTo(2);
    assertThat(bookPage.getContent()).containsExactly(book1, book2);
  }

  @Test
  void shouldSaveBook() {
    Book book = new Book(ID_1, NAME_1, PRICE_1, AUTHORS_1, ISBN_1, PUBLISHER_1, DOB_1);
    bookService.save(book);

    ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
    verify(bookRepository).save(bookArgumentCaptor.capture());

    Book capturedBook = bookArgumentCaptor.getValue();
    assertThat(capturedBook).isEqualTo(book);
  }

  @Test
  void shouldFindBookByID() {
    Book book = new Book(ID_1, NAME_1, PRICE_1, AUTHORS_1, ISBN_1, PUBLISHER_1, DOB_1);

    when(bookRepository.findById(ID_1)).thenReturn(Optional.of(book));

    assertThat(bookService.findBookById(ID_1)).isEqualTo(Optional.of(book));
  }

  @Test
  void shouldDeleteBook() {
    Book book = new Book(ID_1, NAME_1, PRICE_1, AUTHORS_1, ISBN_1, PUBLISHER_1, DOB_1);

    when(bookRepository.findById(ID_1)).thenReturn(Optional.of(book));
    bookService.delete(ID_1);
    verify(bookRepository).deleteById(ID_1);
  }
}
