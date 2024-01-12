package me.code.server.repository;

import me.code.server.model.Blog;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends Neo4jRepository<Blog, String> {

    @Query("MATCH (b:Blog) WHERE b.id = $blogId RETURN b")
    Optional<Blog> findById(String blogId);

    @Query("MATCH (b:Blog) WHERE toLower(b.title) CONTAINS toLower($search) RETURN b " +
            "ORDER BY CASE WHEN toLower(b.title) STARTS WITH toLower(SUBSTRING($search, 0, 1))" +
            " THEN 0 ELSE 1 END")
    List<Blog> findBySearch(String search);

    @Query("MATCH (b:Blog) RETURN b ORDER BY b.timeStamp DESC")
    List<Blog> findAllAndOrderByNewest();

    @Query("MATCH (b:Blog) RETURN b ORDER BY b.timeStamp ASC")
    List<Blog> findAllAndOrderByOldest();

    @Query("MATCH (b:Blog) WHERE toLower(b.title) CONTAINS toLower($search) RETURN b " +
            "ORDER BY b.timeStamp DESC")
    List<Blog> findBySearchAndOrderByNewest(String search);

    @Query("MATCH (b:Blog) WHERE toLower(b.title) CONTAINS toLower($search) RETURN b " +
            "ORDER BY b.timeStamp ASC")
    List<Blog> findBySearchAndOrderByOldest(String search);

    @Query("MATCH (b:Blog {id: $blogId}) DETACH DELETE b")
    void deleteBlog(String blogId);

    @Query("MATCH (b:Blog) DETACH DELETE b")
    void deleteAllBlogs();

}

