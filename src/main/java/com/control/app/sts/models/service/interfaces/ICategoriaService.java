package com.control.app.sts.models.service.interfaces;

import com.control.app.sts.models.entity.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoriaService {
    public List<Categoria> findAll();

    public Page<Categoria> findAll(Pageable pageable);

    public void save(Categoria categoria);

    public Categoria findOne(Long id);

    public void delete(Long id);
}
