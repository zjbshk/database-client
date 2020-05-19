package org.jasonsui.service;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jasonsui.commons.AppEnum;
import org.jasonsui.modal.DBConfigModal;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class DBFactory {

    private final List<DbService<DBConfigModal>> dbServices;

    private final Map<AppEnum.TypeEnum, DbService<DBConfigModal>> serviceMap = Maps.newConcurrentMap();

    public DBFactory(List<DbService<DBConfigModal>> dbServices) {
        this.dbServices = dbServices;
    }

    public Connection getConn(DBConfigModal dbConfigModal) {
        if (!serviceMap.containsKey(dbConfigModal.getTypeEnum())) {
            Optional<DbService<DBConfigModal>> dbConfigModalDbService = dbServices.stream().filter(x -> {
                Service annotation = x.getClass().getAnnotation(Service.class);
                return annotation.value().equals(dbConfigModal.getTypeEnum().getCode());
            }).findFirst();
            if(!dbConfigModalDbService.isPresent()){
                log.error("DBFactory_getConn_dbConfigModal.getTypeEnum()={} 数据库相关接口好没有实现",dbConfigModal.getTypeEnum());
                throw new NoSuchElementException();
            }
            dbConfigModalDbService.ifPresent(configModalDbService -> serviceMap.put(dbConfigModal.getTypeEnum(), configModalDbService));
        }
        return serviceMap.get(dbConfigModal.getTypeEnum()).getConn(dbConfigModal);
    }
}
