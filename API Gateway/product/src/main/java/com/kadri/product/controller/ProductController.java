package com.kadri.product.controller;

import com.kadri.product.dto.ProductRequest;
import com.kadri.product.dto.ProductResponse;
import com.kadri.product.mapper.ProductMapper;
import com.kadri.product.model.Product;
import com.kadri.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;
    private final ProductMapper mapper;

    @PostMapping
    public ProductResponse create(@RequestBody ProductRequest request){
        Product productProduct = service.create(mapper.toEntity(request));
        return mapper.toResponse(productProduct);
    }

    @GetMapping("/{id}")
    public ProductResponse get(@PathVariable Long id){
        return mapper.toResponse(service.get(id));
    }

    @GetMapping
    public List<ProductResponse> getAll(){
        return service.getAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @RequestBody ProductRequest request){
        Product updatedProduct = service.update(id, mapper.toEntity(request));
        return mapper.toResponse(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }
}
