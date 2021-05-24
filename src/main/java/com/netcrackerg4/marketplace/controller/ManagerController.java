package com.netcrackerg4.marketplace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcrackerg4.marketplace.model.dto.product.NewProductDto;
import com.netcrackerg4.marketplace.service.interfaces.IProductService;
import com.netcrackerg4.marketplace.service.interfaces.IS3Service;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/manager")
@AllArgsConstructor
public class ManagerController {
    private final IS3Service s3Service;
    private final IProductService productService;

    @PostMapping("/add-product")
    void createProduct(@RequestParam(value = "file") MultipartFile multipartFile,
                    @RequestParam(value = "product")  String product) throws JsonProcessingException {

        UUID id = UUID.randomUUID();

        ObjectMapper mapper = new ObjectMapper();
        NewProductDto newProductDto = mapper.readValue(product, NewProductDto.class);

        URL url = s3Service.uploadImage(id, multipartFile);

        productService.addProduct(url.toString(),newProductDto);
    }
}
