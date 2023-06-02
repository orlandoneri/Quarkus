package com.bpensiones.entites;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import lombok.Data;


import javax.persistence.*;
import java.sql.Date;

@Data
@Entity
@IdClass(PensionadoPK.class)
@Table(name="usu_app_clientes")
@NamedQueries({
        @NamedQuery(name = "PensionadoEntity.getByIdCliente", query = "from PensionadoEntity where idCliente= ?1"),
        @NamedQuery(name = "PensionadoEntity.getByIdClienteOrEmail", query = "select count(*) from PensionadoEntity p where p.idCliente = :idCliente or p.email = :email"),
       /* @NamedQuery(name = "PensionadoApp.updateByIdCliente", query = "update PensionadoApp p set p.fecha_nacimiento = :fechaNacimiento" +
                ",p.telefono = :telefono, p.email =:email where p.idCliente = :idCliente"),*/
})
public class PensionadoEntity extends PanacheEntityBase {

    @Id
    @Column(name = "no_cliente")
    private String idCliente;

    private String email;
    @Column(name = "fecha_nacimiento")
    private java.sql.Date fechaNacimiento;
    private String telefono;
    @Column(name = "fecha_reg")
    private java.sql.Date  fechaReg;

    public static Uni<PensionadoEntity> findByIdCliente(String idCliente){
        return find("#PensionadoEntity.getByIdCliente", idCliente).firstResult();
    }

    public static Uni<Long> findByIdClienteOrEmail(String idCliente, String email){
        return count("#PensionadoEntity.getByIdClienteOrEmail", Parameters.with("idCliente", idCliente).and("email", email).map());
    }

   /* public static Uni<Integer> updateById(String fechaNacimiento, String telefono, String email, String idCliente) {
        return update("#PensionadoApp.updateByIdCliente", Parameters.with("fechaNacimiento", fechaNacimiento).and("telefono", telefono).and("email", email).and("idCliente", idCliente));
    }*/


}
