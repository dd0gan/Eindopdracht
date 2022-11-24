package com.example.les17.service;

import com.example.les17.dto.UserDto;
import com.example.les17.exception.InvalidAuthException;
import com.example.les17.exception.InvalidFileException;
import com.example.les17.exception.UserExistedException;
import com.example.les17.exception.UserIsNotExistedException;
import com.example.les17.model.Role;
import com.example.les17.model.SystemFile;
import com.example.les17.model.User;
import com.example.les17.repository.RoleRepository;
import com.example.les17.repository.SystemFileRepository;
import com.example.les17.repository.UserRepository;
import com.example.les17.security.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SystemFileRepository systemFileRepository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, SystemFileRepository systemFileRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.systemFileRepository = systemFileRepository;
        this.encoder = encoder;
    }

    public UserDto createUser(UserDto userDto) {
        Optional<User> user = userRepository.findById(userDto.getUsername());
        if (user.isPresent()) {
            throw new UserExistedException("User is existed");
        }

        User toSaveUser = new User();
        toSaveUser.setUsername(userDto.getUsername());
        toSaveUser.setPassword(encoder.encode(userDto.getPassword()));

        Optional<Role> userRole = roleRepository.findById("USER");
        List<Role> roles = Arrays.asList(userRole.get());
        toSaveUser.setRoles(roles);
        toSaveUser = userRepository.save(toSaveUser);
        return userDto;
    }

    public UserDto uploadCv(MultipartFile file) throws Exception {
        User userContext = ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Optional<User> uo = userRepository.findById(userContext.getUsername());
        User user = uo.orElseThrow(() -> new UserIsNotExistedException("User doesn't existed"));

        SystemFile systemFile = new SystemFile();
        systemFile.setFilename(file.getOriginalFilename());
        systemFile.setCreateDatetime(new Date());
        systemFile.setData(file.getBytes());
        systemFile.setUuid(UUID.randomUUID().toString());
        systemFileRepository.save(systemFile);

        user.setCvUniqueId(systemFile.getUuid());
        user.setCvFilename(systemFile.getFilename());
        userRepository.save(user);

        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setCvUniqueId(user.getCvUniqueId());
        userDto.setCvFilename(systemFile.getFilename());
        return userDto;
    }

    public UserDto getMyInfo() {
        User userContext = ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Optional<User> uo = userRepository.findById(userContext.getUsername());
        User user = uo.orElseThrow(() -> new UserIsNotExistedException("User doesn't existed"));

        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setCvUniqueId(user.getCvUniqueId());
        userDto.setCvFilename(user.getCvFilename());
        return userDto;
    }

    public SystemFile downloadCv(String uuid) {
        User userContext = ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Optional<User> uo = userRepository.findById(userContext.getUsername());
        User user = uo.orElseThrow(() -> new UserIsNotExistedException("User doesn't existed"));

        SystemFile systemFile = systemFileRepository.findByUuid(uuid);
        if (systemFile == null) {
            throw new InvalidFileException("File doesn't existed");
        }
        return systemFile;
    }
}
