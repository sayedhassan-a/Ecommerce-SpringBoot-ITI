package com.example.ecommerce.service;

import com.example.ecommerce.entity.*;
import com.example.ecommerce.enums.OrderState;
import com.example.ecommerce.repository.CouponRepository;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CouponRepository couponRepository;
    private final CartService cartService;
    private final CustomerService customerService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CouponRepository couponRepository, CartService cartService, CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.couponRepository = couponRepository;
        this.cartService = cartService;
        this.customerService = customerService;
    }

    @Override
    public synchronized Order addOrder(Customer customer, String coupon) {
        Set<CartHasProduct> cartHasProducts = customer.getCart().getCartHasProducts();
        Product product = null;
        boolean flag = false;
        for (CartHasProduct cartHasProduct : cartHasProducts) {
            if (cartHasProduct.getProduct().getStock() >= cartHasProduct.getQuantity()) {
                continue;
            } else {
                product = cartHasProduct.getProduct();
                flag = true;
                break;
            }
        }
        if (!flag) {
            Set<OrderItem> orderItems = new HashSet<>();
            Order order = new Order();
            order.setCustomer(customer);
            for (CartHasProduct cartHasProduct : cartHasProducts) {
                cartHasProduct.getProduct().setStock(cartHasProduct.getProduct().getStock() - cartHasProduct.getQuantity());
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(cartHasProduct.getProduct());
                orderItem.setQuantity(cartHasProduct.getQuantity());
                orderItem.setCurrentPrice(cartHasProduct.getProduct().getPrice());
                orderItems.add(orderItem);
                order.setTotalPrice((cartHasProduct.getProduct().getPrice()*cartHasProduct.getQuantity()) + order.getTotalPrice());
            }
            if (!coupon.isEmpty()) {
                Coupon c = couponRepository.findByCoupon(coupon).orElse(null);
                if (c != null) {
                    if(c.getEndDate().before(new Date())) {
                        System.out.println("Coupon is Expired");
                        return null;
                    }
                    order.setCoupon(c);
                    int discount = Math.min(order.getTotalPrice()*c.getPercentage()/100, c.getLimitPayment());
                    order.setTotalPrice(order.getTotalPrice() - discount);
                } else {
                    System.out.println("Coupon not found");
                    return null;
                }
            }
            order.setState(OrderState.PENDING);
            cartService.emptyCart(customer.getId());
            orderItemRepository.saveAll(orderItems);
            return orderRepository.save(order);
        } else {
            System.out.println("No Enough Stock for " + product.getName());
            throw new RuntimeException("No Enough Stock for " + product.getName());
        }
    }

    @Override
    public Order getOrder(int id) {
        Order order = orderRepository.findById(id).orElse(null);
        return order;
    }

    @Override
    public Page<Order> getOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAll(pageable);
    }

    @Override
    public Order updateOrderState(int id, OrderState state) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setState(state);
        return orderRepository.save(order);
    }


    public List<Order> getOrdersByCustomerId(int customerId) {
        Customer customer = customerService.findById(customerId);
        List<Order> orders = customer.getOrders().stream().toList();
        return orders;

    }

    public void deleteOrder(int id) {
        Order order =
                orderRepository.findById(id).orElseThrow(()->new RuntimeException("This Product doesn't exist"));
        orderRepository.delete(order);
    }
}
