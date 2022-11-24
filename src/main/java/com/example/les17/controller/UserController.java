package com.example.les17.controller;

import com.example.les17.model.SystemFile;
import com.example.les17.repository.RoleRepository;
import com.example.les17.dto.UserDto;
import com.example.les17.model.Role;
import com.example.les17.model.User;
import com.example.les17.repository.UserRepository;
import com.example.les17.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getMyInfo() throws Exception {
        return ResponseEntity.ok(userService.getMyInfo());
    }

    @GetMapping("/cv/download")
    public ResponseEntity downloadCv(@RequestParam String uuid) throws Exception {
        SystemFile systemFile = userService.downloadCv(uuid);
        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + systemFile.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(systemFile.getData());
    }

    @PostMapping("/cv/upload")
    public ResponseEntity<UserDto> uploadCv(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            return ResponseEntity.ok(userService.uploadCv(file));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

}
