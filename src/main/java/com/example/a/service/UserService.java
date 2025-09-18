// UserService.java
package com.example.a.service;

import com.example.a.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    int updatePassword(Long userId, String oldPassword, String newPassword) throws Exception;
    int registerUser(User user) throws Exception;
    User loginUser(String username, String password);
    User getUserInfo(Long userId);
    int updateUserInfo(User user);

    String uploadAvatar(Long userId, MultipartFile file) throws Exception;

    int updateUserStatus(Long userId, Integer status);
    int deleteUser(Long userId);
    List<User> getAllUsers();
}

