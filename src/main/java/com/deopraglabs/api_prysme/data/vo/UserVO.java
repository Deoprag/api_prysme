package com.deopraglabs.api_prysme.data.vo;

import com.deopraglabs.api_prysme.data.model.Permission;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder("id")
public class UserVO extends RepresentationModel<UserVO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("id")
    private long key;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private char gender;
    private String phoneNumber;
    private String password;
    private boolean enabled;
    private long teamId;
    private String team;
    private List<String> permissions;
    private List<TaskVO> tasks;
    private Date createdDate;
    private Date lastModifiedDate;
}
