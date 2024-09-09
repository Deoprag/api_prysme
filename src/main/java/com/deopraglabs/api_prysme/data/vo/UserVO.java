package com.deopraglabs.api_prysme.data.vo;

import com.deopraglabs.api_prysme.data.model.Task;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class UserVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long id;
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
    private List<Task> tasks;
}
