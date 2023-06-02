package com.bpensiones.entites;


import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Data
@IdClass(HistCambiosaPK.class)
@Entity
@Table(name="HIST_CAMBIO_DIR")
public class HistCambiosEntity extends PanacheEntityBase {

    /*@Id
    @SequenceGenerator(
            name = "histCambioSequence",
            sequenceName = "hist_id_seq",
            allocationSize = 1,
            initialValue = 4)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "histCambioSequence")
    private String consecutivo;*/
    @Id
    private String usuario;
    private String poliza;
    private String regimen;
    private String nucleo;

    @Column(name = "email_ant")
    private String emailAnt;
    @Column(name = "email_nvo")
    private String emailNvo;
    @Column(name = "cel_ant")
    private String celAnt;
    @Column(name = "cel_nvo")
    private String celNvo;

    private Date fecha;
    private String comentarios;

    /*public  HistCambiosEntity(String usuario,String poliza){
        this.usuario = usuario;
        this.poliza = poliza;
    }*/
}
