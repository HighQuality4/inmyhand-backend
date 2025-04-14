package com.inmyhand.refrigerator.fridge.domain.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FridgeUsersDTO {
    private Long id;
    private Timestamp joinDate;
    private Boolean state;
    private Long usersId;
    private Long fridgeId;
}
