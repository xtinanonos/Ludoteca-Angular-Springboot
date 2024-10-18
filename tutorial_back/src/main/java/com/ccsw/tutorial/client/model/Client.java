package com.ccsw.tutorial.client.model;

import jakarta.persistence.*;

/**
 * @author ccsw
 *
 */
@Entity                 // indica que implementa una entidad de BBDD, permite hacer queries
@Table(name = "client")           // indica a JPA el nombre de la tabla
public class Client {

    // Columna ID que es primary key
    @Id                                                   // indica que es PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // indica la estrategia para generar la PK
    @Column(name = "id", nullable = false)      // indica que mapea una propiedad como columna e indica su nombre
    private Long id;

    // Columna name (nombre)
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * @return id
     */
    public Long getId() {

        return this.id;
    }

    /**
     * @param id new value of {@link #getId}.
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * @return name
     */
    public String getName() {

        return this.name;
    }

    /**
     * @param name new value of {@link #getName}.
     */
    public void setName(String name) {

        this.name = name;
    }
}

