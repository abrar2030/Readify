package com.readify.controller;

import com.readify.model.Book;
import com.readify.service.BookService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = {"/book"})
public class BookController {
  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping(value = {"", "/"})
  public String getAllBooks(
      Model model,
      @RequestParam(value = "page") Optional<Integer> page,
      @RequestParam(value = "size") Optional<Integer> size) {
    return this.page(null, model, page, size);
  }

  @GetMapping(value = {"/search"})
  public String searchBooks(
      @RequestParam(value = "term") String term,
      Model model,
      @RequestParam(value = "page") Optional<Integer> page,
      @RequestParam(value = "size") Optional<Integer> size) {
    if (term.isBlank()) {
      return "redirect:/book";
    }
    return this.page(term, model, page, size);
  }

  private String page(
      @RequestParam(value = "term") String term,
      Model model,
      @RequestParam(value = "page") Optional<Integer> page,
      @RequestParam(value = "size") Optional<Integer> size) {
    int currentPage = page.orElse(1);
    int pageSize = size.orElse(10);
    Page bookPage =
        term == null
            ? this.bookService.findPaginated(
                (Pageable) PageRequest.of((int) (currentPage - 1), (int) pageSize), null)
            : this.bookService.findPaginated(
                (Pageable) PageRequest.of((int) (currentPage - 1), (int) pageSize), term);
    model.addAttribute("bookPage", (Object) bookPage);
    int totalPages = bookPage.getTotalPages();
    if (totalPages > 0) {
      List pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    return "list";
  }

  @GetMapping(value = {"/add"})
  public String addBook(Model model) {
    model.addAttribute("book", (Object) new Book());
    return "form";
  }

  @PostMapping(value = {"/save"})
  public String saveBook(@Valid Book book, BindingResult result, RedirectAttributes redirect) {
    if (result.hasErrors()) {
      return "form";
    }
    this.bookService.save(book);
    redirect.addFlashAttribute("successMessage", (Object) "Saved book successfully!");
    return "redirect:/book";
  }

  @GetMapping(value = {"/edit/{id}"})
  public String editBook(@PathVariable(value = "id") Long id, Model model) {
    model.addAttribute("book", (Object) this.bookService.findBookById(id));
    return "form";
  }

  @GetMapping(value = {"/delete/{id}"})
  public String deleteBook(@PathVariable Long id, RedirectAttributes redirect) {
    this.bookService.delete(id);
    redirect.addFlashAttribute("successMessage", (Object) "Deleted book successfully!");
    return "redirect:/book";
  }
}
