package pl.piotrchowaniec.gallery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.piotrchowaniec.gallery.models.forms.LoginForm;
import pl.piotrchowaniec.gallery.models.services.UserService;
import pl.piotrchowaniec.gallery.models.utils.AttributeNames;
import pl.piotrchowaniec.gallery.models.utils.Mappings;
import pl.piotrchowaniec.gallery.models.utils.Messages;
import pl.piotrchowaniec.gallery.models.utils.ViewNames;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final UserService userService;
    private final Messages messages;

    @Autowired
    public LoginController(UserService userService, Messages messages) {
        this.userService = userService;
        this.messages = messages;
    }

    @GetMapping(Mappings.LOGIN)
    public String login(Model model) {
        model.addAttribute(AttributeNames.LOGIN_FORM, new LoginForm());
        return ViewNames.INDEX;
    }

    @PostMapping(Mappings.LOGIN)
    public String login(@ModelAttribute LoginForm loginForm,
                        HttpSession httpSession,
                        Model model) {
        if (!userService.logIn(loginForm)) {
            model.addAttribute(AttributeNames.ERROR_LOGIN_MESSAGE, messages.getMessageLoginError());
            return ViewNames.INDEX;
        }
        userService.setUserSessionId(httpSession.getId());
        return "redirect:/" + Mappings.HOME;
    }

    @GetMapping(Mappings.LOGOUT)
    public String logout() {
        userService.logOut();
        return "redirect:/";
    }
}
