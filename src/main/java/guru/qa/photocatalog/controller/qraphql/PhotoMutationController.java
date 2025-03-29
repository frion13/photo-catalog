package guru.qa.photocatalog.controller.qraphql;

import guru.qa.photocatalog.domain.graphql.PhotoGql;
import guru.qa.photocatalog.domain.graphql.PhotoInputGql;
import guru.qa.photocatalog.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("api/photo")
public class PhotoMutationController {
    private final PhotoService photoService;


    @Autowired
    public PhotoMutationController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @MutationMapping
    public PhotoGql addPhoto(@Argument PhotoInputGql input) {
        return photoService.addPhotoGql(input);
    }
}
