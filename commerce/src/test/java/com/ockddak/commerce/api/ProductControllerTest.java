package com.ockddak.commerce.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ockddak.commerce.domain.Product;
import com.ockddak.commerce.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ProductController 통합 테스트")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    public void cleanup() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/products - 상품을 생성할 수 있다.")
    void createProductTest() throws Exception {
        // given
        Product newProduct = Product.builder()
                .name("모니터")
                .price(300000.0)
                .stock(5)
                .description("27인치 게이밍 모니터입니다.")
                .build();
        String jsonRequest = objectMapper.writeValueAsString(newProduct);

        // when & then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("모니터"))
                .andExpect(jsonPath("$.price").value(300000.0));
    }

    @Test
    @DisplayName("GET /api/products/{id} - 특정 상품을 조회할 수 있다.")
    void getProductByIdTest() throws Exception {
        // given - 테스트용 상품을 DB에 미리 저장
        Product savedProduct = productRepository.save(Product.builder()
                .name("노트북")
                .price(1500000.0)
                .stock(10)
                .description("고성능 노트북입니다.")
                .build());

        // when & then
        mockMvc.perform(get("/api/products/{id}", savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("노트북"));
    }

    @Test
    @DisplayName("PUT /api/products/{id} - 상품 정보를 수정할 수 있다.")
    void updateProductTest() throws Exception {
        // given - 수정할 상품을 미리 저장
        Product existingProduct = productRepository.save(Product.builder()
                .name("키보드")
                .price(50000.0)
                .stock(50)
                .description("기계식 키보드입니다.")
                .build());

        // given - 수정 내용이 담긴 새로운 상품 객체 생성
        Product updatedProduct = Product.builder()
                .name("무선 키보드")
                .price(60000.0)
                .stock(45)
                .description("무선 기계식 키보드입니다.")
                .build();
        String jsonRequest = objectMapper.writeValueAsString(updatedProduct);

        // when & then
        mockMvc.perform(put("/api/products/{id}", existingProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("무선 키보드"))
                .andExpect(jsonPath("$.price").value(60000.0));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} - 상품을 삭제할 수 있다.")
    void deleteProductTest() throws Exception {
        // given - 삭제할 상품을 미리 저장
        Product productToDelete = productRepository.save(Product.builder()
                .name("마우스")
                .price(25000.0)
                .stock(100)
                .description("게이밍 마우스입니다.")
                .build());

        // when & then
        mockMvc.perform(delete("/api/products/{id}", productToDelete.getId()))
                .andExpect(status().isNoContent()); // 204 No Content
    }
}