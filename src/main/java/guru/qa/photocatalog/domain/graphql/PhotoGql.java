package guru.qa.photocatalog.domain.graphql;

import java.util.Date;
import java.util.UUID;

public record PhotoGql(UUID id,
                       String description,
                       Date lastModifyDate,
                       String content) {
}
