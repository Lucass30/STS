package com.control.app.sts.models.dao;

import com.control.app.sts.models.entity.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISubCategoriaDAO extends JpaRepository<SubCategoria, Long> {

    public List<SubCategoria> findSubCategoriaByCategoria_Id(Long id);
}
