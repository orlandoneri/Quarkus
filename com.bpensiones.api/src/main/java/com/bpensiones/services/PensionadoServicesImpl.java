package com.bpensiones.services;

import com.bpensiones.entites.HistCambiosEntity;
import com.bpensiones.entites.PensionadoEntity;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.*;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Singleton
@Slf4j
public class PensionadoServicesImpl implements PensionadoServices{

    @Override
    public Uni<Response> create(PensionadoEntity p) {
        Map<String, Object> params = new HashMap<>();

        if (p.getIdCliente() == null) {
            params.put("Mensaje", "el Id cliente es requerido!!");
           // throw new WebApplicationException("Id was invalidly set on request.", 422);
            return Uni.createFrom().item(Response.ok(params).status(422).build());
        }

        Uni<Long> exist = PensionadoEntity.findByIdClienteOrEmail(p.getIdCliente(), p.getEmail());
        long result;

        result = exist.await().indefinitely();

        if(result >= 1){
            params.put("Mensaje", "el Id cliente o correo ya se encuentra registrado!!");
            return Uni.createFrom().item(Response.ok(params).status(OK).build());
        }

        return Panache.withTransaction(p::persist)
                .replaceWith(Response.ok(p).status(CREATED)::build);
    }


    @Override
    public Uni<Response> update(String idCliente, PensionadoEntity c) {

        Map<String, Object> params = new HashMap<>();
        if (c == null ||  idCliente == null) {
            params.put("Mensaje", "el Id cliente o json se encuentran vacios");
            return Uni.createFrom().item(Response.ok(params).status(NOT_FOUND).build());
        }

        Date date = new Date(new java.util.Date().getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = simpleDateFormat.format(date);

        HistCambiosEntity hist1 = PensionadoEntity.findByIdCliente(idCliente)
                .onItem().ifNotNull().transform(entity -> {
                    log.info("update " + entity);
                    HistCambiosEntity hist = new HistCambiosEntity();
                    hist.setUsuario(entity.getIdCliente());
                    hist.setPoliza("001254");
                    hist.setRegimen("IM");
                    hist.setNucleo("01");
                    hist.setEmailAnt(entity.getEmail());
                    hist.setEmailNvo(c.getEmail());
                    hist.setCelAnt(entity.getTelefono());
                    hist.setCelNvo(c.getTelefono());
                    hist.setFecha(date);
                    hist.setComentarios("UPDATE DATOS APP CLIENTES");
                    log.info("hist " + hist);
                    return hist;
                }).await().indefinitely();

        log.info("objeto hist " + hist1);

        return Panache
                .withTransaction( () -> {
                    Uni<Response> q1 = PensionadoEntity.findByIdCliente(idCliente)
                            .onItem().ifNotNull().invoke(entity -> {
                                log.info("id " + entity.getIdCliente());
                                entity.setFechaNacimiento(((Date) c.getFechaNacimiento()));
                                entity.setTelefono(c.getTelefono());
                                entity.setEmail(c.getEmail());
                            }).onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
                            .onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build);
                     if(hist1 != null) {
                         params.put("Email", c.getEmail());
                         params.put("Telefono", c.getTelefono());
                         params.put("Fecha Nacimiento", c.getFechaNacimiento());

                         Uni<Response> q2 = hist1.persist().replaceWith(Response.ok(hist1).status(CREATED)::build)
                                 .onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build);

                         return Uni.combine().all().unis(q1, q2).asTuple().onItem().ifNotNull().transform(entity -> Response.ok(params).build())
                                 .onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build);
                     }else{
                         params.put("Mensaje", "el Id cliente no existe");
                         return Uni.createFrom().item(Response.ok(params).status(NOT_FOUND).build());
                     }

                });
    }
}
