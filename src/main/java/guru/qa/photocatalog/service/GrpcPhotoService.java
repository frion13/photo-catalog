package guru.qa.photocatalog.service;

import com.google.protobuf.util.Timestamps;
import guru.qa.photocatalog.*;
import guru.qa.photocatalog.domain.graphql.PhotoGql;
import guru.qa.photocatalog.domain.graphql.PhotoInputGql;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class GrpcPhotoService extends PhotocatalogServiceGrpc.PhotocatalogServiceImplBase {
    private final PhotoService photoService;
    private static final Random rand = new Random();

    public GrpcPhotoService(PhotoService photoService) {
        this.photoService = photoService;
    }

    @Override
    public void photo(IdRequest request, StreamObserver<PhotoResponse> responseObserver) {
        final PhotoGql photoGql = photoService.photoGqlById(request.getId());

        responseObserver.onNext(
                PhotoResponse.newBuilder()
                        .setId(photoGql.id().toString())
                        .setDescription(photoGql.description())
                        .setLastModifyDate(Timestamps.fromDate(photoGql.lastModifyDate()))
                        .setContent(photoGql.content())
                        .build()
        );


        responseObserver.onCompleted();
    }

    @Override
    public void addPhoto(PhotoRequest request, StreamObserver<PhotoResponse> responseObserver) {
        final PhotoGql photoGql = photoService.addPhotoGql(new PhotoInputGql(
                request.getDescription(),
                request.getContent()
        ));

        responseObserver.onNext(
                PhotoResponse.newBuilder()
                        .setId(photoGql.id().toString())
                        .setDescription(photoGql.description())
                        .setLastModifyDate(Timestamps.fromDate(photoGql.lastModifyDate()))
                        .setContent(photoGql.content())
                        .build()
        );


        responseObserver.onCompleted();
    }

    @Override
    public void randomPhotos(CountRequest request, StreamObserver<PhotoResponse> responseObserver) {
        List<PhotoGql> photoGqls = photoService.allGqlPhotos();
        for (int i = 0; i < request.getCount(); i++) {
            PhotoGql randomPhoto = photoGqls.get(rand.nextInt(photoGqls.size()));
            responseObserver.onNext(
                    PhotoResponse.newBuilder()
                            .setId(randomPhoto.id().toString())
                            .setDescription(randomPhoto.description())
                            .setLastModifyDate(Timestamps.fromDate(randomPhoto.lastModifyDate()))
                            .setContent(randomPhoto.content())
                            .build());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        responseObserver.onCompleted();    }
}
