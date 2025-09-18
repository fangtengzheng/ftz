package com.example.a.service;

import com.example.a.entity.Shipping;

import java.util.List;

public interface AddressService {

    int addAddress(Shipping shipping);
    Shipping getAddress(Long userId, Long shippingId);
    List<Shipping> getAllAddresses(Long userId);
    int updateAddress(Shipping shipping);
    int deleteAddress(Long userId, Long shippingId);

    int setDefaultAddress(Long userId, Long shippingId);
    Shipping getDefaultAddress(Long userId);
}