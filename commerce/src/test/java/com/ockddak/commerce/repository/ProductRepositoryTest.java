package com.ockddak.commerce.repository;

import com.ockddak.commerce.domain.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("ProductRepository 통합 테스트")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    public void cleanup() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("상품을 저장하고 ID로 조회할 수 있다.")
    void saveAndFindByIdTest() {
        //given
        Product newProduct = Product.builder()
                .name("노트북")
                .price(1500000.0)
                .stock(10)
                .description("고성능 노트북입니다.")
                .build();

        //when
        Product savedProduct = productRepository.save(newProduct);

        //then
        Product foundProduct = productRepository.findById(savedProduct.getId()).orElse(null);
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getName()).isEqualTo(newProduct.getName());
        assertThat(foundProduct.getPrice()).isEqualTo(newProduct.getPrice());
    }

    @Test
    @DisplayName("상품 ID로 상품을 삭제할 수 있다.")
    void deleteProductByIdTest() {
        // given - 미리 저장된 상품이 있는 상태를 준비한다.
        Product newProduct = Product.builder()
                .name("키보드")
                .price(50000.0)
                .stock(50)
                .description("기계식 키보드입니다.")
                .build();

        Product savedProduct = productRepository.save(newProduct);
        Long productId = savedProduct.getId();

        // when - 특정 ID를 가진 상품을 삭제하는 행동을 실행한다.
        productRepository.deleteById(productId);

        // then - 상품이 더 이상 존재하지 않음을 검증한다.
        assertThat(productRepository.findById(productId)).isEmpty();
    }
}