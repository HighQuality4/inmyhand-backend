package com.inmyhand.refrigerator.payment.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "subscription")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString(exclude = {"subscriptionPlansEntity", "paymentList"})
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_id")
    private Long id;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "next_pay_date")
    private Timestamp nextPayDate;

    @Column(name = "canceled_at")
    private Timestamp canceledAt;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    @PrePersist
    protected void onCreate() {
        if (this.regdate == null) {
            this.regdate = new Date();
        }
        if(this.startDate == null) {
            this.startDate = new Timestamp(this.regdate.getTime());
        }
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnoreProperties("subscriptionList")
    private SubscriptionPlansEntity subscriptionPlansEntity;

    @OneToMany(mappedBy = "subscriptionEntity")
    @JsonIgnoreProperties("subscriptionEntity")
    private List<PaymentEntity> paymentList = new ArrayList<>();
}

