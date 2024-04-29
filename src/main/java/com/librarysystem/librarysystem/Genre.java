package com.librarysystem.librarysystem;

public class Genre {
    public int id;
    public String name;

    Genre(int id, String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
