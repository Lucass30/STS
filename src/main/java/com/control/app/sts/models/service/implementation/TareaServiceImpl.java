package com.control.app.sts.models.service.implementation;

import com.control.app.sts.models.dao.ITareaDAO;
import com.control.app.sts.models.entity.OrdenCompra;
import com.control.app.sts.models.entity.Tarea;
import com.control.app.sts.models.service.interfaces.ITareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TareaServiceImpl implements ITareaService {

    @Autowired
    private ITareaDAO tareaDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Tarea> findAll(){
        return (List<Tarea>) tareaDAO.findAll();
    }

    @Override
    public Page<Tarea> findAll(Pageable pageable) {
        return tareaDAO.findAll(pageable);
    }


    @Override
    @Transactional
    public void save(Tarea tarea) {
        tareaDAO.save(tarea);
    }

    @Override
    @Transactional(readOnly = true)
    public Tarea findOne(Long id) {
        return tareaDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        tareaDAO.deleteById(id);
    }

    /*@Override
    public List<Tarea> findByUsuario(Long id) {
        return(List<Tarea>)  tareaDAO.findTareaByUsuario_Id(id);
    }*/
}
