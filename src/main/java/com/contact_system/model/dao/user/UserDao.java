package com.contact_system.model.dao.user;

import com.contact_system.model.entities.telephone.Telephone;
import com.contact_system.model.entities.user.User;

import java.util.List;

public interface UserDao {

    public void insert(User user);

    public User authenticate(String user, String password);

    public void addTelephone(String telephone, String userName);

    public void deleteByNumber(String number);

    public Telephone getTelephoneByNumber(String number);

    public List<Telephone> getAllTelephonesByUser(String user);
}
