package com.example.common.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor // 🔥 Jackson 역직렬화 오류 방지
public class ProductDto {
    private String id;
    private String name;
    private int price;
}
