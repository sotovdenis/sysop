package events;

import java.io.Serializable;

public record BookDeletedEvent(
        Long bookId
) implements Serializable {
}
