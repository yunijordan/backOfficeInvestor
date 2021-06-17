package com.investor.backofficeinvestor.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "payments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id")
//                @UniqueConstraint(columnNames = "userId"),
        })
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "paymentId")
    private Long paymentId;

    @NotNull
    @Size(max = 20)
    @Column(name = "payment_date")
    private Date paymentDate;

    @NotNull
    @Size(max = 50)
    @Column(name = "payment_status_code")
    private String payStatusCode;

    @NotNull
    @Size(max = 20)
    @Column(name = "payment_details")
    private String payDetails;

    @NotNull
    @Size(max = 20)
    @Column(name = "failure_code")
    private String failureCode;

    @NotNull
    @Size(max = 20)
    @Column(name = "failure_message")
    private String failureMessage;



    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private User user;


    public Payment(Long id, Long paymentId, Date paymentDate, String payStatusCode, String payDetails, String failureCode, String failureMessage) {
        this.id = id;
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.payStatusCode = payStatusCode;
        this.payDetails = payDetails;
        this.failureCode = failureCode;
        this.failureMessage = failureMessage;
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

    public String getPayStatusCode() {
        return payStatusCode;
    }

    public void setPayStatusCode(String payStatusCode) {
        this.payStatusCode = payStatusCode;
    }

    public String getPayDetails() {
        return payDetails;
    }

    public void setPayDetails(String payDetails) {
        this.payDetails = payDetails;
    }

    public String getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(String failureCode) {
        this.failureCode = failureCode;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }
}
