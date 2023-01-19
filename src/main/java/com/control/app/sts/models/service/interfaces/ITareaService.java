package com.control.app.sts.models.service.interfaces;

import com.control.app.sts.models.entity.Tarea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITareaService {

    public List<Tarea> findAll();

    public Page<Tarea> findAll(Pageable pageable);

    public void save(Tarea tarea);

    public Tarea findOne(Long id);

    public void delete(Long id);

   // public List<Tarea> findByUsuario(Long id);

}
