package com.control.app.sts.models.service.interfaces;

import com.control.app.sts.models.entity.Rol;

import java.util.List;

public interface IRolService {

    public List<Rol> findAll();

    public void save(Rol rol);

    public Rol findOne(Long id);

    public void delete(Long id);

    public Rol findByDescripcion(String descripcion);
}
