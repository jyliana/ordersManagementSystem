package com.example.system.model.dto;

import com.example.system.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FullOrder {
    private Integer id;
    private Date tradeDate;
    private Integer amount;

    @Enumerated(EnumType.STRING)
    private Status status;

    private List<BookedProduct> products;

}
