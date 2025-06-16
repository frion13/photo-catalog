package guru.qa.photocatalog.domain;

import java.util.Date;

public record Event(Date date,
                    EventType eventType) {
}
