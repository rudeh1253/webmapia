package com.nsl.webmapia.user.controller;

import com.nsl.webmapia.common.CommonResponse;
import com.nsl.webmapia.user.dto.UserResponseDTO;
import com.nsl.webmapia.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/app")
@CrossOrigin(originPatterns = "*")
public class UserRestController {
    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/game/{gameId}/user")
    public ResponseEntity<CommonResponse> getUsers(@PathVariable("gameId") Long gameId) {
        List<UserResponseDTO> users = userService.getAllUsers(gameId);
        return CommonResponse.ok(users, LocalDateTime.now());
    }

    @GetMapping("/game/{gameId}/user/{userId}")
    public ResponseEntity<CommonResponse> getUser(@PathVariable("gameId") Long gameId,
                                                  @PathVariable("userId") Long userId) {
        UserResponseDTO userDTO = userService.getUser(gameId, userId);
        return CommonResponse.ok(userDTO, LocalDateTime.now());
    }

    @PostMapping("/user/id")
    public ResponseEntity<CommonResponse> generateId() {
        Long id = userService.generateId();
        return CommonResponse.ok(id, LocalDateTime.now());
    }

    @DeleteMapping("/user/id/{userId}")
    public ResponseEntity<CommonResponse> removeUser(@PathVariable("userId")Long userId) {
        userService.removeId(userId);
        return CommonResponse.ok(userId, LocalDateTime.now());
    }
}
