package de.fisp.anwesenheit.core.config;

import de.fisp.anwesenheit.core.dao.ParameterDao;
import de.fisp.anwesenheit.core.service.ParameterService;
import de.fisp.anwesenheit.core.service.impl.ParameterServiceImpl;
import net.sf.ehcache.CacheManager;

import net.sf.ehcache.Ehcache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ParameterServiceConfig {
  @Autowired
  private ParameterDao parameterDao;
  private static final Logger logger = LoggerFactory.getLogger(ParameterServiceConfig.class);

  public CacheManager getCacheManager() {
    CacheManager result = CacheManager.getInstance();
    logger.info("getCacheManager: {}", result);
    return result;
  }

  public Ehcache getParameterCache() {
    Ehcache result = getCacheManager().getCache("parameters");
    if(result == null) {
      getCacheManager().addCache("parameters");
      result = getCacheManager().getCache("parameters");
    }
    logger.info("getParameterCache: {}", result);
    return result;
  }

  @Bean
  public ParameterService getParameterService() {
    ParameterService result = new ParameterServiceImpl(parameterDao, getParameterCache());
    logger.info("getParameterService: {}", result);
    return result;
  }
}
