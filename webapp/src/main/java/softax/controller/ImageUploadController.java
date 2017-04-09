package softax.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import softax.service.ImageService;

@RestController
@RequestMapping(value = "/upload", method = RequestMethod.POST)
public class ImageUploadController extends DefaultController {

    @Autowired
    private ImageService imageService;

//    @RequestMapping("/group-photo")
//    public BaseResponse<Group> uploadGroupPhoto(@RequestParam("groupId") Long groupId,
//            @RequestParam("file") MultipartFile file) {
//        Group group = imageService.uploadGroupPhoto(groupId, file);
//        return new OkResponse<>(group);
//    }

}
