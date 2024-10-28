package org.example.ecommerce.services;

import org.example.ecommerce.dtos.CartItemRequestDTO;
import org.example.ecommerce.dtos.CartItemResponseDTO;
import org.example.ecommerce.system.exceptions.exceptionHandling.exception.BadRequestException;
import org.example.ecommerce.system.exceptions.exceptionHandling.exception.NotFoundException;
import org.example.ecommerce.mappers.ProductCartMapper;
import org.example.ecommerce.models.*;
import org.example.ecommerce.repositories.CartItemRepository;
import org.example.ecommerce.repositories.ProductRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CustomerService customerService;
    private final ProductRepository productRepository;
    private final ProductCartMapper productCartMapper;
    public CartItemService(CartItemRepository cartItemRepository,
                           CustomerService customerService,
                           ProductRepository productRepository,
                           ProductCartMapper productCartMapper) {
        this.cartItemRepository = cartItemRepository;
        this.customerService = customerService;
        this.productRepository = productRepository;
        this.productCartMapper = productCartMapper;
    }

    @Transactional
    public CartItemResponseDTO setQuantity(Long productId, int quantity) {
        Customer customer = customerService.findUserByEmail(
                (String) ((Jwt)SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal()).getClaims().get(
                                "sub"));


        Product product =
                productRepository.findById(productId).
                        orElseThrow(()->new NotFoundException("Product not found"));

        if(product.getStock() < -1) {
            throw new BadRequestException("Quantity cannot be negative");
        }

        if(product.getStock() < quantity) {
            throw new BadRequestException("Available quantity is not enough");
        }

        CartKey cartKey = new CartKey(customer, product);

        CartItem cartItem = cartItemRepository.findById(cartKey).orElseGet(CartItem::new);
        cartItem.setQuantity(quantity);
        cartItem.setCustomer(customer);
        cartItem.setProduct(product);
        if (quantity == 0) {
            cartItemRepository.delete(cartItem);
            return null;
        }

        CartItem saved = cartItemRepository.save(cartItem);

        CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
        cartItemResponseDTO.setQuantity(saved.getQuantity());
        cartItemResponseDTO.setProduct(productCartMapper.toDTO(cartItem.getProduct()));
        return cartItemResponseDTO;
    }

    @Transactional
    public CartItemResponseDTO addQuantity(Long productId, int quantity) {
        Customer customer = customerService.findUserByEmail(
                (String) ((Jwt)SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal()).getClaims().get(
                        "sub"));


        Product product =
                productRepository.findById(productId).
                        orElseThrow(()->new NotFoundException("Product not found"));

        if(product.getStock() < -1) {
            throw new BadRequestException("Quantity cannot be negative");
        }

        CartKey cartKey = new CartKey(customer, product);

        Optional<CartItem> item = cartItemRepository.findById(cartKey);
        CartItem cartItem = null;
        if(item.isPresent()) {
            cartItem = item.get();
            if(cartItem.getQuantity() + quantity <= product.getStock()) {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            }
            else {
                throw new BadRequestException("Available quantity is not enough");
            }
        }
        else {
            cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
        }


        CartItem saved = cartItemRepository.save(cartItem);

        CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
        cartItemResponseDTO.setQuantity(saved.getQuantity());
        cartItemResponseDTO.setProduct(productCartMapper.toDTO(cartItem.getProduct()));
        return cartItemResponseDTO;
    }

    @Transactional
    public boolean emptyCart(Long customerId) {
        cartItemRepository.deleteAll(cartItemRepository.findByCustomerId(customerId));
        return true;
    }
    
    public List<CartItemResponseDTO> findByCustomerId(Long customerId) {
        return cartItemRepository.findByCustomerId(customerId).stream().map((cartItem) -> {
            CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
            cartItemResponseDTO.setQuantity(cartItem.getQuantity());
            cartItemResponseDTO.setProduct(productCartMapper.toDTO(cartItem.getProduct()));
            return cartItemResponseDTO;
        }).toList();
    }

    public List<CartItem> findCartItemsByCustomerId(Long customerId) {
        return cartItemRepository.findByCustomerId(customerId);
    }

    public List<CartItemResponseDTO> findCartForCurrentUser() {
        var claims =
                ((Jwt)SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal()).getClaims();
        String email = (String) claims.get("sub");

        return cartItemRepository.findByCustomerEmail(email).stream().map((cartItem) -> {
            CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
            cartItemResponseDTO.setQuantity(cartItem.getQuantity());
            cartItemResponseDTO.setProduct(productCartMapper.toDTO(cartItem.getProduct()));
            return cartItemResponseDTO;
        }).toList();
    }

    @Transactional
    public List<CartItemResponseDTO> mergeCart(List<CartItemRequestDTO> cartItemRequestDTOS) {
        var claims =
                ((Jwt)SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal()).getClaims();
        String email = (String) claims.get("sub");
        Map<Long,CartItem> productCartMap=
                cartItemRepository.findByCustomerEmail(email).stream()
                        .collect(HashMap::new,
                                (bucket, item)->bucket.put(
                                        item.getProduct().getId(), item),
                                HashMap::putAll);

        System.out.println("@%%%%%%%%%%");
        cartItemRequestDTOS.forEach((item)->{
            if(productCartMap.containsKey(item.getProductId())){
                productCartMap.get(item.getProductId()).setQuantity(item.getQuantity());
            }
            else {
                CartItem cartItem = new CartItem();
                cartItem.setQuantity(item.getQuantity());
                System.out.println("^^^");
                cartItem.setCustomer(customerService.findUserByEmail(email));
                System.out.println("^^^");
                cartItem.setProduct(
                        productRepository.findById(item.getProductId()).
                        orElseThrow(()->new NotFoundException("Product not found")));
                System.out.println("^^^");
                cartItemRepository.save(cartItem);
                System.out.println("^^^");
            }
        });
        System.out.println("@%%%%%%%%%%");
        return cartItemRepository.findByCustomerEmail(email).stream().map((cartItem) -> {
            CartItemResponseDTO cartItemResponseDTO = new CartItemResponseDTO();
            cartItemResponseDTO.setQuantity(cartItem.getQuantity());
            cartItemResponseDTO.setProduct(productCartMapper.toDTO(cartItem.getProduct()));
            return cartItemResponseDTO;
        }).toList();
    }
}
