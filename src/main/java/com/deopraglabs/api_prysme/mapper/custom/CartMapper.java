package com.deopraglabs.api_prysme.mapper.custom;

import com.deopraglabs.api_prysme.data.model.Cart;
import com.deopraglabs.api_prysme.data.model.Product;
import com.deopraglabs.api_prysme.data.vo.CartVO;
import com.deopraglabs.api_prysme.data.vo.ProductVO;
import com.deopraglabs.api_prysme.repository.CustomerRepository;
import com.deopraglabs.api_prysme.utils.exception.CustomRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartMapper {

    private final ProductMapper productMapper;
    private final CustomerRepository customerRepository;

    @Autowired
    public CartMapper(ProductMapper productMapper, CustomerRepository customerRepository) {
        this.productMapper = productMapper;
        this.customerRepository = customerRepository;
    }

    public CartVO convertToVO(Cart cart) {
        final CartVO vo = new CartVO();

        vo.setKey(cart.getId());
        for (final Product product : cart.getProducts()) {
            vo.getProducts().add(productMapper.convertToVO(product));
        }
        vo.setCustomerId(cart.getCustomer().getId());
        vo.setCustomer(cart.getCustomer().getName());
        vo.setTotalPrice(calculateTotalPrice(cart.getProducts()));

        return vo;
    }

    public Cart convertFromVO(CartVO cartVO) {
        final Cart cart = new Cart();

        cart.setId(cartVO.getKey());
        for (final ProductVO productVO : cartVO.getProducts()) {
            cart.getProducts().add(productMapper.convertFromVO(productVO));
        }
        cart.setCustomer(customerRepository.findById(cartVO.getCustomerId())
                .orElseThrow(() -> new CustomRuntimeException.CustomerNotFoundException(cartVO.getCustomerId())));

        return cart;
    }

    public double calculateTotalPrice(List<Product> products) {
        double sum = 0;
        for (final Product product : products) {
            sum += product.getPrice().doubleValue();
        }
        return sum;
    }
}