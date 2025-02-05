package com.contact_system.model.entities.telephone;

import com.contact_system.model.entities.user.User;

import java.util.Objects;

public class Telephone {
    private User user;
    private String number;

    public Telephone(User user, String number) {
        this.user = user;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Telephone{" +
                "user=" + user +
                ", number='" + number + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Telephone telephone = (Telephone) o;
        return Objects.equals(user, telephone.user) && Objects.equals(number, telephone.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, number);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
