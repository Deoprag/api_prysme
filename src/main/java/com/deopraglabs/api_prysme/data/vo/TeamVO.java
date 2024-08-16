package com.deopraglabs.api_prysme.data.vo;

import com.deopraglabs.api_prysme.data.model.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class TeamVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
    private long managerId;
    private String manager;
    private List<User> sellers;

}
