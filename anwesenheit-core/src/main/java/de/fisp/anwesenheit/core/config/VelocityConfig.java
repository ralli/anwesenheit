package de.fisp.anwesenheit.core.config;

import java.util.Properties;

import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.ToolManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

@Configuration
public class VelocityConfig {

  @Bean
  public VelocityEngineFactoryBean velocityEngineFactoryBean() {
    Properties properties = new Properties();
    properties.setProperty("resource.loader", "class");
    properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
    properties.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute");
    properties.setProperty("runtime.log.logsystem.log4j.logger", "de.fisp.velocity");
    VelocityEngineFactoryBean bean = new VelocityEngineFactoryBean();
    bean.setVelocityProperties(properties);
    return bean;
  }

  @Bean
  public ToolManager toolManager() {
    ToolManager manager = new ToolManager();
    manager.configure("toolbox.xml");
    return manager;
  }

}
