package com.deopraglabs.api_prysme.data.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "nf")
public class NF implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nf_key", nullable = false, unique = true)
    private String nfKey;

    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate = LocalDateTime.now();

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_id", nullable = false, unique = true)
    private SalesOrder salesOrder;

    @OneToMany(mappedBy = "nf", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemProduct> items = new ArrayList<>();

    @Column(name = "total_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalValue;

    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NFStatus status;

    @Column(name = "observations")
    private String observations;

    public BigDecimal calculateFinalTotal() {
        BigDecimal total = totalValue;

        if (discountType == DiscountType.PERCENTAGE) {
            final BigDecimal discountAmount = total.multiply(discount.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
            total = total.subtract(discountAmount);
        } else if (discountType == DiscountType.FIXED) {
            total = total.subtract(discount);
        }

        return total.setScale(2, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : total;
    }
}


