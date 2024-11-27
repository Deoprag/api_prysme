package com.deopraglabs.api_prysme.data.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "customer")
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "cpf_cnpj", nullable = false, unique = true)
    private String cpfCnpj;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "trade_name")
    private String tradeName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "birth_foundation_date")
    private LocalDate birthFoundationDate;

    @Column(name = "state_registration", unique = true)
    private String stateRegistration;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_status")
    private CustomerStatus customerStatus;

    @OneToMany(mappedBy = "customer", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Address address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id")
    private User seller;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date lastModifiedDate;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by")
    private User lastModifiedBy;
}