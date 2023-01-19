package com.control.app.sts.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    private String descripcion;

    private Long stock;
    @Column(name = "precio")
    private Float precio;

    @Column(name = "preciocompra")
    private Float preciocompra;

    @Column(name = "imagen")
    private String imagen;

    @ManyToOne
    @JsonBackReference
    private SubCategoria subcategoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private UnidadMedida unidadmedida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Proveedor proveedor;

    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemUsuario> items;

    public Producto() {
        this.items = new ArrayList<ItemUsuario>();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Float getPreciocompra() {
        return preciocompra;
    }

    public void setPreciocompra(Float preciocompra) {
        this.preciocompra = preciocompra;
    }

    public SubCategoria getCategoria() {
        return subcategoria;
    }

    public void setCategoria(SubCategoria subcategoria) {
        this.subcategoria = subcategoria;
    }

    public UnidadMedida getUnidadmedida() {
        return unidadmedida;
    }

    public void setUnidadmedida(UnidadMedida unidadmedida) {
        this.unidadmedida = unidadmedida;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public SubCategoria getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(SubCategoria subcategoria) {
        this.subcategoria = subcategoria;
    }

    public List<ItemUsuario> getItems() {
        return items;
    }

    public void setItems(List<ItemUsuario> items) {
        this.items = items;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    private static final long serialVersionUID = 1L;
}
