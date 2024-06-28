package com.example.test.controller;

import com.example.test.model.BooksModel;
import com.example.test.model.UsersModel;
import com.example.test.service.BooksService;
import com.example.test.service.UsersService;
import jakarta.persistence.GeneratedValue;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UsersController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private BooksService booksService;

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        model.addAttribute("registerRequest", new UsersModel());
        return "rejestracja1";
    }
    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginRequest",new UsersModel());
        return "logowanie1";
    }

    @GetMapping("/borrowed")
    public String getBorrowedPage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");  // Pobierasz userId z sesji
        if (userId != null) {
            List<BooksModel> books = usersService.getUserBooks(userId);  // Pobierasz listę książek użytkownika
            model.addAttribute("books", books);
            return "wypozyczone";
        } else {
            return "redirect:/login";  // Jeśli userId jest null, przekierowujesz do strony logowania
        }
    }

    @GetMapping("/userBooks")
    public String getUserBooksPage(@RequestParam("id") int id, Model model) {
        // Tutaj pobierz szczegółowe informacje o książce na podstawie jej identyfikatora
        List<BooksModel> books = usersService.getUserBooks(id);

        // Przekazanie informacji o książce do widoku
        model.addAttribute("books", books);
        model.addAttribute("userId", id);
        return "ksiazkiuzytkownika";
    }
    @GetMapping("/userList")
    public String getUserListPage(Model model){
        model.addAttribute("users", usersService.getAllUsers());
        return "listauzytkownikow";
    }

    @GetMapping("/payment")
    public String getPaymentPage(Model model){
        //model.addAttribute("users", usersService.getAllUsers());
        return "payment";
    }
    @GetMapping("/help")
    public String getHelpPage(Model model){
        //model.addAttribute("users", usersService.getAllUsers());
        return "help";
    }
    @GetMapping("/menu")
    public String getMenuPage(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");  // Pobierasz userId z sesji
        if (userId != null) {
            UsersModel user = usersService.findUserById(userId);  // Pobierasz pełne dane użytkownika
            model.addAttribute("user", user);
            model.addAttribute("books",booksService.getAllBooks());
            return "menu";
        } else {
            return "redirect:/login";  // Jeśli userId jest null, przekierowujesz do strony logowania
        }
    }
//    @GetMapping("/menu")
//    public String getMenuPage( Model model) {
//       // UsersModel user = usersService.findUserById(userId);
//       // model.addAttribute("user", user);
//        return "menu";
//    }
@GetMapping("/menuAdmin")
public String getAdminPage(HttpSession session, Model model) {
    Integer userId = (Integer) session.getAttribute("userId");
    if (userId != null) {
        UsersModel user = usersService.findUserById(userId);
        model.addAttribute("user", user);
        return "paneladmina";
    } else {
        return "redirect:/login";
    }
}


    @PostMapping("/register")
    public String register(@ModelAttribute UsersModel usersModel){
        System.out.println("register request: "+ usersModel);
        UsersModel registeredUser = usersService.registerUser(usersModel.getEmail(),usersModel.getPassword(),usersModel.getfName(),usersModel.getlName());
        return registeredUser == null ? "errorPage" : "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UsersModel usersModel, HttpSession session) {
        UsersModel authenticated = usersService.authenticate(usersModel.getEmail(), usersModel.getPassword());
        if (authenticated != null) {
            session.setAttribute("userId", authenticated.getId());  // Zapisujesz userId w sesji
            if (authenticated.getRole().equals("user")) {
                return "redirect:/menu";
            } else {
                return "redirect:/menuAdmin";
            }
        } else {
            return "errorPage";
        }
    }

}
