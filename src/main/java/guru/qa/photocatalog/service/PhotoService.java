package guru.qa.photocatalog.service;

import guru.qa.photocatalog.domain.Photo;

import java.util.List;

public interface PhotoService {
    List<Photo> allPhotos();

    Photo PhotoByDescription(String description);

    Photo byId (String id);
}
