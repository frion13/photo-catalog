package guru.qa.photocatalog.domain;

import guru.qa.photocatalog.domain.graphql.PhotoGql;

import java.util.Date;

public record Photo(String description,
                    Date lastModifyDate,
                    String content) {

    public static Photo fromGqlPhoto(PhotoGql photoGql){
        return new Photo(
                photoGql.description(),
                photoGql.lastModifyDate(),
                photoGql.content()
        );
    }
}
