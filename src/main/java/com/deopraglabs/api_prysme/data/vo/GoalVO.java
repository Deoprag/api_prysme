package com.deopraglabs.api_prysme.data.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder("id")
public class GoalVO extends RepresentationModel<GoalVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private long key;
    private BigDecimal goal;
    private BigDecimal currentProgress;
    private long sellerId;
    private String seller;
    private Date startDate;
    private Date endDate;
    private Date createdDate;
    private Date lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;
}
