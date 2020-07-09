package com.bcp.pe.shopping.service;

import com.bcp.pe.shopping.entity.Invoice;
import com.bcp.pe.shopping.repository.InvoiceItemRepository;
import com.bcp.pe.shopping.repository.InvoiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class InvoiceServiceImpl implements InvoiceService{

    @Autowired
    InvoiceItemRepository invoiceItemRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Override
    public List<Invoice> findInvoiceAll() {
        return invoiceRepository.findAll();
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        Invoice invoiceDB = invoiceRepository.findByNumberInvoice(invoice.getNumberInvoice());
        if (invoiceDB != null){
            return invoiceDB;
        }
        invoice.setState("CREATED");
        invoiceDB = invoiceRepository.save(invoice);
        /*
        invoiceDB.getItems().forEach( invoiceItem -> {
            productClient.updateStockProduct( invoiceItem.getProductId(), invoiceItem.getQuantity() * -1);
        });
         */
        return invoiceDB;
    }

    @Override
    public Invoice updateInvoice(Invoice invoice) {
        Invoice invoiceDB = invoiceRepository.findByNumberInvoice(invoice.getNumberInvoice());
        if (invoiceDB == null){
            return null;
        }
        invoiceDB.setCustomerId(invoice.getCustomerId());
        invoiceDB.setDescription(invoice.getDescription());
        invoiceDB.setNumberInvoice(invoice.getNumberInvoice());
        invoiceDB.getItems().clear();
        invoiceDB.setItems(invoice.getItems());
        invoiceDB = invoiceRepository.save(invoiceDB);

        return invoiceDB;
    }

    @Override
    public Invoice deleteInvoice(Invoice invoice) {
        Invoice invoiceDB = invoiceRepository.findByNumberInvoice(invoice.getNumberInvoice());
        if (invoiceDB == null){
            return null;
        }
        invoice.setState("DELETED");
        invoiceDB = invoiceRepository.save(invoice);
        return invoiceDB;
    }

    @Override
    public Invoice getInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        /*
        if (null != invoice ){
            Customer customer = customerClient.getCustomer(invoice.getCustomerId()).getBody();
            invoice.setCustomer(customer);
            List<InvoiceItem> listItem=invoice.getItems().stream().map(invoiceItem -> {
                Product product = productClient.getProduct(invoiceItem.getProductId()).getBody();
                invoiceItem.setProduct(product);
                return invoiceItem;
            }).collect(Collectors.toList());
            invoice.setItems(listItem);
        }
         */
        return invoice;
    }
}
