package com.webapp08.pujahoy.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Usuario{
    
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String nombre;
    private int reputacion;
    private String tipo;

    @OneToMany(mappedBy="vendedor")
    private List<Producto> productos;

    protected Usuario(){

    }

    public Usuario(String id, String nombre, int reputacion, String tipo){
        this.id = id;
        this.nombre = nombre;
        this.reputacion = reputacion;
        this.tipo = tipo;
        List<Producto> productos = new ArrayList<Producto>();
        this.productos = productos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getReputacion() {
        return reputacion;
    }

    public void setReputacion(int reputacion) {
        this.reputacion = reputacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public void addProducto(Producto producto){
        this.productos.add(producto);
    }
    
}
