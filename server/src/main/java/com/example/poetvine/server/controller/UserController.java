package com.example.poetvine.server.controller;

import com.example.poetvine.server.config.security.JwtService;
import com.example.poetvine.server.exception.ResourceNotFoundException;
import com.example.poetvine.server.exception.UserNotAuthorisedException;
import com.example.poetvine.server.model.User;
import com.example.poetvine.server.model.enumeration.Filter;
import com.example.poetvine.server.payload.EditUserRequest;
import com.example.poetvine.server.response.BaseUserDto;
import com.example.poetvine.server.response.MyUserDto;
import com.example.poetvine.server.response.OtherUserDto;
import com.example.poetvine.server.response.PoemDto;
import com.example.poetvine.server.service.MapValidationErrorService;
import com.example.poetvine.server.service.PoemService;
import com.example.poetvine.server.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final PoemService poemService;

    private final JwtService jwtService;

    private final MapValidationErrorService mapValidationErrorService;

//    @GetMapping
//    public ResponseEntity<?> getUsers(@AuthenticationPrincipal UserDetails userDetails) {
//        User userRequesting = null;
//        if (userDetails != null) {
//            userRequesting = userService.findByUsername(userDetails.getUsername());
//        }
//
//        Set<BaseUserDto> users = userService.getUsers(userRequesting).stream()
//                .map(User::toOtherUserDto)
//                .collect(Collectors.toSet());
//
//        Map<String, Set<BaseUserDto>> response = new HashMap<>();
//        response.put("users", users);
//
//        return ResponseEntity.ok(response);
//    }

    @GetMapping
    public ResponseEntity<?> getUsersByFilter(
            @RequestParam(name = "filter") Filter filter,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        Set<PoemDto> poems = poemService.getPoemsByFilter(filter, userRequesting);
        Set<BaseUserDto> users = userService.getUsersByFilter(poems, userRequesting).stream()
                .map(User::toOtherUserDto)
                .collect(Collectors.toSet());

        Map<String, Set<BaseUserDto>> response = new HashMap<>();
        response.put("users", users);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/{username}")
    public ResponseEntity<?> getUserProfile(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        User userProfileToView = null;
        try {
            userProfileToView = userService.getUserProfile(userRequesting, username);

        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);

        }

        if (userProfileToView.equals(userRequesting)) {
            Map<String, MyUserDto> response = new HashMap<>();
            response.put("user", userProfileToView.toMyUserDto());
            return ResponseEntity.ok(response);
        }

        Map<String, OtherUserDto> response = new HashMap<>();
        response.put("user", userProfileToView.toOtherUserDto());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/profile/{username}")
    public ResponseEntity<?> editUserProfile(
            @PathVariable String username,
            @Valid @RequestBody EditUserRequest editUserRequest,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationErrorService(result);
        if (errorMap != null) {
            return errorMap;
        }

        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        User updatedUser;
        try {
            updatedUser = userService.editUserProfile(userRequesting, username, editUserRequest);
        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        String updatedJwtToken = jwtService.generateToken(updatedUser);

        Map<String, Object> response = new HashMap<>();
        response.put("user", updatedUser.toMyUserDto());
        response.put("token", updatedJwtToken);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/profile/{username}")
    public ResponseEntity<?> deleteUserProfile(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        User deletedUser;
        try {
            deletedUser = userService.deleteUserProfile(userRequesting, username);
        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Map<String, MyUserDto> response = new HashMap<>();
        response.put("user", deletedUser.toMyUserDto());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/{username}/followers")
    public ResponseEntity<?> getFollowers(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        Set<OtherUserDto> followers;
        try {
            followers = userService.getFollowers(userRequesting, username).stream()
                    .map(User::toOtherUserDto)
                    .collect(Collectors.toSet());

        } catch (ResourceNotFoundException e) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (UserNotAuthorisedException e) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Map<String, Set<OtherUserDto>> response = new HashMap<>();
        response.put("followers", followers);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/{username}/following")
    public ResponseEntity<?> getUsersFollowing(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        Set<OtherUserDto> usersFollowing;
        try {
            usersFollowing = userService.getUsersFollowing(userRequesting, username).stream()
                    .map(User::toOtherUserDto)
                    .collect(Collectors.toSet());

        } catch (ResourceNotFoundException e) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (UserNotAuthorisedException e) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Map<String, Set<OtherUserDto>> response = new HashMap<>();
        response.put("following", usersFollowing);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/profile/{username}/follow")
    public ResponseEntity<?> followUser(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        try {
            userService.followUser(userRequesting, username);
        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully followed " + username);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/profile/{username}/unfollow")
    public ResponseEntity<?> unfollowUser(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        try {
            userService.unfollowUser(userRequesting, username);
        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully unfollowed " + username);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/profile/{username}/remove-follower")
    public ResponseEntity<?> removeFollower(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        try {
            userService.removeFollower(userRequesting, username);
        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully removed " + username + " from followers list.");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/{username}/followers/number")
    public ResponseEntity<?> getNumberOfFollowers(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        int numberOfFollowers;
        try {
            numberOfFollowers = userService.getFollowers(userRequesting, username).size();

        } catch (ResourceNotFoundException e) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (UserNotAuthorisedException e) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Map<String, Integer> response = new HashMap<>();
        response.put("number_of_followers", numberOfFollowers);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/{username}/following/number")
    public ResponseEntity<?> getNumberOfUsersFollowing(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        int numberOfUsersFollowing;
        try {
            numberOfUsersFollowing = userService.getUsersFollowing(userRequesting, username).size();

        } catch (ResourceNotFoundException e) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (UserNotAuthorisedException e) {

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        Map<String, Integer> response = new HashMap<>();
        response.put("number_of_users_following", numberOfUsersFollowing);

        return ResponseEntity.ok(response);
    }
}
