package com.example.chat.controller;

import com.example.chat.model.Photo;
import com.example.chat.model.User;
import com.example.chat.repository.PhotoRepository;
import com.example.chat.service.IUserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {

    private IUserService userService;
    private PhotoRepository photoRepository;

    @Value("classpath:default.png")
    private Resource resource;

    @Autowired
    public ImageController(IUserService userService, PhotoRepository photoRepository) {
        this.userService = userService;
        this.photoRepository = photoRepository;
    }

    @RequestMapping(value = "/image/user/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    @Transactional
    public ResponseEntity<byte[]> getImage(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        Photo photo = null;
        if (user != null) {
             photo = photoRepository.findByUser(user);
        }
        byte[] image = (user == null || photo == null) ? getDefaultPhoto() : photo.getPhoto();
        HttpHeaders headers = new HttpHeaders() {{
            setContentType(MediaType.IMAGE_PNG);
        }};
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    private byte[] getDefaultPhoto() {
        byte[] photo;
        try (InputStream in = resource.getInputStream()) {
            photo =  IOUtils.toByteArray(in);
        } catch (IOException e) {
            photo = null;
        }
        return photo;
    }

}
