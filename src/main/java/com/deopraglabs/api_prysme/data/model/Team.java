package com.deopraglabs.api_prysme.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "team")
public class Team implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", unique = true, nullable = false)
    private User manager;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private List<User> sellers;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public User getManager() {
        return manager;
    }

    @JsonProperty
    public void setManager(User manager) {
        this.manager = manager;
    }

    @JsonIgnore
    public List<User> getSellers() {
        return sellers;
    }

    @JsonProperty
    public void setSellers(List<User> sellers) {
        this.sellers = sellers;
    }
}
