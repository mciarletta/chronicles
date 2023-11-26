package learn.chronicles.controllers;

import learn.chronicles.App;
import learn.chronicles.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;


@Controller
@ConditionalOnWebApplication

public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final Map<Integer, Room> roomList = new HashMap<>();




    @MessageMapping("/room/boardsUpdate/{roomId}") //where a message is mapped from  url: app/room...
    @SendTo("/room/boards/{roomId}") //which topic it goes to
    public List<Object> updateBoards(@Payload List<Object> boards,@DestinationVariable int roomId) {
        return boards;
    }

    @MessageMapping("/room/piecesUpdate/{roomId}") //where a message is mapped from  url: app/room...
    @SendTo("/room/pieces/{roomId}") //which topic it goes to
    public List<Object> updatePieces(@Payload List<Object> pieces,@DestinationVariable int roomId) {
        return pieces;
    }

    @MessageMapping("/room/diceUpdate/{roomId}") //where a message is mapped from  url: app/room...
    @SendTo("/room/dice/{roomId}") //which topic it goes to
    public List<Object> updateDice(@Payload List<Object> dice,@DestinationVariable int roomId) {
        return dice;
    }

    @MessageMapping("/room/cardsUpdate/{roomId}") //where a message is mapped from  url: app/room...
    @SendTo("/room/cards/{roomId}") //which topic it goes to
    public List<Object> updateCards(@Payload List<Object> cards,@DestinationVariable int roomId) {
        return cards;
    }

    @MessageMapping("/room/boxUpdate/{roomId}") //where a message is mapped from  url: app/room...
    @SendTo("/room/box/{roomId}") //which topic it goes to
    public List<Object> updateBox(@Payload List<Object> box,@DestinationVariable int roomId) {
        return box;
    }

    @MessageMapping("/room/logUpdate/{roomId}") //where a message is mapped from  url: app/room...
    @SendTo("/room/log/{roomId}") //which topic it goes to
    public String updateLog(@Payload String log,@DestinationVariable int roomId) {
        return log;
    }

    @MessageMapping("/room/highlightUpdate/{roomId}") //where a message is mapped from  url: app/room...
    @SendTo("/room/highlight/{roomId}") //which topic it goes to
    public List<Object> updateHighlight(@Payload List<Object> highlight,@DestinationVariable int roomId) {
        return highlight;
    }

    @MessageMapping("/room/discardUpdate/{roomId}") //where a message is mapped from  url: app/room...
    @SendTo("/room/discard/{roomId}") //which topic it goes to
    public List<Object> updateDiscard(@Payload List<Object> discard,@DestinationVariable int roomId) {
        return discard;
    }


    @MessageMapping("/room/add/{roomId}")
    @SendTo("/room/{roomId}")
    public List<UserListResponse> addUser(@Payload String message, @DestinationVariable int roomId, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();

        String[] userSplit = message.split(",");

        UserListResponse user = new UserListResponse();
        user.setName(userSplit[0]);
        user.setAvatar(userSplit[1]);
        user.setColor(userSplit[2]);
        user.setApp_user_id(Integer.parseInt(userSplit[3]));

        synchronized (roomList) {
            Room room = roomList.get(roomId);
            if (room == null) {
                room = new Room(roomId);
                roomList.put(roomId, room);
                Map<String, UserListResponse> users = new HashMap<>();
                users.put(sessionId, user);
                room.setUsers(users);
                return new ArrayList<>(users.values());


            } else {
                Map<String, UserListResponse> users = room.getUsers();

                //check to make sure you don't add a user twice
                if (users.containsValue(user)){
                    // don't add the user again
                    room.setUsers(users);
                    return users.values().stream().toList();

                }

                users.put(sessionId, user);
                room.setUsers(users);
                return users.values().stream().toList();
            }
        }

    }


    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        //look for that session id
        for (Map.Entry<Integer, Room> entry : roomList.entrySet()){
            //see if the key, sessionId is there

            Room r = entry.getValue();

           if(r.getUsers().containsKey(sessionId)){
               //remove that user
               Map<String, UserListResponse> users = r.getUsers();
               users.remove(sessionId);
               //check if there are any users left in the room
               if (users.isEmpty()){
                   //kill the room, there's no one left
                   roomList.remove(entry.getKey());
                   return;
               }
               //replace the users
               r.setUsers(users);
               //grab the id of the room
               int roomId = r.getId();
               //send a message to the remaining users in that room
               simpMessagingTemplate.convertAndSend("/room/" + roomId, users.values().stream().toList());
           }
        }
    }



}
