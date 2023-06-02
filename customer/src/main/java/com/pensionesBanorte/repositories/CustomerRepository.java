package com.pensionesBanorte.repositories;


import com.pensionesBanorte.entites.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CustomerRepository {

    @Inject
    EntityManager em;

    @Transactional
    public void createdCustomer(Customer c){
        em.persist(c);
    }

    @Transactional
    public Customer findCustomer(Long id){
        Customer customer = em.find(Customer.class, id);
        return customer;
    }

    @Transactional
    public List<Customer> listCustomers(){
        List<Customer> customers = em.createQuery("select p from Customer p").getResultList();
        return customers;
    }

    @Transactional
    public Customer update(Customer c){
        Customer customer = em.merge(c);

        return  customer;
    }

    @Transactional
    public void delete(Customer c) {em.remove(c);}
}
