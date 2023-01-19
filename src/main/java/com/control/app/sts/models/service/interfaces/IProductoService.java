package com.control.app.sts.models.service.interfaces;

import com.control.app.sts.models.entity.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductoService {
    public List<Producto> findAll();

    public Page<Producto> findAll(Pageable pageable);


    public void save(Producto producto);

    public Producto findOne(Long id);

    public void delete(Long id);

    public List<Producto> findByNombre(String term);

    public List<Producto> findByProveedor(Long  id,String term);

    public List<Producto> findBySubCategoria(Long  id);

}
