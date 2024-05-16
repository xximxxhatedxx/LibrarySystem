package com.librarysystem.librarysystem;

public class CurrentSession {
    private static CurrentSession instance;
    private User currentUser;
    private CurrentSession() { }
    public static CurrentSession getInstance() {
        if (instance == null) {
            instance = new CurrentSession();
        }
        return instance;
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    public void deleteSession(){
        currentUser = null;
    }
}