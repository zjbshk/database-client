package org.jasonsui.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class AppConf {

    @Data
    @Component
    @ConfigurationProperties(prefix = "app")
    public static class AppInfo {
        private Recommend recommend;
    }

    @Data
    public static class Recommend {
        private String jianshuUrl;
        private String giteeUrl;
        private String githubUrl;
    }
}
