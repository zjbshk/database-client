package org.jasonsui.modal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jasonsui.commons.AppEnum;


@EqualsAndHashCode(callSuper = true)
@Data
public class MySqlDBConfigModal extends DBConfigModal {

    public MySqlDBConfigModal(AppEnum.TypeEnum typeEnum) {
        super(typeEnum);
    }
}
