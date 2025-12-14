package gpb.simplenotificationservice.controller;

import gpb.simplenotificationservice.handler.NotificationWebSocketHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationWebSocketHandler handler;

    public NotificationController(NotificationWebSocketHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendToUser(@RequestBody SendMessageRequest request) {
        boolean sent = handler.sendToUser(Long.valueOf(request.getUserId()), request.getMessage());

        if (sent) {
            return ResponseEntity.ok(Map.of(
                    "status", "ok",
                    "message", "Сообщение отправлено пользователю " + request.getUserId(),
                    "userId", request.getUserId()
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Пользователь " + request.getUserId() + " не найден или неактивен",
                    "userId", request.getUserId()
            ));
        }
    }

    @PostMapping("/send-to-many")
    public ResponseEntity<Map<String, Object>> sendToUsers(@RequestBody SendToManyRequest request) {
        List<Long> userIds = request.getUserIds();
        String message = request.getMessage();

        if (userIds == null || userIds.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Список userIds не может быть пустым"
            ));
        }

        boolean sent = handler.sendToUsers(userIds, message);

        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "sent", sent,
                "userIds", userIds,
                "message", "Попытка отправки уведомления " + userIds.size() + " пользователям"
        ));
    }

    @PostMapping("/broadcast")
    public ResponseEntity<Map<String, Object>> broadcast(@RequestBody String message) {
        int sent = handler.broadcast(message);
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "sentTo", sent,
                "message", message
        ));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        return ResponseEntity.ok(Map.of(
                "activeConnections", handler.getActiveConnections()
        ));
    }

    public static class SendMessageRequest {
        private String userId;
        private String message;

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    public static class SendToManyRequest {
        private List<Long> userIds;
        private String message;

        public List<Long> getUserIds() { return userIds; }
        public void setUserIds(List<Long> userIds) { this.userIds = userIds; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}