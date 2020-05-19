package org.jasonsui.modal;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class LiteConfigModal {
    private Integer id;
    private String dbmodal;
}
