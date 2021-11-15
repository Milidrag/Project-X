package alcatraz.common;

import java.util.UUID;

public class User {

    private UUID userId ;

    private String username;

    public User() {

    }

    public User(String username) {
        this.username = username;
        this.userId=UUID.randomUUID();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}
