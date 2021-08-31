package com.devappron.backendjava.data.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity(name = "equipos")
public class EquipoEntity implements Serializable {

    private static final long serialVersionUID =1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 20)
    private String nombre;

    @OneToMany(cascade = CascadeType.ALL, mappedBy ="equipoEntityLocal")
    private List<PartidoEntity> partidoEntityLocalList= new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy ="equipoEntityVisitante")
    private List<PartidoEntity> partidoEntityVisitanteList= new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<PartidoEntity> getPartidoEntityLocalList() {
        return partidoEntityLocalList;
    }

    public void setPartidoEntityLocalList(List<PartidoEntity> partidoEntityLocalList) {
        this.partidoEntityLocalList = partidoEntityLocalList;
    }

    public List<PartidoEntity> getPartidoEntityVisitanteList() {
        return partidoEntityVisitanteList;
    }

    public void setPartidoEntityVisitanteList(List<PartidoEntity> partidoEntityVisitanteList) {
        this.partidoEntityVisitanteList = partidoEntityVisitanteList;
    }

}
