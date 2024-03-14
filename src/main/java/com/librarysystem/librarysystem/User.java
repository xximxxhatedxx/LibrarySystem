package com.librarysystem.librarysystem;

public class User {
        public int id;
        public String name;
        public String surname;
        public String email;
        public String password;

        public User(String name, String surname, String email, String password){
            this.name = name;
            this.surname = surname;
            this.email = email;
            this.password = password;
        }

        public User(int id, String name, String surname, String email, String password){
            this.id = id;
            this.name = name;
            this.surname = surname;
            this.email = email;
            this.password = password;
        }

        @Override
        public String toString() {
            return id + " - " + name + " - " + surname + " - " + email + " - " + password;
        }
}
