package com.bcp.pe.product.controller;

import com.bcp.pe.product.entity.Category;
import com.bcp.pe.product.entity.Product;
import com.bcp.pe.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/products")
public class ProductController {
    @Autowired(required=true)
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> ListProduct(@RequestParam(name="categoryId",required = false)Long categoryId){
        List<Product> products = null;
        if (categoryId == null){
            products = productService.listAllProduct();
            if (products.isEmpty()){
                return ResponseEntity.noContent().build();
            }
        }else{
            products = productService.findByCategory(Category.builder().id(categoryId).build());
            if (products.isEmpty()){
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok(products);

    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id){
        Product product = productService.getProduct(id);
        if (product == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody  Product product, BindingResult result ){
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }

        Product productCreate = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreate);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody  Product product){
        product.setId(id);
        Product productBD = productService.updateProduct(product);
        if (productBD == null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productBD);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id){
        Product productBD = productService.deleteProduct(id);
        if (productBD == null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productBD);
    }

    @GetMapping(value = "/{id}/stock")
    public ResponseEntity<Product> updateStockProducto(@PathVariable("id") Long id, @RequestParam(name="quantity",required = true) Double quantity){
        Product productBD = productService.updateStock(id,quantity);
        if (productBD == null){
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productBD);
    }

    private String formatMessage(BindingResult result){
        List<Map<String,String>> errors = result.getAllErrors().stream()
                .map(err ->{
                    Map<String, String> error = new HashMap<String, String>();
                    error.put(err.getCode(), err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());
        //Message Error
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        //Cast to Json
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try{
            jsonString = mapper.writeValueAsString(errorMessage);
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return jsonString;
    }


}
