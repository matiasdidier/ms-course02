package com.bcp.pe.product;

import com.bcp.pe.product.entity.Category;
import com.bcp.pe.product.entity.Product;
import com.bcp.pe.product.repository.ProductoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

@DataJpaTest
public class ProductoRepositoryMockTest {

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    public void whenFindByCategory_thenResultListProduct(){
        Product product01 = Product.builder()
                            .name("computer")
                            .category(Category.builder().id(1L).build())
                            .description("")
                            .stock(Double.parseDouble("10"))
                            .price(Double.parseDouble("1240.99"))
                            .status("Created")
                            .createAt(new Date()).build();
        productoRepository.save(product01);
        List<Product> founds = productoRepository.findByCategory(product01.getCategory());
        Assertions.assertTrue(founds.size()==3);


    }
}
