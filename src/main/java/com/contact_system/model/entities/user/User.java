package com.contact_system.model.entities.user;

import com.contact_system.model.entities.telephone.Telephone;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String password;
    private List<Telephone> telephones;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.telephones = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<Telephone> getTelephones() {
        return telephones;
    }
}
