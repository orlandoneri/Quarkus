package com.pensionesBanorte.entites;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Customer {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    private String code;
    private String accountNumber;
    private String name;
    private String surName;
    private String phone;
    private String address;
    @OneToMany(mappedBy = "customer", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Product> products;

}
