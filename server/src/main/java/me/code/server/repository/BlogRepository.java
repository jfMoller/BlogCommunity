package me.code.server.repository;

import me.code.server.model.Blog;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends Neo4jRepository<Blog, String> {

    @Query("MATCH (u:User)<-[pb:PUBLISHED_BY]-(b:Blog) WHERE b.id = $blogId RETURN b, pb, u")
    @NotNull
    Optional<Blog> findById(@NotNull String blogId);

    @Query("MATCH (u:User)<-[pb:PUBLISHED_BY]-(b:Blog) WHERE toLower(b.title) CONTAINS toLower($search) RETURN b, pb, u " +
            "ORDER BY CASE WHEN toLower(b.title) STARTS WITH toLower(substring($search, 0, 1))" +
            " THEN 0 ELSE 1 END")
    List<Blog> findBySearch(String search);

    @Query("MATCH (u:User)<-[pb:PUBLISHED_BY]-(b:Blog) RETURN b, pb, u ORDER BY b.timeStamp DESC")
    List<Blog> findAllAndOrderByNewest();

    @Query("MATCH (u:User)<-[pb:PUBLISHED_BY]-(b:Blog) RETURN b, pb, u ORDER BY b.timeStamp ASC")
    List<Blog> findAllAndOrderByOldest();

    @Query("MATCH (u:User)<-[pb:PUBLISHED_BY]-(b:Blog) WHERE toLower(b.title) CONTAINS toLower($search) RETURN b, pb, u " +
            "ORDER BY b.timeStamp DESC")
    List<Blog> findBySearchAndOrderByNewest(String search);

    @Query("MATCH (u:User)<-[pb:PUBLISHED_BY]-(b:Blog) WHERE toLower(b.title) CONTAINS toLower($search) RETURN b, pb, u " +
            "ORDER BY b.timeStamp ASC")
    List<Blog> findBySearchAndOrderByOldest(String search);

    @Query("MATCH (b:Blog {id: $blogId}) DETACH DELETE b")
    void deleteBlog(String blogId);

    @Query("MATCH (b:Blog) DETACH DELETE b")
    void deleteAllBlogs();

}

