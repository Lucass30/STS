package com.control.app.sts.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Email
    private String email;

    @Column
    private String password;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "usuario_rol",
            joinColumns = { @JoinColumn(name = "usuario_id") },
            inverseJoinColumns = { @JoinColumn(name = "rol_id") })
    private List<Rol> roles;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JsonBackReference
    private List<Tarea> items;


    private String estado;

    @NotNull
    private String nombres;

    @NotNull
    private String apellidos;


    private String direccion;


    private String telefono;


    private String dni;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Distrito distrito;

    private Long codPostal;

    private String foto;

    private int count;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemUsuario> itemUsuarios;

    public Usuario() {
        this.roles = new ArrayList<Rol>();
        this.estado= "Disponible";
        this.count=0;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public void addRol(Rol rol){
        int cont=0;
        for (Rol r:this.roles){
            if (r.getId()==rol.getId()){
                cont=1;
            }
        }
        if (cont==0){
            this.roles.add(rol);
        }

    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Long getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(Long codPostal) {
        this.codPostal = codPostal;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Tarea> getItems() {
        return items;
    }

    public void setItems(List<Tarea> items) {
        this.items = items;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Distrito getDistrito() {
        return distrito;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }

    public List<ItemUsuario> getItemUsuarios() {
        return itemUsuarios;
    }

    public void setItemUsuarios(List<ItemUsuario> itemUsuarios) {
        this.itemUsuarios = itemUsuarios;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int getTareas(String term){
        int count=0;
        for(Tarea c:this.items){
            if(c.getEstado().compareTo(term)==0){
                count++;
            }
        }
        return count;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    private static final long serialVersionUID = 1L;
}
