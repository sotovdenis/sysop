package events;

import java.util.List;

public record GameArrivedEvent(
        Long gameId,
        String title,
        List<Long> userIdsLiked
        ) {
}
