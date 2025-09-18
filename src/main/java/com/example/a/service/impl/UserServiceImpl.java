// UserServiceImpl.java
package com.example.a.service.impl;

import com.example.a.entity.User;
import com.example.a.mapper.UserMapper;
import com.example.a.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    // 头像存储路径
    private static final String AVATAR_UPLOAD_PATH = "uploads/avatars/";

    @Override
    public int updatePassword(Long userId, String oldPassword, String newPassword) throws Exception {
        // 根据用户ID查询用户
        User user = userMapper.selectUserById(userId);
        if (user == null) {
            throw new Exception("用户不存在");
        }

        // 检查旧密码是否正确
        if (!user.getPassword().equals(oldPassword)) {
            throw new Exception("旧密码错误");
        }

        // 更新密码
        return userMapper.updatePassword(userId, newPassword);
    }

    @Override
    public int registerUser(User user) throws Exception {
        // 检查用户名是否已存在
        User existUser = userMapper.selectUserByUsername(user.getUsername());
        if (existUser != null) {
            throw new Exception("用户名已存在");
        }

        // 检查手机号是否已存在
        existUser = userMapper.selectUserByPhone(user.getPhone());
        if (existUser != null) {
            throw new Exception("手机号已存在");
        }

        // 检查邮箱是否已存在
        existUser = userMapper.selectUserByEmail(user.getEmail());
        if (existUser != null) {
            throw new Exception("邮箱已存在");
        }

        // 设置默认状态为正常
        user.setStatus(1);
        // 只在 isAdmin 为空时才默认普通用户，否则保留前端传递的 isAdmin
        if (user.getIsAdmin() == null) {
            user.setIsAdmin(0);
        }

        return userMapper.insertUser(user);
    }

    @Override
    public User loginUser(String username, String password) {
        User user = userMapper.selectUserByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }
        return user;
    }

    @Override
    public User getUserInfo(Long userId) {
        return userMapper.selectUserById(userId);
    }

    @Override
    public int updateUserInfo(User user) {
        return userMapper.updateUser(user);
    }

    @Override
    public String uploadAvatar(Long userId, MultipartFile file) throws Exception {
        // 检查用户是否存在
        User user = userMapper.selectUserById(userId);
        if (user == null) {
            throw new Exception("用户不存在");
        }

        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new Exception("上传的文件不能为空");
        }

        // 检查文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isValidImageFile(originalFilename)) {
            throw new Exception("只支持上传图片文件（jpg, jpeg, png, gif）");
        }

        // 检查文件大小（限制为5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new Exception("文件大小不能超过5MB");
        }

        // 创建上传目录
        File uploadDir = new File(AVATAR_UPLOAD_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 生成唯一文件名
        String fileExtension = getFileExtension(originalFilename);
        String newFileName = UUID.randomUUID().toString() + fileExtension;
        String filePath = AVATAR_UPLOAD_PATH + newFileName;

        // 保存文件
        try {
            Path path = Paths.get(filePath);
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new Exception("文件保存失败: " + e.getMessage());
        }

        // 删除旧头像文件（如果存在）
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            String oldAvatarPath = user.getAvatar();
            if (oldAvatarPath.startsWith("/uploads/")) {
                oldAvatarPath = oldAvatarPath.substring(1); // 移除开头的斜杠
            }
            File oldAvatarFile = new File(oldAvatarPath);
            if (oldAvatarFile.exists()) {
                oldAvatarFile.delete();
            }
        }

        // 更新数据库中的头像路径
        String avatarUrl = "/uploads/avatars/" + newFileName;
        int result = userMapper.updateUserAvatar(userId, avatarUrl);
        
        if (result > 0) {
            return avatarUrl;
        } else {
            throw new Exception("头像更新失败");
        }
    }

    // 检查是否为有效的图片文件
    private boolean isValidImageFile(String filename) {
        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif"};
        String lowerFilename = filename.toLowerCase();
        for (String ext : allowedExtensions) {
            if (lowerFilename.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    // 获取文件扩展名
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex);
        }
        return "";
    }

    @Override
    public int updateUserStatus(Long userId, Integer status) {
        return userMapper.updateUserStatus(userId, status);
    }

    @Override
    public int deleteUser(Long userId) {
        return userMapper.deleteUser(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectAllUsers();
    }
}