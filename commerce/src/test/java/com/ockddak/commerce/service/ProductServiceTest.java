package com.ockddak.commerce.service;

import com.ockddak.commerce.domain.Product;
import com.ockddak.commerce.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService 단위 테스트")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;


    @Test
    @DisplayName("상품을 저장할 수 있다.")
    void saveProductTest() {
        // given
        Product newProduct = Product.builder()
                .name("노트북")
                .price(1500000.0)
                .stock(10)
                .description("새로운 노트북입니다.")
                .build();
        given(productRepository.save(any(Product.class))).willReturn(newProduct);

        // when
        Product savedProduct = productService.saveProduct(newProduct);

        // then
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("노트북");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("유효하지 않은 ID로 상품을 조회하면 예외가 발생한다.")
    void findProductByIdWithInvalidIdTest() {
        // given
        Long invalidId = 999L;
        given(productRepository.findById(invalidId)).willReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.findProductById(invalidId);
        });
        verify(productRepository).findById(invalidId);
    }

}