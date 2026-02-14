package com.stocklab.api;

import com.stocklab.api.dto.UserDtos;
import com.stocklab.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserDtos.ProfileResponse me(Principal principal) {
        var user = userService.findByUsername(principal.getName());
        return new UserDtos.ProfileResponse(user.getId(), user.getUsername(), user.getRole().name(), user.getFullName());
    }

    @PutMapping("/me")
    public UserDtos.ProfileResponse updateMe(
            Principal principal,
            @Valid @RequestBody UserDtos.UpdateProfileRequest request
    ) {
        var user = userService.updateFullName(principal.getName(), request.fullName());
        return new UserDtos.ProfileResponse(user.getId(), user.getUsername(), user.getRole().name(), user.getFullName());
    }
}
