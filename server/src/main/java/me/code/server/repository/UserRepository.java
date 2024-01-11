package me.code.server.repository;

import me.code.server.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends Neo4jRepository<User, String> {

    @Query("MATCH (u:User) WHERE u.username = $username RETURN u")
    Optional<User> findByUsername(String username);

}

