package events;

import java.io.Serializable;

public record GameCreatedEvent(
         Long id,
         String title,
         Double price,
         String description,
         Long ownerId
) implements Serializable {
}
