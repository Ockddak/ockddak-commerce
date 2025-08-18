package com.ockddak.commerce.service;

import com.ockddak.commerce.domain.Product;
import com.ockddak.commerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(
            final ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
    }

    // 새로운 제품을 저장하는 기능 (CREATE)
    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // 모든 제품 목록을 조회하는 기능 (READ)
    public Iterable<Product> findAllProducts() {
        return productRepository.findAll();
    }

    // 특정 ID의 제품을 조회하는 기능 (READ)
    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
    }

    // 제품 정보를 업데이트하는 기능 (UPDATE)
    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));

        // setter 대신 엔티티의 update() 메서드 호출
        existingProduct.update(
                updatedProduct.getName(),
                updatedProduct.getPrice(),
                updatedProduct.getStock(),
                updatedProduct.getDescription()
        );

        // Dirty Checking(변경 감지) 덕분에 save()를 명시적으로 호출하지 않아도 됨
        return existingProduct;
    }

    // 특정 ID의 제품을 삭제하는 기능 (DELETE)
    @Transactional
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Invalid product Id:" + id);
        }
        productRepository.deleteById(id);
    }

}
