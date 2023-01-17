package com.example.system.model;

import com.example.system.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class UserOrder {
    private Integer userId;

    private String name;
    @Id
    private Integer orderId;

    private Date tradeDate;

    private Integer amount;

    @Enumerated(EnumType.STRING)
    private Status status;

}
