package org.jasonsui.service;

import org.jasonsui.modal.DBConfigModal;

import java.sql.Connection;

public interface DbService<T extends DBConfigModal> {
    Connection getConn(T dbConfigModal);
}
