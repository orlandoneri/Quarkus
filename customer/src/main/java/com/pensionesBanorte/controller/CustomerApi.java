package com.pensionesBanorte.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pensionesBanorte.entites.Customer;
import com.pensionesBanorte.entites.Product;
import com.pensionesBanorte.repositories.CustomerRepository;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import lombok.extern.slf4j.Slf4j;


import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class CustomerApi {

    @Inject
    CustomerRepository customerRepository;

    @Inject
    Vertx vertx;
    private WebClient webClient;

    @PostConstruct
    void initialize(){

        this.webClient = WebClient.create(vertx,
        new WebClientOptions().setDefaultHost("localhost")
                .setDefaultPort(8081).setSsl(false).setTrustAll(true));
    }


    @POST
    @Blocking
    public Response add(Customer c){
        c.getProducts().forEach(p-> p.setCustomer(c));
        customerRepository.createdCustomer(c);
        return  Response.ok().build();
    }

    @PUT
    @Blocking
    public  Response update(Customer c){
        Customer customer = customerRepository.findCustomer(c.getId());
        customer.setCode(c.getCode());
        customer.setAccountNumber(c.getAccountNumber());
        customer.setSurName(c.getSurName());
        customer.setPhone(c.getPhone());
        customer.setAddress(c.getAddress());
        customer.setProducts(c.getProducts());
        customerRepository.update(customer);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Blocking
    public Response delete(@PathParam("id") Long id){
        Customer customer = customerRepository.findCustomer(id);
        customerRepository.delete(customer);
        return Response.ok().build();
    }

    @GET
    @Blocking
    public List<Customer> list(){ return customerRepository.listCustomers();}

    @GET
    @Path("/{id}")
    @Blocking
    public  Customer getById(@PathParam("id") Long id) {

        return  customerRepository.findCustomer(id);
    }

    @GET
    @Path("/{id}/product")
    @Blocking
    public Uni<Customer> getByIdProduct(@PathParam("id") Long id) {
       return Uni.combine().all().unis(getCustomerReactive(id), getAllProducts())
                .combinedWith((v1,v2) -> {
                    v1.getProducts().forEach(product ->{
                        v2.forEach(p ->{
                            if(product.getProduct().equals(p.getId())){
                                product.setName(p.getName());
                                product.setDescription(p.getDescription());
                            }
                        });
                    });
                    return v1;
                });

    }


    private Uni<Customer> getCustomerReactive(long id){

        Customer customer = customerRepository.findCustomer(id);

        Uni<Customer> item = Uni.createFrom().item(customer);

        return item;
    }

    private Uni<List<Product>> getAllProducts(){
       return webClient.get(8081,"localhost","/product").send()
                .onFailure().invoke(res -> log.error("Error recuperando productos", res))
                .onItem().transform(res -> {
                    List<Product> lista = new ArrayList<>();
                    JsonArray objects = res.bodyAsJsonArray();
                    objects.forEach( p ->{
                        log.info("See Objects: " + objects);
                        ObjectMapper objectMapper = new ObjectMapper();
                        //pasa JSON string and the POJO class
                        Product product = null;
                        try {
                            product = objectMapper.readValue(p.toString(),Product.class);
                        } catch (JsonProcessingException e){
                            e.printStackTrace();
                        }
                        lista.add(product);
                    });
                    return lista;
        });
    }




}
