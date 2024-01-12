package me.code.server.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Node("Blog")
public class Blog {

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;
    private String title;
    private String text;
    private LocalDateTime timeStamp;

    @Relationship(type = "PUBLISHED_BY")
    User user;

    public Blog(User user, String title, String text) {
        this.user = user;
        this.title = title;
        this.text = text;
        this.timeStamp = LocalDateTime.now();
    }
}
