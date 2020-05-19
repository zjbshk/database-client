package org.jasonsui.service.impl;

import org.jasonsui.commons.Constants;
import org.jasonsui.modal.DBConfigModal;
import org.jasonsui.service.DbService;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;

@Service(Constants.MYSQL)
public class MySqlDbServiceImpl<MySqlDBConfigModal extends DBConfigModal> implements DbService<MySqlDBConfigModal> {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    @Override
    public Connection getConn(MySqlDBConfigModal dbConfigModal) {
        try {
            Class.forName(JDBC_DRIVER);
            return DriverManager.getConnection(dbConfigModal.getUrl(), dbConfigModal.getUser(),
                    dbConfigModal.getPassword());
        } catch (Exception e) {
            throw new RuntimeException("连接失败");
        }
    }
}
