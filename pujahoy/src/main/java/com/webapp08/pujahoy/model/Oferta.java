package com.webapp08.pujahoy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import java.util.Date;

//import org.springframework.stereotype.Indexed;

@Entity
public class Oferta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    private Usuario usuario;

    @ManyToOne
    private Producto producto;

    private double coste;
    private Date hora;

    protected Oferta(){

    }

    public Oferta(Usuario usuario, Producto producto, double coste, Date hora){
        this.usuario = usuario;
        this.producto = producto;
        this.coste = coste;
        this.hora = hora;
    }

    public long getId(){
        return id;
    }

    public double getCoste(){
        return coste;
    }

    public Date getHora(){
        return hora;
    }

    public void setCoste(double coste){
        this.coste = coste;
    }

    public void setHora(Date hora){
        this.hora = hora;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
