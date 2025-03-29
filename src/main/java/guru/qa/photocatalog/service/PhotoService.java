package guru.qa.photocatalog.service;

import guru.qa.photocatalog.domain.Photo;
import guru.qa.photocatalog.domain.graphql.PhotoGql;
import guru.qa.photocatalog.domain.graphql.PhotoInputGql;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PhotoService {
    List<Photo> allPhotos();

    Slice<PhotoGql> allPhotosGql(Pageable pageable);

    Photo addPhotoGql(Photo photo);

    PhotoGql addPhotoGql(PhotoInputGql photo);

    List<PhotoGql> allGqlPhotos();

    Photo PhotoByDescription(String description);

    Photo photoById(String id);

    PhotoGql photoGqlById(String id);
}
