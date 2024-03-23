package ru.vsu.cs.yurov.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.yurov.models.Book;
import ru.vsu.cs.yurov.models.Person;
import ru.vsu.cs.yurov.services.BooksService;
import ru.vsu.cs.yurov.services.PeopleService;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public BooksController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping()
    public String index(@RequestParam(name = "page", required = false) Integer page,
                        @RequestParam(name="books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(name = "sort_by_year", required = false, defaultValue = "false") String sort,
                        Model model) {
        model.addAttribute("books", booksService.index(page, booksPerPage, sort));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Book book = booksService.show(id);

        if (book.isAvailable()) {
            model.addAttribute("newHolder", new Person());
        }
        model.addAttribute("book", book);
        model.addAttribute("people", peopleService.index());
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.show(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book updatedBook,
                         BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        booksService.update(id, updatedBook);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/holder")
    public String setHolder(@PathVariable("id") int bookId,
                            @ModelAttribute("holder") Person newHolder) {
        booksService.setHolder(bookId, newHolder);
        return "redirect:/books/" + bookId;
    }

    @DeleteMapping("/{id}/holder")
    public String deleteHolder(@PathVariable("id") int bookId) {
        booksService.deleteHolder(bookId);
        return "redirect:/books/" + bookId;
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "searchQuery", required = false) String query, Model model) {
        if (query != null) {
            model.addAttribute("books", booksService.findByQuery(query));
        }
        return "books/search";
    }
}
