package com.control.app.sts.models.service.interfaces;

import com.control.app.sts.models.entity.SubCategoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISubCategoriaService {
    public List<SubCategoria> findAll();

    public Page<SubCategoria> findAll(Pageable pageable);

    public void save(SubCategoria subcategoria);

    public SubCategoria findOne(Long id);

    public void delete(Long id);

    public List<SubCategoria> findByCategoria(Long id);
}
