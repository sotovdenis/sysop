package events;

import java.io.Serializable;

public record GameValuedEvent(Long gameId, Integer valueScore, String verdict, Long ownerId) implements Serializable {}