package com.kadri.product.service;

import com.kadri.product.model.Product;
import com.kadri.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Product create(Product product){
        return repository.save(product);
    }

    public Product get(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not Found " + id));
    }

    public List<Product> getAll(){
        return repository.findAll();
    }

    public Product update(Long id, Product updatedProduct){
        Product existingProduct = get(id);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStock(updatedProduct.getStock());
        return repository.save(existingProduct);
    }
    public void delete(Long id){
        repository.deleteById(id);
    }

    public List<Product> findByIds(List<Long> ids) {
        return repository.findByIdIn(ids);
    }
}
