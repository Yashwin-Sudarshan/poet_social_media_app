package com.example.poetvine.server.controller;

import com.example.poetvine.server.exception.PoemAlreadyExistsException;
import com.example.poetvine.server.exception.ResourceNotFoundException;
import com.example.poetvine.server.exception.UserNotAuthorisedException;
import com.example.poetvine.server.model.Poem;
import com.example.poetvine.server.model.User;
import com.example.poetvine.server.model.enumeration.Filter;
import com.example.poetvine.server.payload.CreateOrEditPoemRequest;
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

@RestController
@RequestMapping("/poems")
@RequiredArgsConstructor
public class PoemController {

    private final PoemService poemService;
    private final UserService userService;
    private final MapValidationErrorService mapValidationErrorService;

    @GetMapping
    public ResponseEntity<?> getPoemsByFilter(
            @RequestParam(name = "filter") Filter filter,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = null;
        if (userDetails != null) {
            user = userService.findByUsername(userDetails.getUsername());
        }

        Set<PoemDto> poems = poemService.getPoemsByFilter(filter, user);
        Map<String, Set<PoemDto>> response = new HashMap<>();
        response.put("poems", poems);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/poem/{poemId}")
    public ResponseEntity<?> getPoem(
            @PathVariable Long poemId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = null;
        if (userDetails != null) {
            user = userService.findByUsername(userDetails.getUsername());
        }

        PoemDto poem = null;
        try {
            poem = poemService.getPoem(poemId, user).toPoemDto();
        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        Map<String, PoemDto> response = new HashMap<>();
        response.put("poem", poem);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUsersPoems(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User userRequesting = null;
        if (userDetails != null) {
            userRequesting = userService.findByUsername(userDetails.getUsername());
        }

        Set<PoemDto> poems;
        try{
            User author = userService.findByUsername(username);
            poems = poemService.getPoemsByUser(userRequesting, author);

        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        Map<String, Set<PoemDto>> response = new HashMap<>();
        response.put("poems", poems);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/saved")
    public ResponseEntity<?> getSavedPoems(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = null;
        if (userDetails != null) {
            user = userService.findByUsername(userDetails.getUsername());
        }

        Set<PoemDto> savedPoems;
        try {
            savedPoems = poemService.getSavedPoems(user);

        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        Map<String, Set<PoemDto>> response = new HashMap<>();
        response.put("poems", savedPoems);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/poem/{poemId}")
    public ResponseEntity<?> savePoem(
            @PathVariable Long poemId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = null;
        if (userDetails != null) {
            user = userService.findByUsername(userDetails.getUsername());
        }

        PoemDto poem = null;
        try {
            poem = poemService.savePoem(user, poemId);
        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        } catch (PoemAlreadyExistsException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Map<String, PoemDto> response = new HashMap<>();
        response.put("poem", poem);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPoem(
            @Valid @RequestBody CreateOrEditPoemRequest createOrEditPoemRequest,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationErrorService(result);
        if (errorMap != null) {
            return errorMap;
        }

        User author = null;
        if (userDetails != null) {
            author = userService.findByUsername(userDetails.getUsername());
        }

        Poem poem = null;
        try {
            poem = poemService.createPoem(author, createOrEditPoemRequest);

        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        Map<String, PoemDto> response = new HashMap<>();
        response.put("poem", poem.toPoemDto());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/edit/{poemId}")
    public ResponseEntity<?> editPoem(
            @Valid @PathVariable Long poemId,
            @Valid @RequestBody CreateOrEditPoemRequest createOrEditPoemRequest,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationErrorService(result);
        if (errorMap != null) {
            return errorMap;
        }

        User author = null;
        if (userDetails != null) {
            author = userService.findByUsername(userDetails.getUsername());
        }

        Poem editedPoem = null;
        try {
            editedPoem = poemService.editPoem(author, poemId, createOrEditPoemRequest);
        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        Map<String, PoemDto> response = new HashMap<>();
        response.put("poem", editedPoem.toPoemDto());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/poem/{poemId}")
    public ResponseEntity<?> deletePoem(
            @Valid @PathVariable Long poemId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User author = null;
        if (userDetails != null) {
            author = userService.findByUsername(userDetails.getUsername());
        }

        Poem deletedPoem = null;
        try {
            deletedPoem = poemService.deletePoem(author, poemId);
        } catch (ResourceNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

        } catch (UserNotAuthorisedException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }

        Map<String, PoemDto> response = new HashMap<>();
        response.put("poem", deletedPoem.toPoemDto());

        return ResponseEntity.ok(response);
    }
}
