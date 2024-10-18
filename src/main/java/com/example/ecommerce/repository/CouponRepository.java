package com.example.ecommerce.repository;

import com.example.ecommerce.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    Optional<Coupon> findByCoupon(String name);
}
