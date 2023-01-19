package com.control.app.sts.models.service.interfaces;

import com.control.app.sts.models.entity.Provincia;

import java.util.List;

public interface IProvinciaService {

    public List<Provincia> findAll();

    public void save(Provincia provincia);

    public Provincia findOne(Long id);

    public void delete(Long id);

    List<Provincia> listarPorDepartamento(Long idDepartmento);
}
