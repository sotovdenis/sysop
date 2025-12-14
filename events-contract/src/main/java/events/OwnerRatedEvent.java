package events;

import java.io.Serializable;

public record OwnerRatedEvent(Long userId, Integer score, String verdict) implements Serializable {}