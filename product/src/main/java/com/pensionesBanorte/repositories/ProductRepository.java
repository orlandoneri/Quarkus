package com.pensionesBanorte.repositories;


import com.pensionesBanorte.entites.Product;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ProductRepository {
    @Inject
    EntityManager em;

    @Transactional
    public void createdProduct(Product p){
        em.persist(p);
    }

    @Transactional
    public Product findProduct(Long id){
        Product product = em.find(Product.class, id);
       return product;
    }

    @Transactional
    public List<Product> listProducts(){
      List<Product> products =  em.createQuery("select p from Product p").getResultList();

      return products;
    }

    @Transactional
    public Product update(Product p){
        Product product = em.merge(p);

        return product;
    }

    @Transactional
    public void delete(Product p){
        em.remove(p);
    }

}
