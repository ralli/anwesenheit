package de.fisp.anwesenheit.core.config;

import de.fisp.anwesenheit.core.dao.ParameterDao;
import de.fisp.anwesenheit.core.service.ParameterService;
import de.fisp.anwesenheit.core.service.impl.ParameterServiceImpl;
import net.sf.ehcache.CacheManager;

import net.sf.ehcache.Ehcache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParameterServiceConfig {
  @Autowired
  private ParameterDao parameterDao;

  @Bean
  public CacheManager getCacheManager() {
     return CacheManager.getInstance();
  }

  public Ehcache getParameterCache() {
    return getCacheManager().getCache("parameters");
  }

  @Bean
  public ParameterService getParameterService() {
      return new ParameterServiceImpl(parameterDao, getParameterCache());
  }
}
