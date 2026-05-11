package org.example.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @Column(name = "state", nullable = false, length = 100)
    private String state;

    @Column(name = "zip_code", nullable = false, length = 20)
    private String zipCode;

    @Column(name = "registration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Order> orders;

    public Customer() {}

    public Customer(String firstName, String lastName, String email,
                    String phoneNumber, String address, String city,
                    String state, String zipCode) {
        this.firstName = firstName; this.lastName = lastName;
        this.email = email; this.phoneNumber = phoneNumber;
        this.address = address; this.city = city;
        this.state = state; this.zipCode = zipCode;
    }

    public int getCustomerId()              { return customerId; }
    public void setCustomerId(int v)        { this.customerId = v; }
    public String getFirstName()            { return firstName; }
    public void setFirstName(String v)      { this.firstName = v; }
    public String getLastName()             { return lastName; }
    public void setLastName(String v)       { this.lastName = v; }
    public String getEmail()                { return email; }
    public void setEmail(String v)          { this.email = v; }
    public String getPhoneNumber()          { return phoneNumber; }
    public void setPhoneNumber(String v)    { this.phoneNumber = v; }
    public String getAddress()              { return address; }
    public void setAddress(String v)        { this.address = v; }
    public String getCity()                 { return city; }
    public void setCity(String v)           { this.city = v; }
    public String getState()                { return state; }
    public void setState(String v)          { this.state = v; }
    public String getZipCode()              { return zipCode; }
    public void setZipCode(String v)        { this.zipCode = v; }
    public Date getRegistrationDate()       { return registrationDate; }
    public void setRegistrationDate(Date v) { this.registrationDate = v; }
    public List<Order> getOrders()          { return orders; }
    public void setOrders(List<Order> v)    { this.orders = v; }

    public String getFullName()  { return firstName + " " + lastName; }
    public String getInitials()  {
        return (firstName != null && lastName != null)
            ? ("" + firstName.charAt(0) + lastName.charAt(0)).toUpperCase() : "??";
    }
}
