package com.control.app.sts.models.service.implementation;

import com.control.app.sts.models.dao.IItemUsuarioDAO;
import com.control.app.sts.models.entity.ItemUsuario;
import com.control.app.sts.models.service.interfaces.IItemUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemUsuarioServiceImpl implements IItemUsuarioService {
    @Autowired
    private IItemUsuarioDAO itemUsuarioDAO;

    @Override
    @Transactional(readOnly = true)
    public List<ItemUsuario> findAll() {
        return itemUsuarioDAO.findAll();
    }

    @Override
    public void save(ItemUsuario itemTrabajador) {
        itemUsuarioDAO.save(itemTrabajador);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemUsuario findOne(Long id) {
        return itemUsuarioDAO.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        itemUsuarioDAO.deleteById(id);
    }

}
