package com.example.a.mapper;

import com.example.a.entity.Shipping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressMapper {

    int insertAddress(Shipping shipping);

    Shipping selectAddressById(@Param("userId") Long userId, @Param("shippingId") Long shippingId);

    List<Shipping> selectAddressesByUserId(Long userId);

    int updateAddress(Shipping shipping);
    int clearDefaultAddress(@Param("userId") Long userId);

    int deleteAddress(@Param("userId") Long userId, @Param("shippingId") Long shippingId);

    int setDefaultAddress(@Param("userId") Long userId, @Param("shippingId") Long shippingId);
    Shipping selectDefaultAddress(Long userId);
}