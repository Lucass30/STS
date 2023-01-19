package com.control.app.sts.models.dao;

import com.control.app.sts.models.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductoDAO extends JpaRepository<Producto,Long> {

    public List<Producto> findByNombre(String term);

    public List<Producto> findAllByNombreContainingIgnoreCase(String term);

    public List<Producto> findProductoBySubcategoria_Id(Long id);

    public List<Producto> findAllByProveedor_IdAndNombreContainingIgnoreCase(Long id, String term);



}
