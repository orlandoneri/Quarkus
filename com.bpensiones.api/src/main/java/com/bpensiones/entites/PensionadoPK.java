package com.bpensiones.entites;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
@Data


public class PensionadoPK implements Serializable {

    private String idCliente;


    public PensionadoPK(){}

    public PensionadoPK(String idCliente){

        this.idCliente = idCliente;

    }


}
