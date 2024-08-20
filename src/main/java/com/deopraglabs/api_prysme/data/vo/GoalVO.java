package com.deopraglabs.api_prysme.data.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GoalVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long id;
    private BigDecimal goal;
    private UserVO seller;
    private LocalDate endDate;

}
