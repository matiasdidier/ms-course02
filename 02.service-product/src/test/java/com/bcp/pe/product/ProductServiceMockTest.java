package com.bcp.pe.product;

import com.bcp.pe.product.entity.Category;
import com.bcp.pe.product.entity.Product;
import com.bcp.pe.product.repository.ProductoRepository;
import com.bcp.pe.product.service.ProductService;
import com.bcp.pe.product.service.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
public class ProductServiceMockTest {
    @Mock
    private ProductoRepository productoRepository;

    private ProductService productService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        productService = new ProductServiceImpl(productoRepository);
        Product computer = Product.builder()
                .id(1L)
                .name("computer")
                .category(Category.builder().id(1L).build())
                .description("")
                .stock(Double.parseDouble("5"))
                .price(Double.parseDouble("12.5"))
                .status("Created")
                .createAt(new Date()).build();
        Mockito.when(productoRepository.findById(1L))
                .thenReturn(Optional.of(computer));
        Mockito.when(productoRepository.save(computer)).thenReturn(computer);
    }
    @Test
    public void whenValidGetId_ThenReturnProduct(){
        Product found = productService.getProduct(1L);
        Assertions.assertTrue(found.getName().equals("computer"));
    }
    @Test
    public void whenValidUpdateStock_ThenReturnNewStock(){
        Product newProducto = productService.updateStock(1L, Double.parseDouble("8"));
        Assertions.assertTrue(newProducto.getStock()==13);
    }
}
