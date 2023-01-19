package com.control.app.sts.models.service.implementation;

import com.control.app.sts.models.dao.IProductoDAO;
import com.control.app.sts.models.entity.Producto;
import com.control.app.sts.models.service.interfaces.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoServiceImpl implements IProductoService {

    @Autowired
    private IProductoDAO productoDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        return (List<Producto>) productoDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Producto> findAll(Pageable pageable) {
        return productoDAO.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Producto producto) {
        productoDAO.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findOne(Long id) {
        return productoDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        productoDAO.deleteById(id);
    }

    @Override
    public List<Producto> findByNombre(String term) {
        return (List<Producto>) productoDAO.findAllByNombreContainingIgnoreCase(term);
    }

    @Override
    public List<Producto> findByProveedor(Long id,String term) {
            return (List<Producto>) productoDAO.findAllByProveedor_IdAndNombreContainingIgnoreCase(id,term);
    }

    @Override
    public List<Producto> findBySubCategoria(Long id) {
        return (List<Producto>) productoDAO.findProductoBySubcategoria_Id(id);
    }

}
