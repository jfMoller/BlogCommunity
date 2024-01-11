package me.code.server.neo4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class NodesConfig implements CommandLineRunner {

    private final MockUsersConfig mockUsersConfig;

    @Autowired
    public NodesConfig(MockUsersConfig mockUsersConfig) {
        this.mockUsersConfig = mockUsersConfig;
    }

    @Override
    public void run(String... args) {
        mockUsersConfig.createDefaultUsers();
    }

}
