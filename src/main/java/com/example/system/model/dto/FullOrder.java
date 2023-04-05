package com.example.system.model.dto;

import com.example.system.model.Product;
import com.example.system.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullOrder {
    private Integer id;
    private Timestamp tradeDate;
    private Integer amount;

    @Enumerated(EnumType.STRING)
    private Status status;

    private List<Product> products;

}
