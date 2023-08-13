package com.example.poetvine.server.repository;

import com.example.poetvine.server.model.Poem;
import com.example.poetvine.server.model.User;
import com.example.poetvine.server.model.enumeration.PoemStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PoemRepository extends CrudRepository<Poem, Long> {

    Optional<Poem> findById(Long poemId);

    Set<Poem> findAllByPoemStatus(PoemStatus poemStatus);

//    Set<Poem> findAllByAuthorId(Long authorId);
    Set<Poem> findAllByAuthor(User author);
}
