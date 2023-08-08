package com.xy.experiment.facade.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.*;
import com.xy.experiment.facade.JetcacheVerify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class JetcacheVerifyImpl implements JetcacheVerify {

    private final static Logger logger = LoggerFactory.getLogger(JetcacheVerifyImpl.class);
    @CreateCache(name = "coupon_service:")
    protected Cache<String, Long> cache;

    @Override
    @Cached(name = "coupon_service:",  key = "#token", expire = 600, cacheType = CacheType.REMOTE)
    public String jetcacheVerify(String token) {
        logger.info("-------jetcacheVerify-------->:{}", token);
        return System.currentTimeMillis() + token;
    }
    @Override
    @CacheUpdate(name = "experiment:",  key = "'jetcache'+#token", value = "#token")
    public String updateJetcacheVerify(String token) {
        logger.info("------updateJetcacheVerify--------->:{}", token);
        return System.currentTimeMillis() + token;
    }
    @Override
    @CacheInvalidate(name = "experiment:",  key = "'jetcache'+#token")
    public String invalidateJetcacheVerify(String token) {
        logger.info("------invalidateJetcacheVerify--------->:{}", token);
        return System.currentTimeMillis() + token;
    }

    @Override
    public String tryLock(String token) {
        boolean b = cache.tryLockAndRun("tryLockKey", 10, TimeUnit.SECONDS, () -> {
            try {
                logger.info("----------tryLock----START------>:{}", token);
                Object tryLockKey = cache.get("tryLockKey");
                logger.info("first get tryLockKey:{}", tryLockKey);
                Thread.sleep(30 * 1000);
                tryLockKey = cache.get("tryLockKey");
                logger.info("again get tryLockKey:{}", tryLockKey);
                logger.info("----------tryLock-----END----->:{}", token);
            } catch (InterruptedException e) {
                logger.error("----------tryLock----error------>:{}", e);
            }
        });
        if(b){
            logger.info("-------OK-------");
        } else {
            logger.info("-------ERROR-------");
        }

        return "ok";
    }


}
