package softax.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import softax.service.FileUploadService;

@Controller
public class DefaultController {

    @Autowired
    private FileUploadService fileUploadService;

    @Value("${multipart.maxFileSizeByteNum}")
    private Long maxFileSize;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("files", fileUploadService.loadProcessFiles());

        return "home";
    }

    @RequestMapping("/new")
    public String newRequest(Model model) {
        model.addAttribute("maxFileSize", maxFileSize);

        return "new_request";
    }

}
