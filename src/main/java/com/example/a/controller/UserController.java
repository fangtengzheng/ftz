package com.example.a.controller;

import com.example.a.entity.User;
import com.example.a.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // 用户注册
    @PostMapping("/register")
    @ResponseBody
    public Map<String, Object> registerUser(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        try {
            int rows = userService.registerUser(user);
            if (rows > 0) {
                result.put("status", 200);
                result.put("msg", "注册成功");
            } else {
                result.put("status", 500);
                result.put("msg", "注册失败");
            }
        } catch (Exception e) {
            result.put("status", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    // 用户登录
    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> loginUser(@RequestBody Map<String, String> loginInfo) {
        Map<String, Object> result = new HashMap<>();
        String username = loginInfo.get("username");
        String password = loginInfo.get("password");

        User user = userService.loginUser(username, password);
        if (user != null) {
            result.put("status", 200);
            result.put("msg", "登录成功");
            result.put("user", user);
        } else {
            result.put("status", 401);
            result.put("msg", "用户名或密码错误");
        }
        return result;
    }
    // 修改密码
    @PostMapping("/{userId}/password")
    @ResponseBody
    public Map<String, Object> updatePassword(@PathVariable Long userId, @RequestBody Map<String, String> passwordInfo) {
        Map<String, Object> result = new HashMap<>();
        String oldPassword = passwordInfo.get("oldPassword");
        String newPassword = passwordInfo.get("newPassword");

        try {
            int rows = userService.updatePassword(userId, oldPassword, newPassword);
            if (rows > 0) {
                result.put("status", 200);
                result.put("msg", "密码修改成功");
            } else {
                result.put("status", 400);
                result.put("msg", "密码修改失败，旧密码错误或用户不存在");
            }
        } catch (Exception e) {
            result.put("status", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    // 头像上传
    @PostMapping("/avatar")
    @ResponseBody
    public Map<String, Object> uploadAvatar(@RequestParam("avatar") MultipartFile file, @RequestParam("userId") Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            String avatarUrl = userService.uploadAvatar(userId, file);
            result.put("status",200);
            result.put("msg", "头像上传成功");
            result.put("avatar", avatarUrl);
        } catch (Exception e) {
            result.put("status", 500);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    // 获取用户信息
    @GetMapping("/{userId}")
    @ResponseBody
    public Map<String, Object> getUserInfo(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        User user = userService.getUserInfo(userId);
        if (user != null) {
            result.put("status", 200);
            result.put("msg", "获取成功");
            result.put("user", user);
        } else {
            result.put("status", 404);
            result.put("msg", "用户不存在");
        }
        return result;
    }

    // 更新用户信息
    @PutMapping("/{userId}")
    @ResponseBody
    public Map<String, Object> updateUserInfo(@PathVariable Long userId, @RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        user.setUserId(userId);
        int rows = userService.updateUserInfo(user);
        if (rows > 0) {
            result.put("status", 200);
            result.put("msg", "更新成功");
        } else {
            result.put("status", 500);
            result.put("msg", "更新失败");
        }
        return result;
    }

    // 更新用户状态（管理员操作）
    @PutMapping("/{userId}/status")
    @ResponseBody
    public Map<String, Object> updateUserStatus(@PathVariable Long userId, @RequestParam Integer status) {
        Map<String, Object> result = new HashMap<>();
        int rows = userService.updateUserStatus(userId, status);
        if (rows > 0) {
            result.put("status", 200);
            result.put("msg", "状态更新成功");
        } else {
            result.put("status", 500);
            result.put("msg", "状态更新失败");
        }
        return result;
    }

    // 删除用户（伪删除）
    @DeleteMapping("/{userId}")
    @ResponseBody
    public Map<String, Object> deleteUser(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        int rows = userService.deleteUser(userId);
        if (rows > 0) {
            result.put("status", 200);
            result.put("msg", "用户已禁用");
        } else {
            result.put("status", 500);
            result.put("msg", "操作失败");
        }
        return result;
    }

    // 获取所有用户（管理员使用）
    @GetMapping("/admin/users")
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}