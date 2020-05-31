package org.jasonsui.service;

import org.jasonsui.modal.LiteConfigModal;

import java.util.List;

/**
 * @Author suiyantao393
 * @Description:
 * @Create: 2019-05-27 11:27
 */
public interface SqliteService {

    void save(String dbModal);

    String selectById(Integer id);

    List<LiteConfigModal> selectALl();

    boolean delete(Integer id);

}
