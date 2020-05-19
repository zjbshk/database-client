package org.jasonsui.service.impl;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.jasonsui.modal.LiteConfigModal;
import org.jasonsui.service.ServiceHelper;
import org.jasonsui.service.SqliteService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * @author suiyantao393
 */
@Service
public class SqliteServiceImpl<T> implements SqliteService, ServiceHelper {

    private static final String TABLE_NAME = "dbconfig";

    @Override
    public void save(String dbModal) {
        Db db = Db.use(createDS());
        createTable(db);
        try {
            db.execute("insert into dbconfig(dbModal) values(?)", dbModal);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String selectById(Integer id) {
        Db db = Db.use(createDS());
        createTable(db);
        try {
            Entity entity = db.queryOne("select * from dbconfig where id=?", id);
            return entity.getStr("dbModal");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<LiteConfigModal> selectALl() {
        Db db = Db.use(createDS());
        createTable(db);
        try {
            List<Entity> list = db.query("select * from dbconfig");
            if (list.isEmpty()) {
                return null;
            }
            String s = JSON.toJSONString(list);
            return JSON.parseArray(s, LiteConfigModal.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Lists.newArrayList();
    }

    @Override
    public boolean delete(Integer id) {
        Db db = Db.use(createDS());
        createTable(db);
        try {
            int count = db.execute("delete from dbconfig where id=?", id);
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void createTable(Db db) {
        if (!isTableExist(db, TABLE_NAME)) {
            try {
                db.execute("create table dbconfig(id integer primary key,dbModal text)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
