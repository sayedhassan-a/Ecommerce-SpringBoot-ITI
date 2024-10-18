package com.example.ecommerce.service;

import com.example.ecommerce.entity.Coupon;
import com.example.ecommerce.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CouponService {
    Coupon addCoupon(Coupon coupon);
    void deleteCoupon(Coupon coupon);
    Coupon updateCoupon(Coupon coupon);
    Coupon findCouponById(int id);
    Page<Coupon> findAllCoupons(int page, int size);
    Coupon findCouponByName(String name);
    List<Order> getAllOrdersByCouponName(String couponName);

}
