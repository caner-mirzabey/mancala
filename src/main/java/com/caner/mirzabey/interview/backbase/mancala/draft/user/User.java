package com.caner.mirzabey.interview.backbase.mancala.draft.user;

import java.util.Objects;

/**
 * Created by ecanmir on 10.06.2016.
 */
public class User {
    private String uuid;
    private String name;

    public User() {
    }

    public User(String uuid) {
        this.uuid = uuid;
    }

    public User(String uuid, String name) {
        this(uuid);
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
               "uuid='" + uuid + '\'' +
               ", name='" + name + '\'' +
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
        return Objects.equals(getUuid(), user.getUuid()) && Objects.equals(getName(), user.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid(), getName());
    }
}
