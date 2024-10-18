package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    Optional<Coupon> findByCoupon(String name);
}
