package guru.qa.photocatalog.service;

import guru.qa.photocatalog.data.PhotoEntity;
import guru.qa.photocatalog.data.PhotoRepository;
import guru.qa.photocatalog.domain.Event;
import guru.qa.photocatalog.domain.EventType;
import guru.qa.photocatalog.domain.Photo;
import guru.qa.photocatalog.domain.graphql.PhotoGql;
import guru.qa.photocatalog.domain.graphql.PhotoInputGql;
import guru.qa.photocatalog.ex.PhotoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class DbPhotoService implements PhotoService {
    private final PhotoRepository photoRepository;
    private final KafkaTemplate<String, Event> kafkaTemplate;


    @Autowired
    public DbPhotoService(PhotoRepository photoRepository, KafkaTemplate<String, Event> kafkaTemplate) {
        this.photoRepository = photoRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<Photo> allPhotos() {
        return allGqlPhotos().stream()
                .map(gqlPhoto ->
                    new Photo(
                            gqlPhoto.description(),
                            gqlPhoto.lastModifyDate(),
                            gqlPhoto.content()
                    )
                ).toList();
    }

    @Override
    public Page<PhotoGql> allPhotosGql(Pageable pageable) {
        return photoRepository.findAll(pageable)
                .map(fe -> new PhotoGql(
                        fe.getId(),
                        fe.getDescription(),
                        fe.getLastModifyDate(),
                        fe.getContent() != null ? new String(fe.getContent()) : ""
                ));
    }

    @Override
    public PhotoGql addPhotoGql(PhotoGql photo) {
        final Date date = new Date();

        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setContent(photo.content().getBytes());
        photoEntity.setDescription(photo.description());
        photoEntity.setLastModifyDate(date);
        PhotoEntity saved = photoRepository.save(photoEntity);
        kafkaTemplate.send("events", new Event(
                date,
                EventType.NEW
        ));
        return new PhotoGql(
                saved.getId(),
                saved.getDescription(),
                saved.getLastModifyDate(),
                new String(saved.getContent())
        );
    }

    @Override
    public Photo addPhoto(Photo photo) {
        final Date date = new Date();

        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setContent(photo.content().getBytes());
        photoEntity.setDescription(photo.description());
        photoEntity.setLastModifyDate(date);
        PhotoEntity saved = photoRepository.save(photoEntity);
        kafkaTemplate.send("events", new Event(
                date,
                EventType.NEW
        ));
        return new Photo(
                saved.getDescription(),
                saved.getLastModifyDate(),
                new String(saved.getContent())
        );
    }

    @Override
    public PhotoGql addPhotoGql(PhotoInputGql photo) {
        PhotoEntity pe = new PhotoEntity();
        pe.setDescription(photo.description());
        pe.setLastModifyDate(new Date());
        pe.setContent(photo.content().getBytes(StandardCharsets.UTF_8));
        PhotoEntity saved = photoRepository.save(pe);
        return new PhotoGql(
                saved.getId(),
                saved.getDescription(),
                saved.getLastModifyDate(),
                new String(saved.getContent())
        );
    }


    @Override
    public List<PhotoGql> allGqlPhotos() {
        return photoRepository.findAll().stream()
                .map(fe -> new PhotoGql(
                        fe.getId(),
                        fe.getDescription(),
                        fe.getLastModifyDate(),
                        fe.getContent() != null ? new String(fe.getContent()) : ""
                )).toList();
    }

    @Override
    public Photo PhotoByDescription(String description) {
        return null;
    }

    @Override
    public Photo photoById(String id) {
        return Photo.fromGqlPhoto(photoGqlById(id));
    }

    @Override
    public PhotoGql photoGqlById(String id) {
        return photoRepository.findById(UUID.fromString(id))
                .map(fe -> new PhotoGql(
                        fe.getId(),
                        fe.getDescription(),
                        fe.getLastModifyDate(),
                        fe.getContent() != null ? new String(fe.getContent()) : ""
                )).orElseThrow(PhotoNotFoundException::new);
    }
}
