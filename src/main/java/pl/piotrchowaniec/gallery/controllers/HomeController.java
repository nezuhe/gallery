package pl.piotrchowaniec.gallery.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.piotrchowaniec.gallery.models.utils.AttributeNames;
import pl.piotrchowaniec.gallery.models.utils.Mappings;
import pl.piotrchowaniec.gallery.models.utils.Messages;
import pl.piotrchowaniec.gallery.models.utils.ViewNames;

@Controller
public class HomeController extends BaseController {

    private final Messages messages;

    @Autowired
    public HomeController(Messages messages) {
        this.messages = messages;
    }

    @GetMapping(Mappings.HOME)
    public String home(Model model) {
        model.addAttribute(AttributeNames.TITLE, messages.getHomeTitle());
        model.addAttribute("homeClass", "active");
        return ViewNames.HOME;
    }
}
