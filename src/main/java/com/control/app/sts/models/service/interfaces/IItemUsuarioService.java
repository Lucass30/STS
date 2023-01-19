package com.control.app.sts.models.service.interfaces;

import com.control.app.sts.models.entity.ItemUsuario;

import java.util.List;

public interface IItemUsuarioService {

    public List<ItemUsuario> findAll();

    public void save (ItemUsuario itemUsuario);

    public ItemUsuario findOne(Long id);

    public void delete(Long id);

}
