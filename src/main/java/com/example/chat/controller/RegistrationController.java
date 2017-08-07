package com.example.chat.controller;

import com.example.chat.model.Photo;
import com.example.chat.model.User;
import com.example.chat.repository.PhotoRepository;
import com.example.chat.service.IUserService;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class RegistrationController {

    private IUserService userService;
    private PhotoRepository photoRepository;

    @Autowired
    public RegistrationController(IUserService userService, PhotoRepository photoRepository) {
        this.userService = userService;
        this.photoRepository = photoRepository;
    }

    @GetMapping("/registration")
    public String toRegistrationPage() {
        return "/registration";
    }


    @PostMapping("/registration")
    public String registerUser(@Valid User user, @RequestParam(value = "content", required = false) MultipartFile file)
            throws IOException {
        if (file != null && !file.isEmpty()) {
            InputStream input = file.getInputStream();
            Photo photo = new Photo();
            photo.setUser(user);
            photo.setPhoto(IOUtils.toByteArray(input));
            photoRepository.save(photo);
        }
        userService.register(user);
        return "redirect:/";
    }

}
