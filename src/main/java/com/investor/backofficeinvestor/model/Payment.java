package com.investor.backofficeinvestor.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

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
    @Column(name = "stripe_id")
    private String stripeId;

    @Column(name = "payment_date")
    private ZonedDateTime paymentDate;

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

    @Size(max = 20)
    @Column(name = "payment_email")
    private String paymentEmail;



    public Payment(Long id, String stripeId, ZonedDateTime paymentDate, String payStatusCode, String payDetails, String failureCode, String failureMessage, String paymentEmail) {
        this.id = id;
        this.stripeId = stripeId;
        this.paymentDate = paymentDate;
        this.payStatusCode = payStatusCode;
        this.payDetails = payDetails;
        this.failureCode = failureCode;
        this.failureMessage = failureMessage;
        this.paymentEmail = paymentEmail;
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


    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public ZonedDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(ZonedDateTime paymentDate) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPaymentEmail() {
        return paymentEmail;
    }

    public void setPaymentEmail(String paymentEmail) {
        this.paymentEmail = paymentEmail;
    }
}
