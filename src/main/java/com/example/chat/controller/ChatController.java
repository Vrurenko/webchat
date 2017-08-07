package com.example.chat.controller;

import com.example.chat.model.DTO.MessagePageDTO;
import com.example.chat.model.DTO.UserTypingDTO;
import com.example.chat.model.Discussion;
import com.example.chat.model.Message;
import com.example.chat.model.SecurityUser;
import com.example.chat.model.User;
import com.example.chat.service.IDiscussionService;
import com.example.chat.service.IMessageService;
import com.example.chat.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@Controller
public class ChatController {

    private IMessageService messageService;
    private IUserService userService;
    private IDiscussionService discussionService;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChatController(IMessageService messageService,
                          IUserService userService,
                          IDiscussionService discussionService,
                          SimpMessagingTemplate simpMessagingTemplate) {
        this.messageService = messageService;
        this.userService = userService;
        this.discussionService = discussionService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        User user = ((SecurityUser) authentication.getPrincipal()).getUser();
        List<Discussion> discussions = discussionService.findByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("discussions", discussions);
        return "/chat";
    }

    @MessageMapping("/findusers")
    public void findUsers(String keyWord, Principal principal) throws Exception {
        List<User> userList = userService.findUsersByKeyWord(keyWord);
        simpMessagingTemplate.convertAndSendToUser(principal.getName(), "/search/reply", userList);
    }

    @MessageMapping("/addDiscussion")
    public void addDiscussion(Discussion discussion, Authentication authentication) {
        User currentUser = ((SecurityUser) authentication.getPrincipal()).getUser();
        discussion.getUsers().add(currentUser);
        discussion = discussionService.save(discussion);
        discussion = discussionService.findById(discussion.getId());
        for (User user : discussion.getUsers()) {
            simpMessagingTemplate.convertAndSendToUser(user.getLogin(), "/newDiscussion/reply", discussion);
        }
    }

    @MessageMapping("/newMessage")
    public void saveMessage(Message message, Authentication authentication) {
        User currentUser = ((SecurityUser) authentication.getPrincipal()).getUser();
        message.setSender(currentUser);
        message.setDate(new Date());
        message = messageService.saveAndFlush(message);
        for (User user : discussionService.findById(message.getRecipient().getId()).getUsers()) {
            simpMessagingTemplate.convertAndSendToUser(user.getLogin(), "/newMessage/reply", message);
        }
    }


    @MessageMapping("/loadMessages")
    public void loadMessages(MessagePageDTO messagePageDTO, Authentication authentication) {
        User currentUser = ((SecurityUser) authentication.getPrincipal()).getUser();
        List<Message> messageList = messageService.loadMessages(messagePageDTO.getDiscussionID(), messagePageDTO.getPage());
        simpMessagingTemplate.convertAndSendToUser(currentUser.getLogin(), "/loadMessages/reply", messageList);
    }

    @MessageMapping("/typing")
    public void typing(Discussion discussion, Authentication authentication) {
        User currentUser = ((SecurityUser) authentication.getPrincipal()).getUser();
        UserTypingDTO userTypingDTO = new UserTypingDTO(currentUser.getLogin(), discussion.getId());
        for (User user : discussionService.findById(discussion.getId()).getUsers()) {
            simpMessagingTemplate.convertAndSendToUser(user.getLogin(), "/typing/reply", userTypingDTO);
        }
    }

}
