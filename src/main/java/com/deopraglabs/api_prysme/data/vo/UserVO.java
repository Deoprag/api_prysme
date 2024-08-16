package com.deopraglabs.api_prysme.data.vo;

import com.deopraglabs.api_prysme.data.model.Role;
import com.deopraglabs.api_prysme.data.model.Team;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class UserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private LocalDate birthDate;
    private char gender;
    private String phoneNumber;
    private String password;
    private boolean active;
    private TeamVO team;

}
