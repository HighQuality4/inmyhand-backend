package com.inmyhand.refrigerator.subscription;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "subscription_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubscriptionPlansEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subplan_id")
    private Long id;

    @Column(name = "plan_name", nullable = false, length = 100)
    private String planName;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "payment_interval", nullable = false)
    private Integer paymentInterval;

    @Column(name = "interval_unit", nullable = false, length = 10)
    private String intervalUnit;

    @Column(name = "created_at", columnDefinition = "timestamp default current_timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regdate;

    @PrePersist
    protected void onCreate() {
        if (this.regdate == null) {
            this.regdate = new Date();
        }
    }

    @OneToMany(mappedBy = "subscriptionPlansEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<SubscriptionEntity> subscriptionList = new ArrayList<>();
}

