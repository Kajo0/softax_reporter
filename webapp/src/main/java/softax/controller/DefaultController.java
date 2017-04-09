package softax.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String newRequest() {
        return "new_request";
    }

}
