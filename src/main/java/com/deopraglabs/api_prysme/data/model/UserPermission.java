package com.deopraglabs.api_prysme.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_permission")
public class UserPermission {

    @EmbeddedId
    private UserPermissionId id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;
}

@Data
@Embeddable
class UserPermissionId implements Serializable {
    private long user;
    private long permission;
}
