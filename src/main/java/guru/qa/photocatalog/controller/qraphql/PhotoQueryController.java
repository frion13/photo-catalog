package guru.qa.photocatalog.controller.qraphql;

import guru.qa.photocatalog.domain.graphql.PhotoGql;
import guru.qa.photocatalog.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("api/photo")
public class PhotoQueryController {
    private final PhotoService photoService;


    @Autowired
    public PhotoQueryController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @QueryMapping
    public Slice<PhotoGql> photos(@Argument int page, @Argument int size) {
        return photoService.allPhotosGql(PageRequest.of(
                page, size));
    }

    @QueryMapping
    public PhotoGql photo(@Argument String id) {
        return photoService.photoGqlById(id);
    }
}
