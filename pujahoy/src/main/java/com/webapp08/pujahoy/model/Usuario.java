package com.webapp08.pujahoy.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Usuario{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;
    private int reputacion;
    private String tipo;

    @OneToMany(mappedBy="vendedor")
    private List<Producto> productos;
    private String pass;
    private Boolean estatus;
    private ArrayList<String> roles;

    protected Usuario(){

    }

    public Usuario(String nombre, String nombreVisible, int reputacion, String descripcion, String contacto, String pass, Boolean estatus, ArrayList<String> roles){
        this.nombre = nombre;
        this.reputacion = reputacion;
        List<Producto> productos = new ArrayList<Producto>();
        this.productos = productos;
        this.pass = pass;
        this.estatus = estatus;
        this.roles = new ArrayList<String>(roles);    
    }

    public Long getId() {
        return this.id;
    }

    public String getPass(){
        return this.pass;
    }

    public void setPass(String pass){
        this.pass = pass;
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

    public ArrayList<String> getRoles(){
        return this.roles;
    }
    
}
