package com.example.a.mapper;

import com.example.a.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    int insertUser(User user);
    User selectUserByUsername(String username);
    int updatePassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);

    User selectUserByPhone(String phone);

    User selectUserByEmail(String email);

    User selectUserById(Long userId);

    int updateUser(User user);

    int updateUserAvatar(@Param("userId") Long userId, @Param("avatar") String avatar);

    int updateUserStatus(@Param("userId") Long userId, @Param("status") Integer status);

    int deleteUser(Long userId);

    List<User> selectAllUsers();
}