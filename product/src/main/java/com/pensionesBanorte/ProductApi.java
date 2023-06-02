package com.pensionesBanorte;

import com.pensionesBanorte.entites.Product;
import com.pensionesBanorte.repositories.ProductRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductApi {

    @Inject
    ProductRepository productRep;

    @POST
    public Response add(Product p){
        productRep.createdProduct(p);
        return Response.ok().build();
    }

    @PUT
    public  Response update(Product p){
        Product product = productRep.findProduct(p.getId());
        product.setCode(p.getCode());
        product.setName(p.getName());
        product.setDescription(p.getDescription());
        productRep.update(product);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public  Response delete(@PathParam("id") Long id){
        productRep.delete(productRep.findProduct(id));
        return Response.ok().build();
    }

    @GET
    public List<Product> list(){
        return productRep.listProducts();
    }


    @GET
    @Path("/{id}")
    public  Product listId(@PathParam("id") Long id){

        return  productRep.findProduct(id);
    }
}