package com.deopraglabs.api_prysme.data.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder("id")
public class UserVO extends RepresentationModel<UserVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private long key;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private LocalDate birthDate;
    private char gender;
    private String phoneNumber;
    private String password;
    private boolean active;
    private TeamVO team;
    private List<TaskVO> tasks;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
