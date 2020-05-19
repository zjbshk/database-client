package org.jasonsui.modal;

import lombok.Data;
import lombok.experimental.Accessors;
import org.jasonsui.commons.AppEnum;
import org.jasonsui.commons.group.Group;

import javax.validation.constraints.NotEmpty;

@Accessors(chain = true)
@Data
public class DBConfigModal {

    public DBConfigModal(AppEnum.TypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public DBConfigModal() {
    }

    @NotEmpty(message = "name is not empty", groups = Group.Name.class)
    private String name;
    @NotEmpty(message = "host is not empty", groups = Group.Host.class)
    private String host;
    @NotEmpty(message = "port is not empty", groups = Group.Port.class)
    private String port;
    @NotEmpty(message = "user is not empty", groups = Group.User.class)
    private String user;
    @NotEmpty(message = "password is not empty", groups = Group.Password.class)
    private String password;
    @NotEmpty(message = "database is not empty", groups = Group.Database.class)
    private String database;
    @NotEmpty(message = "url is not empty", groups = Group.Url.class)
    private String url;
    private AppEnum.TypeEnum typeEnum;
}
