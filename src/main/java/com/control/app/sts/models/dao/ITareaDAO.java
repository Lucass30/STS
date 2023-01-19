package com.control.app.sts.models.dao;

import com.control.app.sts.models.entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITareaDAO extends JpaRepository<Tarea,Long> {

    //public List<Tarea> findTareaByUsuario_Id(Long Id);
}
