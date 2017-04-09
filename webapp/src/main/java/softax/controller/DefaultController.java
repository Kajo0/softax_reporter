package softax.controller;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import softax.model.FileToProcess;

@Controller
public class DefaultController {

    @RequestMapping("/")
    public String index(Model model) {
        // TODO remove
        model.addAttribute("files", Lists.newArrayList(FileToProcess.builder().path("a").status("b").build(),
                FileToProcess.builder().path("d").status("c").build()));
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
