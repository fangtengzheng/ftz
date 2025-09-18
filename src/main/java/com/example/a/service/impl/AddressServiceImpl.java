package com.example.a.service.impl;

import com.example.a.entity.Shipping;
import com.example.a.mapper.AddressMapper;
import com.example.a.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public int addAddress(Shipping shipping) {
        // 检查该用户是否已经有默认地址
        Shipping defaultAddress = addressMapper.selectDefaultAddress(shipping.getUserId());
        if (defaultAddress == null && shipping.getIsDefault() == null) {
            shipping.setIsDefault(1); // 如果没有默认地址，设置第一个地址为默认
        }
        return addressMapper.insertAddress(shipping);
    }

    @Override
    public Shipping getAddress(Long userId, Long shippingId) {
        return addressMapper.selectAddressById(userId, shippingId);
    }

    @Override
    public List<Shipping> getAllAddresses(Long userId) {
        return addressMapper.selectAddressesByUserId(userId);
    }

    @Override
    public int updateAddress(Shipping shipping) {
        return addressMapper.updateAddress(shipping);
    }

    @Override
    public int deleteAddress(Long userId, Long shippingId) {
        // 如果要删除的是默认地址，需要先取消默认
        Shipping address = addressMapper.selectAddressById(userId, shippingId);
        if (address != null && address.getIsDefault() == 1) {
            addressMapper.setDefaultAddress(userId, null);
        }
        return addressMapper.deleteAddress(userId, shippingId);
    }

    @Override
    public int setDefaultAddress(Long userId, Long shippingId) {
        // 取消该用户的其他默认地址
        addressMapper.clearDefaultAddress(userId);
        // 设置指定地址为默认地址
        return addressMapper.setDefaultAddress(userId, shippingId);
    }
    @Override
    public Shipping getDefaultAddress(Long userId) {
        return addressMapper.selectDefaultAddress(userId);
    }
}