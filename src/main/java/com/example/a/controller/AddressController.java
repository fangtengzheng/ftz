package com.example.a.controller;

import com.example.a.entity.Shipping;
import com.example.a.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/address")

public class AddressController {

    @Autowired
    private AddressService addressService;

    // 添加收货地址
    @PostMapping
    @ResponseBody
    public Map<String, Object> addAddress(@RequestBody Shipping shipping) {
        Map<String, Object> result = new HashMap<>();
        int rows = addressService.addAddress(shipping);
        if (rows > 0) {
            result.put("msg", "添加成功");
        } else {
            result.put("msg", "添加失败");
        }
        return result;
    }


    // 获取收货地址
    @GetMapping("/{shippingId}")
    @ResponseBody
    public Map<String, Object> getAddress(@PathVariable Long shippingId, @RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        Shipping address = addressService.getAddress(userId, shippingId);
        if (address != null) {
            result.put("msg", "获取成功");
            result.put("address", address);
        } else {
            result.put("msg", "地址不存在");
        }
        return result;
    }

    // 获取所有收货地址
    @GetMapping
    @ResponseBody
    public Map<String, Object> getAllAddresses(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        List<Shipping> addresses = addressService.getAllAddresses(userId);
        result.put("msg", "获取成功");
        result.put("addresses", addresses);
        return result;
    }

    // 更新收货地址
    @PutMapping
    @ResponseBody
    public Map<String, Object> updateAddress(@RequestBody Shipping shipping) {
        Map<String, Object> result = new HashMap<>();
        int rows = addressService.updateAddress(shipping);
        if (rows > 0) {
            result.put("msg", "更新成功");
        } else {
            result.put("msg", "更新失败");
        }
        return result;
    }

    // 删除收货地址
    @DeleteMapping("/{shippingId}")
    @ResponseBody
    public Map<String, Object> deleteAddress(@PathVariable Long shippingId, @RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        int rows = addressService.deleteAddress(userId, shippingId);
        if (rows > 0) {
            result.put("msg", "删除成功");
        } else {
            result.put("msg", "删除失败");
        }
        return result;
    }

    // 设置默认地址
    @PutMapping("/{shippingId}/default")
    @ResponseBody
    public Map<String, Object> setDefaultAddress(@PathVariable Long shippingId, @RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        int rows = addressService.setDefaultAddress(userId, shippingId);
        if (rows > 0) {
            result.put("msg", "设置成功");
        } else {
            result.put("msg", "设置失败");
        }
        return result;
    }


    // 获取默认地址
    @GetMapping("/default")
    @ResponseBody
    public Map<String, Object> getDefaultAddress(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        Shipping address = addressService.getDefaultAddress(userId);
        if (address != null) {
            result.put("msg", "获取成功");
            result.put("address", address);
        } else {
            result.put("msg", "没有默认地址");
        }
        return result;
    }

    // 测试端点 - 验证数据库连接和地址数据
    @GetMapping("/test")
    @ResponseBody
    public Map<String, Object> testAddressAPI(@RequestParam Long userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Shipping> addresses = addressService.getAllAddresses(userId);
            result.put("msg", "测试成功");
            result.put("addresses", addresses);
            result.put("count", addresses.size());
            result.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            result.put("msg", "测试失败: " + e.getMessage());
            result.put("error", e.toString());
        }
        return result;
    }
}