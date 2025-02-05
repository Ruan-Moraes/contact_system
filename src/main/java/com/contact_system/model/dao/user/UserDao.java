package com.contact_system.model.dao.user;

import com.contact_system.model.entities.telephone.Telephone;
import com.contact_system.model.entities.user.User;

public interface UserDao {

    public void insert(User user);

    public User authenticate(String user, String password);

    public void addTelephone(String telephone);

    public void updateByUser(User user);

    public void updateByNumber(String phoneNumber, String newUserName, String newPhoneNumber);

    public void deleteByUser(String user);

    public void deleteByNumber(String number);

    public Telephone getTelephoneByUser(String user);

    public Telephone getTelephoneByNumber(String number);
}
