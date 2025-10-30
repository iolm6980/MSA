package com.example.orderservice.client;
import com.example.common.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@FeignClient(
        name = "product-service",
        fallback = ProductClientFallback.class
)
public interface ProductClient {
    @GetMapping("/products")
    List<ProductDto> getProducts();
}
