package com.example.poetvine.server.repository;

import com.example.poetvine.server.model.Poem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoemRepository extends CrudRepository<Poem, Long> {
}
