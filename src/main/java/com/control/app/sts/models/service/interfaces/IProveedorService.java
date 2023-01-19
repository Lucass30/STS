package com.control.app.sts.models.service.interfaces;

import com.control.app.sts.models.entity.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProveedorService {

    public List<Proveedor> findAll();

    public Page<Proveedor> findAll(Pageable pageable);

    public void save (Proveedor proveedor);

    public Proveedor findOne(Long id);

    public void delete(Long id);

}
