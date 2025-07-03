package com.deopraglabs.api_prysme.data.model;

import com.deopraglabs.api_prysme.data.enums.ContactType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "contact_info")
public class ContactInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_type", nullable = false)
    private ContactType contactType;

    @Column(name = "value")
    private String value;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;
}
