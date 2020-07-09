package com.bcp.pe.product.repository;

import com.bcp.pe.product.entity.Category;
import com.bcp.pe.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ProductoRepository  extends JpaRepository<Product, Long> {

    public List<Product> findByCategory(Category category);

}
