package com.caner.mirzabey.interview.backbase.mancala.user;

import java.util.Objects;

/**
 * Created by ecanmir on 10.06.2016.
 */
public class User {
    private String uuid;
    private String username;

    public User(String uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
               "uuid='" + uuid + '\'' +
               ", username='" + username + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(getUuid(), user.getUuid()) && Objects.equals(getUsername(), user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getUsername());
    }
}
