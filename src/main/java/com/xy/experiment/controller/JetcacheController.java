package com.xy.experiment.controller;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheGetResult;
import com.alicp.jetcache.anno.CreateCache;
import com.xy.experiment.facade.JetcacheVerify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/api/jetcache")
public class JetcacheController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(JetcacheController.class);

    @Autowired
    private JetcacheVerify jetcacheVerify;
    @CreateCache(name = "coupon_service:")
    protected Cache<String, Long> cache;

    @CreateCache(name = "coupon_service:J:")
    protected Cache<String, Long> cacheJ;


    /**
     * 缓存注解使用
     */
    @RequestMapping(value = "/jetcacheAnn", method = RequestMethod.POST)
    void jetcacheAnn(HttpServletRequest request, HttpServletResponse response) {
        logger.info("jetcache START");
        String res = jetcacheVerify.jetcacheVerify("STOCK_OF_USER_TOTAL_CACHE_KEY_owr_y4gSbexgJ4aBN2aAubYlnh3c_1009000000001202303162268486716");
        sendSuccessData(response, res);
        logger.info("jetcache END! res :{}", res);
        cacheJ.put("20230615", 1L);
    }
    /**
     * 缓存注解使用
     */
    @RequestMapping(value = "/updateJetcacheAnn", method = RequestMethod.POST)
    void updateJetcacheAnn(HttpServletRequest request, HttpServletResponse response) {
        logger.info("updateJetcacheAnn START");
        String res = jetcacheVerify.updateJetcacheVerify("JETCACHE-TOKEN");
        sendSuccessData(response, res);
        logger.info("updateJetcacheAnn END!");
    }
    /**
     * 缓存注解使用
     */
    @RequestMapping(value = "/invalidateJetcacheAnn", method = RequestMethod.POST)
    void invalidateJetcacheAnn(HttpServletRequest request, HttpServletResponse response) {
        logger.info("invalidateJetcacheAnn START");
        String res = jetcacheVerify.invalidateJetcacheVerify("JETCACHE-TOKEN");
        sendSuccessData(response, res);
        logger.info("invalidateJetcacheAnn END!");
    }
    /**
     * 缓存注解使用
     */
    @RequestMapping(value = "/cache", method = RequestMethod.POST)
    void cache(HttpServletRequest request, HttpServletResponse response) {
        logger.info("cache START");
        cache.put("JETCACHE-TOKEN", 1L);
        sendSuccessData(response, cache.get("cache"));
        logger.info("cache END!");
    }

    /**
     * 缓存注解使用
     */
    @RequestMapping(value = "/tryLock", method = RequestMethod.POST)
    void tryLock(HttpServletRequest request, HttpServletResponse response) {
        logger.info("invalidateJetcacheAnn START");
        String res = jetcacheVerify.tryLock("JETCACHE-TOKEN");
        sendSuccessData(response, res);
        logger.info("invalidateJetcacheAnn END!");
    }

    /**
     * 缓存注解使用
     */
    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    void verify(HttpServletRequest request, HttpServletResponse response) {
        CacheGetResult<Long> res = cache.GET("STOCK_OF_USER_TOTAL_CACHE_KEY_owr_y4gSbexgJ4aBN2aAubYlnh3c_1009000000001202303162268486716");
        sendSuccessData(response, res);
    }


}
