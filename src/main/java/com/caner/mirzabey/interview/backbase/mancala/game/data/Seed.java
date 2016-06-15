package com.caner.mirzabey.interview.backbase.mancala.game.data;

import java.util.Objects;

/**
 * Created by ecanmir on 14.06.2016.
 */
public class Seed {
    private final int id;

    public Seed(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Seed{" +
               "id=" + id +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seed)) {
            return false;
        }
        Seed seed = (Seed) o;
        return getId() == seed.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}


