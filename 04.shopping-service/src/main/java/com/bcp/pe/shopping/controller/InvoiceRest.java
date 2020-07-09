package com.bcp.pe.shopping.controller;

import com.bcp.pe.shopping.entity.Invoice;
import com.bcp.pe.shopping.service.InvoiceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(name = "/invoices")
public class InvoiceRest {
    @Autowired
    InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<Invoice>> listAllInvoices(){
        log.info("[listAllInvoices] Inicio");
        List<Invoice> invoices = invoiceService.findInvoiceAll();
        if (invoices.isEmpty()){
            log.info("[listAllInvoices] Empty");
            return ResponseEntity.noContent().build();
        }
        log.info("[listAllInvoices] Fin");
        return ResponseEntity.ok(invoices);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Invoice>  getInvoice(@PathVariable(value = "id") Long id){
        log.info("[getInvoice] Inicio");
        Invoice invoice = invoiceService.getInvoice(id);
        if (invoice == null){
            log.info("[getInvoice] Null");
            return  ResponseEntity.notFound().build();
        }
        log.info("[getInvoice] Fin");
        return ResponseEntity.ok(invoice);
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice, BindingResult result){
        if(result.hasErrors()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Invoice invoiceBD = invoiceService.createInvoice(invoice);
        return  ResponseEntity.status(HttpStatus.CREATED).body(invoiceBD);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable("id") long id, @RequestBody Invoice invoice) {
        log.info("Updating Invoice with id {}", id);

        invoice.setId(id);
        Invoice currentInvoice=invoiceService.updateInvoice(invoice);

        if (currentInvoice == null) {
            log.error("Unable to update. Invoice with id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(currentInvoice);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Invoice> deleteInvoice(@PathVariable("id") long id) {
        log.info("Fetching & Deleting Invoice with id {}", id);

        Invoice invoice = invoiceService.getInvoice(id);
        if (invoice == null) {
            log.error("Unable to delete. Invoice with id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        invoice = invoiceService.deleteInvoice(invoice);
        return ResponseEntity.ok(invoice);
    }

    private String formatMessage( BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String> error =  new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;

                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
