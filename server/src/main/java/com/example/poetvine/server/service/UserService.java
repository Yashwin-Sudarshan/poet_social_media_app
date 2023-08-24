package com.example.poetvine.server.service;

import com.example.poetvine.server.exception.ResourceNotFoundException;
import com.example.poetvine.server.exception.UserNotAuthorisedException;
import com.example.poetvine.server.model.Poem;
import com.example.poetvine.server.model.User;
import com.example.poetvine.server.model.enumeration.VisibilityPreference;
import com.example.poetvine.server.payload.EditUserRequest;
import com.example.poetvine.server.repository.PoemRepository;
import com.example.poetvine.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PoemRepository poemRepository;

    private final PasswordEncoder passwordEncoder;

    public User findByUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) return userRepository.findByUsername(username).get();
        else throw new ResourceNotFoundException("User not found.");
    }

    public User getUserProfile(User userRequesting, String usernameToView) {
        User userToView = findByUsername(usernameToView);

        if (userRequesting == null) {
            if (userToView.getProfileVisibilityPreference().equals(VisibilityPreference.PUBLIC)) {
                return userToView;
            }
            else throw new UserNotAuthorisedException("User is not authorised to view resource.");
        }
        else {
            if (userRequesting.equals(userToView)) {
                return userRequesting;
            }
            else if (userToView.getProfileVisibilityPreference().equals(VisibilityPreference.PUBLIC) ||
                    (userToView.getProfileVisibilityPreference().equals(VisibilityPreference.FOLLOWERS) &&
                            userToView.getFollowers().contains(userRequesting))) {
                return userToView;
            }
            else throw new UserNotAuthorisedException("User is not authorised to view resource.");
        }
    }

    public User editUserProfile(User userRequesting, String username, EditUserRequest editUserRequest) {
        User userToEdit = this.findByUsername(username);

        if (userRequesting != null && userRequesting.equals(userToEdit)) {
            String newEmail = canUserEditEmail(userToEdit, editUserRequest.getEmail())
                    ? editUserRequest.getEmail()
                    : userToEdit.getEmail();
            userToEdit.setEmail(newEmail);

            userToEdit.setProfileImageName(editUserRequest.getProfileImageName());
            userToEdit.setBio(editUserRequest.getBio());

            String newUsername = canUserEditUsername(userToEdit, editUserRequest.getUsername())
                    ? editUserRequest.getUsername()
                    : userToEdit.getUsername();
            userToEdit.setUsername(newUsername);

            userToEdit.setPassword(passwordEncoder.encode(editUserRequest.getPassword()));
            userToEdit.setTopicsWrittenAbout(editUserRequest.getTopicsWrittenAbout());
            userToEdit.setProfileVisibilityPreference(editUserRequest.getProfileVisibilityPreference());
            userToEdit.setPoemVisibilityPreference(editUserRequest.getPoemVisibilityPreference());

            userRepository.save(userToEdit);
        }
        else throw new UserNotAuthorisedException("User is not authorised to perform function.");

        return userToEdit;
    }

    public User deleteUserProfile(User userRequesting, String username) {
        User userToDelete = this.findByUsername(username);

        if (userRequesting != null && userRequesting.equals(userToDelete)) {
            for (User follower: userToDelete.getFollowers()) {
                follower.unfollowUser(userToDelete);
            }
            for (User userFollowing: userToDelete.getFollowing()) {
                userToDelete.unfollowUser(userFollowing);
            }
            for (Poem poem: userToDelete.getPoemsWritten()) {
                userToDelete.removePoemWritten(poem);
                poemRepository.delete(poem);
            }
            for (Poem poem: userToDelete.getPoemsSaved()) {
                userToDelete.removePoemSaved(poem);
            }
            userToDelete.removeAllNotifications();

            userRepository.delete(userToDelete);
        }
        else throw new UserNotAuthorisedException("User is not authorised to perform function.");

        return userToDelete;
    }

    private boolean canUserEditEmail(User user, String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            if (userRepository.findByEmail(email).get().equals(user)) {
                return true;
            }
            else throw new UserNotAuthorisedException("User is not authorised to perform function.");
        }
        else {
            return true;
        }
    }

    private boolean canUserEditUsername(User user, String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            if (userRepository.findByUsername(username).get().equals(user)) {
                return true;
            }
            else throw new UserNotAuthorisedException("User is not authorised to perform function.");
        }
        else {
            return true;
        }
    }
}
