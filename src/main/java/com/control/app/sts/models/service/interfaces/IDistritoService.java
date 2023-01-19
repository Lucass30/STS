package com.control.app.sts.models.service.interfaces;

import com.control.app.sts.models.entity.Distrito;

import java.util.List;

public interface IDistritoService {

    public List<Distrito> findAll();

    public void save(Distrito distrito);

    public Distrito findOne(Long id);

    public void delete(Long id);

    List<Distrito> listarPorProvincia(Long idProvincia);
}
