package com.example.poetvine.server.service;

import com.example.poetvine.server.exception.ResourceNotFoundException;
import com.example.poetvine.server.exception.UserNotAuthorisedException;
import com.example.poetvine.server.model.Poem;
import com.example.poetvine.server.model.User;
import com.example.poetvine.server.model.enumeration.VisibilityPreference;
import com.example.poetvine.server.payload.EditUserRequest;
import com.example.poetvine.server.repository.PoemRepository;
import com.example.poetvine.server.repository.UserRepository;
import com.example.poetvine.server.response.BaseUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PoemRepository poemRepository;

    private final PasswordEncoder passwordEncoder;

    private final NotificationService notificationService;

    public User findByUsername(String username) {
        if (userRepository.findByUsername(username).isPresent()) return userRepository.findByUsername(username).get();
        else throw new ResourceNotFoundException("User not found.");
    }

    public Set<User> getUsers(User userRequesting) {
        Set<User> users = userRepository.findAllByProfileVisibilityPreference(VisibilityPreference.PUBLIC);

        if (userRequesting != null) {
            userRequesting.getFollowing().stream()
                    .forEach(user -> {
                        if (user.getProfileVisibilityPreference().equals(VisibilityPreference.FOLLOWERS))
                            users.add(user);
                    });
        }

        return users;
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

    public void followUser(User userRequesting, String username) {
        User userToFollow = this.findByUsername(username);

        if (userRequesting == null) throw new UserNotAuthorisedException("User is not authorised to perform function.");
        else {
            if (userToFollow.getProfileVisibilityPreference().equals(VisibilityPreference.PUBLIC) &&
                    !userToFollow.getFollowers().contains(userRequesting)) {
                userRequesting.followUser(userToFollow);

                userRepository.save(userRequesting);
                userRepository.save(userToFollow);

                // Notify userToFollow
                String message = userRequesting.getUsername() + " started following you.";
                notificationService.createNotification(userToFollow, message);
            }
            else throw new UserNotAuthorisedException("User is not authorised to perform function.");
        }
    }

    public void unfollowUser(User userRequesting, String username) {
        User userToUnfollow = this.findByUsername(username);

        if (userRequesting == null) throw new UserNotAuthorisedException("User is not authorised to perform function.");
        else {
            if (userToUnfollow.getFollowers().contains(userRequesting)) {
                userRequesting.unfollowUser(userToUnfollow);

                userRepository.save(userRequesting);
                userRepository.save(userToUnfollow);
            }
            else throw new UserNotAuthorisedException("User is not authorised to perform function.");
        }
    }

    public void removeFollower(User userRequesting, String username) {
        User followerToRemove = this.findByUsername(username);

        if (userRequesting == null) throw new UserNotAuthorisedException("User is not authorised to perform function.");
        else {
            if (userRequesting.getFollowers().contains(followerToRemove)) {
                followerToRemove.unfollowUser(userRequesting);

                userRepository.save(userRequesting);
                userRepository.save(followerToRemove);
            }
            else throw new UserNotAuthorisedException("User is not authorised to perform function.");
        }
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

    public Set<User> getFollowers(User userRequesting, String username) {
        Set<User> followers = null;
        User user = this.findByUsername(username);

        if (userRequesting == null && user.getProfileVisibilityPreference().equals(VisibilityPreference.PUBLIC)) {
            followers = user.getFollowers();
        }
        else if (
                userRequesting != null &&
                        (user.getProfileVisibilityPreference().equals((VisibilityPreference.PUBLIC)) ||
                                (user.getProfileVisibilityPreference().equals(VisibilityPreference.FOLLOWERS) &&
                                        user.getFollowers().contains(userRequesting)))) {
            followers = user.getFollowers();
        }
        else if (userRequesting != null && userRequesting.equals(user)) {
            followers = user.getFollowers();
        }
        else {
            throw new UserNotAuthorisedException("User is not authorised to perform function.");
        }

        return followers;
    }

    public Set<User> getUsersFollowing(User userRequesting, String username) {
        Set<User> usersFollowing = null;
        User user = this.findByUsername(username);

        if (userRequesting == null && user.getProfileVisibilityPreference().equals(VisibilityPreference.PUBLIC)) {
            usersFollowing = user.getFollowing();
        }
        else if (
                userRequesting != null &&
                        (user.getProfileVisibilityPreference().equals((VisibilityPreference.PUBLIC)) ||
                                (user.getProfileVisibilityPreference().equals(VisibilityPreference.FOLLOWERS) &&
                                        user.getFollowers().contains(userRequesting)))) {
            usersFollowing = user.getFollowing();
        }
        else if (userRequesting != null && userRequesting.equals(user)) {
            usersFollowing = user.getFollowing();
        }
        else {
            throw new UserNotAuthorisedException("User is not authorised to perform function.");
        }

        return usersFollowing;
    }
}
