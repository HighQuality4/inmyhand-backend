package com.inmyhand.refrigerator.fridge.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDTO {

    private String product;
    private String quantity;
    private String category;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;


    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "ReceiptDTO{" +
                "product='" + product + '\'' +
                ",category='" + category + '\'' +
                ", quantity=" + quantity +
                ", purchaseDate=" + (purchaseDate != null ? sdf.format(purchaseDate) : "null") +
                ", expirationDate=" + (expirationDate != null ? sdf.format(expirationDate) : "null") +
                '}';
    }

}