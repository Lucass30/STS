package com.control.app.sts.models.service.implementation;

import com.control.app.sts.models.dao.ISubCategoriaDAO;
import com.control.app.sts.models.entity.SubCategoria;
import com.control.app.sts.models.service.interfaces.ISubCategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubCategoriaServiceImpl implements ISubCategoriaService {
    @Autowired
    private ISubCategoriaDAO subcategoriaDAO;

    @Override
    @Transactional(readOnly = true)
    public List<SubCategoria> findAll() {
        return (List<SubCategoria>) subcategoriaDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubCategoria> findAll(Pageable pageable) {
        return subcategoriaDAO.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(SubCategoria subcategoria) {
        subcategoriaDAO.save(subcategoria);
    }

    @Override
    @Transactional(readOnly = true)
    public SubCategoria findOne(Long id) {
        return subcategoriaDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        subcategoriaDAO.deleteById(id);
    }

    @Override
    public List<SubCategoria> findByCategoria(Long id) {
        return (List<SubCategoria>) subcategoriaDAO.findSubCategoriaByCategoria_Id(id);
    }

}
