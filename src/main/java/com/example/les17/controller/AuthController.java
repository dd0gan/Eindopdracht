package com.example.les17.controller;

import com.example.les17.dto.AuthDto;
import com.example.les17.dto.AuthResponseDto;
import com.example.les17.dto.UserDto;
import com.example.les17.exception.UserExistedException;
import com.example.les17.security.JwtService;
import com.example.les17.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthenticationManager authManager, JwtService jwtService, UserService userService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/signIn")
    public ResponseEntity signIn(@RequestBody AuthDto authDto) {
            UsernamePasswordAuthenticationToken up =
                    new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());

            try {
                Authentication auth = authManager.authenticate(up);

                UserDetails ud = (UserDetails) auth.getPrincipal();
                String token = jwtService.generateToken(ud);

                return ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .body(new AuthResponseDto(authDto.getUsername(), token));
            }
            catch (AuthenticationException ex) {
                return new ResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
            }
    }

    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody AuthDto authDto) {
        UserDto userDto = new UserDto();
        userDto.setUsername(authDto.getUsername());
        userDto.setPassword(authDto.getPassword());
        try {
            userDto = userService.createUser(userDto);
            return ResponseEntity.ok().body(userDto);
        } catch (UserExistedException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
