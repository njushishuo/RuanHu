package edu.nju.data.dao;

import edu.nju.data.entity.Users;
import edu.nju.data.util.VerifyResult;

/**
 * Created by ss14 on 2016/7/12.
 */
public interface UserDAO {

    boolean exists(String username);

    boolean verify(String username , String password);

    VerifyResult login(String username, String password);

    Users getUserByName(String username);

}