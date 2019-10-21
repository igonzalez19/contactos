package com.example.aadpractica1.contacto;

public class  Contacto{

    private long id;
    private String nombre;

    public Contacto(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;

    }

    public Contacto() {

    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Contacto setId(long id) {
        this.id = id;
        return this;
    }

    public Contacto setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    @Override
    public String toString() {
        return  id +". "+
                " nombre: " + nombre+"\n";
    }
}