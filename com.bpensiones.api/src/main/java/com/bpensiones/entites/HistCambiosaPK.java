package com.bpensiones.entites;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class HistCambiosaPK implements Serializable {

    private String usuario;

    public  HistCambiosaPK(){

    }

    public HistCambiosaPK(String usuario){
        this.usuario = usuario;

    }
}
