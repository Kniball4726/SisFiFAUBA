/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.io.Serializable;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author user
 */
public class EmpleadoJpaController implements Serializable {


    
    public EmpleadoJpaController(EntityManagerFactory emf){
        this.emf = emf;
    }
    
    
    public EmpleadoJpaController(){
        emf = Persistence.createEntityManagerFactory("faubaPU");
    }
    private EntityManagerFactory emf = null;
    
    
}
