package com.bpensiones.services;

import com.bpensiones.entites.PensionadoEntity;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.core.Response;

public interface PensionadoServices {

     Uni<Response> create (PensionadoEntity p);
     Uni<Response> update(String idCliente, PensionadoEntity c);
}
