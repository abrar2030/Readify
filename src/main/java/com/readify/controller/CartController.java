package com.readify.controller;

import com.readify.model.Book;
import com.readify.service.BookService;
import com.readify.service.ShoppingCartService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/cart"})
public class CartController {
    private final BookService bookService;
    private final ShoppingCartService shoppingCartService;

    public CartController(BookService bookService, ShoppingCartService shoppingCartService) {
        this.bookService = bookService;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping(value={"", "/"})
    public String shoppingCart(Model model) {
        model.addAttribute("cart", (Object)this.shoppingCartService.getCart());
        return "cart";
    }

    @GetMapping(value={"/add/{id}"})
    public String addToCart(@PathVariable(value="id") Long id, RedirectAttributes redirect) {
        List cart = this.shoppingCartService.getCart();
        Book book = (Book)this.bookService.findBookById(id).get();
        if (book != null) {
            cart.add(book);
        }
        this.shoppingCartService.getSession().setAttribute("cart", (Object)cart);
        redirect.addFlashAttribute("successMessage", (Object)"Added book successfully!");
        return "redirect:/cart";
    }

    @GetMapping(value={"/remove/{id}"})
    public String removeFromCart(@PathVariable(value="id") Long id, RedirectAttributes redirect) {
        Book book = (Book)this.bookService.findBookById(id).get();
        if (book != null) {
            this.shoppingCartService.deleteProductWithId(id);
        }
        redirect.addFlashAttribute("successMessage", (Object)"Removed book successfully!");
        return "redirect:/cart";
    }

    @GetMapping(value={"/remove/all"})
    public String removeAllFromCart() {
        List cart = this.shoppingCartService.getCart();
        cart.removeAll(cart);
        return "redirect:/cart";
    }
}

