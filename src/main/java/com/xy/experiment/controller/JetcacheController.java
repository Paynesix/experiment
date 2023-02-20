package com.xy.experiment.controller;

import com.alicp.jetcache.Cache;
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
    @CreateCache(name = "experiment--xxxx:")
    protected Cache<String, Long> cache;


    /**
     * 缓存注解使用
     */
    @RequestMapping(value = "/jetcacheAnn", method = RequestMethod.POST)
    void jetcacheAnn(HttpServletRequest request, HttpServletResponse response) {
        logger.info("jetcache START");
        String res = jetcacheVerify.jetcacheVerify("JETCACHE-TOKEN");
        sendSuccessData(response, res);
        logger.info("jetcache END!");
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
        cache.put("cache", 1L);
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

}
