package org.jasonsui.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.ds.simple.SimpleDataSource;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public interface ServiceHelper {
    default String getDBFile() {
        String homePath = System.getProperty("user.home");
        String path =  homePath + File.separator + "codeGenerated" + File.separator + "c-g-pro.db";
        if(!FileUtil.exist(path)){
            FileUtil.writeBytes(new byte[]{}, path);
        }
        return path;
    }


    default SimpleDataSource createDS() {
        return new SimpleDataSource("jdbc:sqlite://" + getDBFile(),"admin","admin", "org.sqlite.JDBC");
    }


    default boolean isTableExist(Db db, String tableName) {
        try {
            List<Entity> query = db.query("SELECT count(*) FROM sqlite_master WHERE type='table' AND name=?", tableName);
            Integer count = query.get(0).getInt("count(*)");
            return count>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
