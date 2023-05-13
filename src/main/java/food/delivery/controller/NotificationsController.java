//package food.delivery.controller;
//
//import food.delivery.domain.NotificationMessage;
//import lombok.AllArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.*;
//
//@CrossOrigin
//@RestController
//@RequestMapping("/notifications")
//@AllArgsConstructor
//public class NotificationsController {
//
//    private final SimpMessagingTemplate messagingTemplate;
//
//    @PostMapping
//    public ResponseEntity<Void> sendNotificationToUsers(@RequestBody NotificationMessage message){
//        messagingTemplate.convertAndSend("/topic/publicmessages", message);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
//}
