package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Genre {
    public Genre(String name) {
        this.name = name;
    }

    public Genre() {

    }

    private int id;
    private String name;

}
