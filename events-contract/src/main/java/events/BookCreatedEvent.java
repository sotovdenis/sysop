package events;

import java.io.Serializable;

public record BookCreatedEvent(
        Long bookId,
        String title,
        String authorFullName
) implements Serializable {}
