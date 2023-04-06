package com.example.system.model.dto;

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
    private Integer orderId;
    private Timestamp tradeDate;
    private Integer totalAmount;

    @Enumerated(EnumType.STRING)
    private Status status;

    private List<BookedProduct> products;

}
