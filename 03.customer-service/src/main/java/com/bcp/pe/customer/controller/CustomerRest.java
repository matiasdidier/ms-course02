package com.bcp.pe.customer.controller;

import com.bcp.pe.customer.repository.entity.Customer;
import com.bcp.pe.customer.repository.entity.Region;
import com.bcp.pe.customer.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/customers")
public class CustomerRest {
    @Autowired
    CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> listAllCustomer(@RequestParam(name = "regionId", required = false) Long regionId){
        log.info("[listAllCustomer] Inicio");
        List<Customer> customers = null;
        Region region = null;
        if (regionId == null){
            customers = customerService.findCustomerAll();
            if (customers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        }else{
            region = new Region();
            region.setId(regionId);
            customers = customerService.findCustomersByRegion(region);
            if (customers == null){
                log.error("[listAllCustomer] Customers with Region id {} not found.",regionId);
                return ResponseEntity.notFound().build();
            }
        }

        log.info("[listAllCustomer] Fin");
        return ResponseEntity.ok(customers);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Customer> getCustomer(@PathVariable("id") Long id){
        log.info("[getCustomer] Inicio id:"+id);
        Customer customer = customerService.getCustomer(id);
        if (customer == null){
            return ResponseEntity.notFound().build();
        }
        log.info("[getCustomer] Fin");
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer, BindingResult result){
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Customer customerCreate = customerService.createCustomer(customer);
        return  ResponseEntity.ok(customerCreate);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Customer> updateCustormer(@PathVariable("id")Long id, @RequestBody Customer customer){
        Customer customerBD = customerService.getCustomer(id);
        if (customerBD== null){
            return ResponseEntity.notFound().build();
        }
        customer.setId(id);
        customerBD = customerService.updateCustomer(customer);
        return ResponseEntity.ok(customerBD);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Customer> deleteCustormer(@PathVariable("id")Long id){
        Customer customerBD = customerService.getCustomer(id);
        if (customerBD== null){
            return ResponseEntity.notFound().build();
        }
        customerBD = customerService.updateCustomer(customerBD);
        return ResponseEntity.ok(customerBD);
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
