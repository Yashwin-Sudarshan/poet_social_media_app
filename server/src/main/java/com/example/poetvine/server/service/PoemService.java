package com.example.poetvine.server.service;

import com.example.poetvine.server.exception.PoemAlreadyExistsException;
import com.example.poetvine.server.exception.ResourceNotFoundException;
import com.example.poetvine.server.exception.UserNotAuthorisedException;
import com.example.poetvine.server.model.Poem;
import com.example.poetvine.server.model.User;
import com.example.poetvine.server.model.enumeration.Filter;
import com.example.poetvine.server.model.enumeration.PoemStatus;
import com.example.poetvine.server.model.enumeration.VisibilityPreference;
import com.example.poetvine.server.payload.CreateOrEditPoemRequest;
import com.example.poetvine.server.repository.PoemRepository;
import com.example.poetvine.server.repository.UserRepository;
import com.example.poetvine.server.response.PoemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PoemService {

    private final PoemRepository poemRepository;

    private final UserRepository userRepository;

    /**
     *  Returns a poem with the specified id, if the authenticated or unauthenticated user is allowed
     *  to see it.
     *
     *  The poem must be PUBLISHED and its author's poem visibility preferences must either be set to
     *  PUBLIC or, if set to FOLLOWERS, the user needs to be one of the author's followers
     */
    public Poem getPoem(Long poemId, User user) {
        Poem poem = poemRepository.findById(poemId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        if (isPoemAllowedToBeViewedByUser(poem, user)) return poem;
        else throw new UserNotAuthorisedException("User not authorised to view resource");
    }

    public Set<PoemDto> getPoemsByFilter(Filter filter, User user) {
        Set<Poem> poems = getViewablePoemsByUser(user);
        Set<PoemDto> poemsByFilter = new HashSet<>();
        LocalDateTime fromDateTime =
                filter.equals(Filter.TOP_TODAY) ? LocalDateTime.now().minusDays(1)
                : filter.equals(Filter.TOP_THIS_WEEK) ? LocalDateTime.now().minusWeeks(1)
                : filter.equals(Filter.TOP_THIS_MONTH) ? LocalDateTime.now().minusMonths(1)
                : null;

        /** Return poems ordered from most likes to the least likes in the last period of time specified
         *  by the filter.
         */
        if (fromDateTime != null) {
            poemsByFilter = poems.stream()
                    .sorted(Comparator.comparingInt(
                                    poem -> getPoemLikesSinceDateTime((Poem) poem, fromDateTime))
                            .reversed())
                    .map(Poem::toPoemDto)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        /** Return a random poem */
        else if (filter.equals(Filter.FEATURED)) {
            poemsByFilter.add(getFeaturedPoem(poems).toPoemDto());
        }
        /** Return poems ordered from most recent creation date to the oldest creation date */
        else if (filter.equals(Filter.RECENT)) {
            poemsByFilter = poems.stream()
                    .sorted(Comparator.comparing(Poem::getCreatedAt).reversed())
                    .map(Poem::toPoemDto)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        else if (fromDateTime == null) {
            throw new IllegalArgumentException("Invalid filter provided.");
        }

        return poemsByFilter;
    }

    /**
     *  Returns poems belonging to the requested user, with certain restrictions:
     *  If an unauthenticated user is making a request, then only the PUBLISHED poems with PUBLIC visibility preferences
     *  are returned.
     *  If an authenticated user is making a request, then both the PUBLISHED poems with PUBLIC visibility preferences
     *  and FOLLOWERS visibility preferences, if the user follows the author, are returned.
     *  If an authenticated user is making a request for their own poems, then all of their poems (PUBLISHED AND DRAFTS
     *  AND SAVED) are returned.
     */
    public Set<PoemDto> getPoemsByUser(User userRequesting, User author) {
        Set<PoemDto> poems;
        Set<Poem> poemsByAuthor;

        if (author != null) {
            poemsByAuthor = poemRepository.findAllByAuthor(author);

            if (author.equals(userRequesting)) {
                poems = poemsByAuthor.stream()
                        .map(Poem::toPoemDto)
                        .collect(Collectors.toCollection(LinkedHashSet::new));
            }
            else {
                poems = poemsByAuthor.stream()
                        .filter(poem -> isPoemAllowedToBeViewedByUser(poem, userRequesting))
                        .map(Poem::toPoemDto)
                        .collect(Collectors.toCollection(LinkedHashSet::new));
            }
        }
        else {
            throw new ResourceNotFoundException("Username not found");
        }

        if (poems.isEmpty() && poemsByAuthor.size() > 0)
            throw new UserNotAuthorisedException("User is not authorised to view resource.");

        return poems;
    }

    /**
     * Returns an authenticated user's saved poems.
     * */
    public Set<PoemDto> getSavedPoems(User user) {
        Set<PoemDto> poems;
        if (user != null) {
            poems = user.getPoemsSaved().stream()
                    .map(Poem::toPoemDto)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        else throw new UserNotAuthorisedException("User is not authorised to view resource");

        return poems;
    }

    /**
     * Adds a poem to a users saved poems, if the user is allowed to view the poem and if the poem is not
     * already saved by the user.
     */
    public PoemDto savePoem(User user, Long poemId) {
        Poem poem = this.getPoem(poemId, user);
        if (!user.getPoemsSaved().contains(poem)) {
            user.addPoemSaved(poem);
            userRepository.save(user);

            return poem.toPoemDto();
        }
        else throw new PoemAlreadyExistsException("Poem is already saved by user");
    }

    /**
     * Returns the poem created by the authenticated user.
     */
    public Poem createPoem(User author, CreateOrEditPoemRequest createOrEditPoemRequest) {
        Poem poem = null;
        if (author != null) {
            poem = new Poem(createOrEditPoemRequest.getTitle(), createOrEditPoemRequest.getContent(),
                    createOrEditPoemRequest.getTags(), author, createOrEditPoemRequest.getStatus());

            poem.setCreatedAt(LocalDateTime.now());
            poemRepository.save(poem);

            author.addPoemWritten(poem);
            userRepository.save(author);
        }
        else throw new UserNotAuthorisedException("User not authorised to perform the following function.");

        return poem;
    }

    public Poem editPoem(User author, Long poemId, CreateOrEditPoemRequest createOrEditPoemRequest) {
        Poem poem = poemRepository.findById(poemId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        if (author != null && author.equals(poem.getAuthor())) {
            poem.setTitle(createOrEditPoemRequest.getTitle());
            poem.setContent(createOrEditPoemRequest.getContent());
            poem.setTags(createOrEditPoemRequest.getTags());
            poem.setPoemStatus(createOrEditPoemRequest.getStatus());
            poem.setCreatedAt(LocalDateTime.now());

            poemRepository.save(poem);
        } else throw new UserNotAuthorisedException("User not authorised to perform the following function.");

        return poem;
    }

    public Poem deletePoem(User author, Long poemId) {
        Poem poem = poemRepository.findById(poemId).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        if (author != null && author.equals(poem.getAuthor())) {
            author.removePoemWritten(poem);
            userRepository.save(author);

            poemRepository.deleteById(poemId);
        } else throw new UserNotAuthorisedException("User not authorised to perform the following function.");

        return poem;
    }

    private boolean isPoemAllowedToBeViewedByUser(Poem poem, User user) {
        /** Unauthenticated user can only view PUBLISHED poems that are set for only PUBLIC viewing */
        if (user == null) {
            if (poem.getPoemStatus().equals(PoemStatus.PUBLISHED) &&
                    poem.getAuthor().getPoemVisibilityPreference().equals(VisibilityPreference.PUBLIC)) {
                return true;
            }
        }

        /** Authenticated user can view PUBLISHED poems that are set for PUBLIC viewing, or PUBLISHED poems that are
         *  set for only followers to view if the user follows the author of the poem, or view their own poem
         */
        if (poem.getPoemStatus().equals(PoemStatus.PUBLISHED) &&
                (poem.getAuthor().getPoemVisibilityPreference().equals(VisibilityPreference.PUBLIC) ||
                        (poem.getAuthor().getPoemVisibilityPreference().equals(VisibilityPreference.FOLLOWERS) &&
                                poem.getAuthor().getFollowers().contains(user)) ||
                        (poem.getAuthor().equals(user)))) {
            return true;
        }

        return false;
    }


    /**
     * Returns all PUBLISHED poems that an authenticated or unauthenticated user is permitted to see.
     *
     * If the user is unauthenticated, then only PUBLISHED poems with PUBLIC poem visibility preferences are returned.
     * If the user is authenticated, then either PUBLISHED poems with PUBLIC poem visibility preferences and PUBLISHED
     * poems with FOLLOWERS poem visibility preferences where the user is a follower of the author are returned.
     */
    private Set<Poem> getViewablePoemsByUser(User user) {
        Set<Poem> publishedPoems = poemRepository.findAllByPoemStatus(PoemStatus.PUBLISHED);
        Set<Poem> viewablePoems = publishedPoems.stream()
                .filter(poem -> isPoemAllowedToBeViewedByUser(poem, user))
                .collect(Collectors.toSet());;

        return viewablePoems;
    }

    private Poem getFeaturedPoem(Set<Poem> poems) {
        if (poems == null || poems.isEmpty()) {
            throw new IllegalArgumentException("Poems must not be null or empty.");
        }

        Random random = new Random();
        int randomIndex = random.nextInt(poems.size());
        /**
         * Iterate until element before the randomIndex. Calling iterator.next() in the return statement will
         * return the target poem
         */
        Iterator<Poem> iterator = poems.iterator();
        for (int i = 0; i < randomIndex; i++) {
            iterator.next();
        }

        return iterator.next();
    }

    private int getPoemLikesSinceDateTime(Poem poem, LocalDateTime dateTime) {
        return (int) poem.getLikes().stream()
                .filter(like -> like.getLikedAt().isAfter(dateTime))
                .count();
    }
}
