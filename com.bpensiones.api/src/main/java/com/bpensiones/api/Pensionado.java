package com.bpensiones.api;

import com.bpensiones.entites.PensionadoEntity;
import com.bpensiones.services.PensionadoServices;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestPath;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Slf4j
@Path("/pensionado")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class   Pensionado {

    @Inject
    PensionadoServices pensionadoServ;


    @GET
    public Uni<List<PensionadoEntity>>list() {
         Uni<List<PensionadoEntity>> data;
        data = PensionadoEntity.listAll();
        return data;
    }


    @GET
    @Path("/{idCliente}")
    public Uni<PensionadoEntity> pensionadoByIdCliente(@PathParam("idCliente") String idCliente) {
        return PensionadoEntity.findByIdCliente(idCliente);
    }

    @POST
    @Blocking
    public Uni<Response> create(PensionadoEntity p) {

        return pensionadoServ.create(p);
    }

    @PUT
    @Path("{idCliente}")
    @Blocking
    public Uni<Response> update(@RestPath String idCliente, PensionadoEntity c) {

        return pensionadoServ.update(idCliente, c);
    }


}