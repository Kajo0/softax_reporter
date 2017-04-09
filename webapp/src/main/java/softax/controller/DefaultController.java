package softax.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softax.service.FileUploadService;

@Controller
public class DefaultController {

    @Autowired
    private FileUploadService fileUploadService;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("files", fileUploadService.loadProcessFiles());

        return "home";
    }

    @RequestMapping("/new")
    public String newRequest(@RequestParam(value = "name", required = false, defaultValue = "World") String name,
            Model model) {
        // TODO remove
        model.addAttribute("name", name);


        return "new_request";
    }
}
