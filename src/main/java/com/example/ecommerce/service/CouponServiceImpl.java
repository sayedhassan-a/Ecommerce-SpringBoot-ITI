package com.example.ecommerce.service;

import com.example.ecommerce.entity.Coupon;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.repository.CouponRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public Coupon addCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public void deleteCoupon(Coupon coupon) {
        couponRepository.delete(coupon);
    }

    @Override
    public Coupon updateCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public Coupon findCouponById(int id) {
        Coupon c = couponRepository.findById(id).orElse(null);
        return c;
    }

    @Override
    public Page<Coupon> findAllCoupons(int page, int size) {
        Page<Coupon> coupons = couponRepository.findAll(PageRequest.of(page,
                size));
        return coupons;
    }

    @Override
    public Coupon findCouponByName(String name) {
        Coupon c = couponRepository.findByCoupon(name).orElse(null);
        return c;
    }

    @Override
    public List<Order> getAllOrdersByCouponName(String couponName) {
        Coupon c = couponRepository.findByCoupon(couponName).orElse(null);
        List<Order> orders = c.getOrders().stream().toList();
        return orders;
    }

    public List<Integer> getCouponInfo(String couponName){
        Coupon coupon = couponRepository.findByCoupon(couponName).orElse(null);
        List<Integer> info = new ArrayList<>();
        info.add(coupon.getPercentage());
        info.add(coupon.getLimitPayment());
        return info;
    }

    public boolean validateCoupon(String couponName){
        Coupon coupon = couponRepository.findByCoupon(couponName).orElse(null);
        if (coupon != null){
            if(coupon.getEndDate().before(new Date())) {
                return false;
            }
            return true;
        }
        return false;
    }
}
