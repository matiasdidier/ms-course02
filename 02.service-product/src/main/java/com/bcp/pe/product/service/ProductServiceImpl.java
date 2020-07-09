package com.bcp.pe.product.service;

import com.bcp.pe.product.entity.Category;
import com.bcp.pe.product.entity.Product;
import com.bcp.pe.product.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements  ProductService{

    private final ProductoRepository productoRepository;

    @Override
    public List<Product> listAllProduct() {
        return productoRepository.findAll();
    }

    @Override
    public Product getProduct(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public Product createProduct(Product product) {
        product.setStatus("CREATED");
        product.setCreateAt(new Date());
        return productoRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        Product productDB = getProduct(product.getId());
        if (productDB == null){
            return null;
        }
        productDB.setName(product.getName());
        productDB.setDescription(product.getDescription());
        productDB.setCategory(product.getCategory());
        productDB.setPrice(product.getPrice());
        return productoRepository.save(productDB);
    }

    @Override
    public Product deleteProduct(Long id) {
        Product productDB = getProduct(id);
        if (productDB == null){
            return null;
        }
        productDB.setStatus("DELETED");
        return productoRepository.save(productDB);
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return productoRepository.findByCategory(category);
    }

    @Override
    public Product updateStock(Long id, Double quantity) {
        Product productDB = getProduct(id);
        if (productDB == null){
            return null;
        }
        Double stock = productDB.getStock() + quantity;
        productDB.setStock(stock);
        return productoRepository.save(productDB);
    }
}
