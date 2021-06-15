package com.investor.backofficeinvestor.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "payments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "paymentId")
//                @UniqueConstraint(columnNames = "userId"),
        })
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotNull
//    @Size(max = 20)
//    @Column(name = "user_id")
//    private Long userId;

    @NotNull
    @Size(max = 20)
    @Column(name = "paymentId")
    private Long paymentId;

    @NotNull
    @Size(max = 20)
    @Column(name = "payment_date")
    private Date paymentDate;

//    @ManyToOne
//    @JoinColumn(name="user_id", referencedColumnName = "UniqueID")
//    private User user;

    public Payment(Long id, Long paymentId, Date paymentDate) {
        this.id = id;
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
    }

    public Payment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Long userId) {
//        this.userId = userId;
//    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
