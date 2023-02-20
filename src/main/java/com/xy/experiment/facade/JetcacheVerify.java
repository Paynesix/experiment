package com.xy.experiment.facade;

import org.springframework.stereotype.Service;

@Service
public interface JetcacheVerify {

    String jetcacheVerify(String token);
    String updateJetcacheVerify(String token);
    String invalidateJetcacheVerify(String token);
    String tryLock(String token);
}
