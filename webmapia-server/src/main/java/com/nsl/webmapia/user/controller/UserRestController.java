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
}
