package com.readify.controller;

import com.readify.service.BookService;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private static final int pageSizeDefault = 6;
    private final BookService bookService;

    public HomeController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value={"", "/"})
    public String listBooks(Model model, @RequestParam(value="page") Optional<Integer> page, @RequestParam(value="size") Optional<Integer> size) {
        return this.page(null, model, page, size);
    }

    @GetMapping(value={"/search"})
    public String searchBooks(@RequestParam(value="term") String term, Model model, @RequestParam(value="page") Optional<Integer> page, @RequestParam(value="size") Optional<Integer> size) {
        if (term.isBlank()) {
            return "redirect:/";
        }
        return this.page(term, model, page, size);
    }

    private String page(@RequestParam(value="term") String term, Model model, @RequestParam(value="page") Optional<Integer> page, @RequestParam(value="size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);
        Page bookPage = term == null ? this.bookService.findPaginated((Pageable)PageRequest.of((int)(currentPage - 1), (int)pageSize), null) : this.bookService.findPaginated((Pageable)PageRequest.of((int)(currentPage - 1), (int)pageSize), term);
        model.addAttribute("bookPage", (Object)bookPage);
        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            List pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "index";
    }
}

