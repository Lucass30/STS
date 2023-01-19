package com.control.app.sts.models.service.interfaces;

import com.control.app.sts.models.entity.UnidadMedida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUnidadMedidaService {

    public List<UnidadMedida> findAll();

    public Page<UnidadMedida> findAll(Pageable pageable);

    public void save(UnidadMedida unidadmedida);

    public UnidadMedida findOne(Long id);

    public void delete(Long id);
}
