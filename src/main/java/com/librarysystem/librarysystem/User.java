package com.librarysystem.librarysystem;

public class User {
        public int id;
        public String name;
        public String surname;
        public String email;
        public String password;
        public boolean isAdmin;

        public String getName(){
            return name;
        }
        public String getSurname(){
            return surname;
        }
        public String getEmail(){
            return email;
        }
        public int getId(){
            return id;
        }
        public boolean getIsAdmin(){return isAdmin;}

        public User(String name, String surname, String email, String password, boolean isAdmin){
            this.name = name;
            this.surname = surname;
            this.email = email;
            this.password = password;
            this.isAdmin = isAdmin;
        }
        public User(String name, String surname, String email, int id){
            this.name = name;
            this.surname = surname;
            this.email = email;
            this.id = id;
        }
        public User(int id, String name, String surname, String email, String password, boolean isAdmin){
            this.id = id;
            this.name = name;
            this.surname = surname;
            this.email = email;
            this.password = password;
            this.isAdmin = isAdmin;
        }

        @Override
        public String toString() {
            return id + " - " + name + " - " + surname + " - " + email + " - " + password;
        }
}
