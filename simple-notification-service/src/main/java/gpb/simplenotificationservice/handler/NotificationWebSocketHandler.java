package gpb.simplenotificationservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(NotificationWebSocketHandler.class);
    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = extractUserIdFromUri(session.getUri());
        if (userId != null && !userId.isEmpty()) {
            userSessions.put(userId, session);
            log.info("Пользователь подключился по WebSocket: userId={}", userId);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = getUserBySession(session);
        if (userId != null) {
            userSessions.remove(userId);
            log.info("Пользователь отключился: userId={}", userId);
        }
    }

    public boolean sendToUser(Long userId, String message) {
        String userIdStr = String.valueOf(userId);
        WebSocketSession session = userSessions.get(userIdStr);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
                return true;
            } catch (IOException e) {
                log.warn("Ошибка отправки сообщения пользователю {}: {}", userId, e.getMessage());
                userSessions.remove(userIdStr);
            }
        }

        return false;
    }

    public boolean sendToUsers(List<Long> userIdsLiked, String message) {
        boolean atLeastOneSent = false;

        for (Long userId : userIdsLiked) {
            WebSocketSession session = userSessions.get(userId.toString());
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                    atLeastOneSent = true;
                } catch (IOException e) {
                    log.warn("Ошибка отправки сообщения пользователю {}: {}", userId, e.getMessage());
                    userSessions.remove(userId.toString());
                }
            }
        }

        return atLeastOneSent;
    }

    private String extractUserIdFromUri(URI uri) {
        if (uri == null || uri.getQuery() == null) return null;
        for (String param : uri.getQuery().split("&")) {
            String[] pair = param.split("=", 2);
            if (pair.length == 2 && "userId".equals(pair[0])) {
                return pair[1];
            }
        }
        return null;
    }

    private String getUserBySession(WebSocketSession session) {
        return userSessions.entrySet().stream()
                .filter(e -> e.getValue().equals(session))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public int broadcast(String message) {
        TextMessage textMessage = new TextMessage(message);
        int sent = 0;

        for (Map.Entry<String, WebSocketSession> entry : userSessions.entrySet()) {
            if (sendMessage(entry.getValue(), textMessage)) {
                sent++;
            }
        }

        log.info("Broadcast: отправлено {}/{} соединениям", sent, getActiveConnections());
        return sent;
    }

    private boolean sendMessage(WebSocketSession session, TextMessage message) {
        if (!session.isOpen()) {
            userSessions.values().removeIf(s -> s.equals(session));
            return false;
        }
        try {
            session.sendMessage(message);
            return true;
        } catch (IOException e) {
            log.warn("Ошибка отправки в сессию {}: {}", session.getId(), e.getMessage());
            userSessions.values().removeIf(s -> s.equals(session));
            return false;
        }
    }

    public int getActiveConnections() {
        return userSessions.size();
    }
}