package org.example.ecommerce.specifications;

import org.example.ecommerce.models.Order;
import org.example.ecommerce.models.OrderState;
import org.example.ecommerce.models.PaymentMethod;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class OrderSpecs {
    public static Specification<Order> hasDateBefore(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(
                "date"), date);
    }

    public static Specification<Order> hasDateAfter(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(
                "date"), date);
    }

    public static Specification<Order> hasPriceLessThan(int price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get(
                "totalPrice"), price);
    }

    public static Specification<Order> hasPriceGreaterThan(int price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(
                "totalPrice"), price);
    }

    public static Specification<Order> hasState(OrderState state) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(
                "state"), state);
    }

    public static Specification<Order> hasCustomerId(Long customerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(
                "customer").get("id"), customerId);
    }

    public static Specification<Order> hasPaymentMethod(
            PaymentMethod paymentMethod){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(
                "paymentMethod"), paymentMethod);
    }

    public static Specification<Order> hasPayment(
            List<PaymentMethod> paymentMethods){
        return (root, query, criteriaBuilder) ->
                root.get("paymentMethod").in(paymentMethods);
    }

    public static Specification<Order> hasOrderState(
            List<OrderState> orderStates){
        return (root, query, criteriaBuilder) ->
                root.get("state").in(orderStates);
    }

}
