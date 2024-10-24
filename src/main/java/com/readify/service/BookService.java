package com.readify.service;

import com.readify.model.Book;
import com.readify.repository.BookRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<Book> findPaginated(Pageable pageable, String term) {
        return this.page(pageable, term);
    }

    private Page<Book> page(Pageable pageable, String term) {
        List<Book> list; // Specify the type here
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        // Ensure books is of type List<Book>
        List<Book> books = term == null
                ? (List<Book>) this.bookRepository.findAll()
                : this.bookRepository.findByNameContaining(term);

        if (books.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            list = books.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, pageable, books.size());
    }

    public void save(Book book) {
        this.bookRepository.save(book); // No casting needed
    }

    public Optional<Book> findBookById(Long id) {
        return this.bookRepository.findById(id); // No casting needed
    }

    public void delete(Long id) {
        this.bookRepository.deleteById(id); // No casting needed
    }
}
