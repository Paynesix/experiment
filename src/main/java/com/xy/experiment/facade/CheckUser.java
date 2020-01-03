package com.xy.experiment.facade;

import com.xy.experiment.vo.VirtualScoreVo;
import org.springframework.stereotype.Service;

@Service
public interface CheckUser {

    String checkToken(String token);

    String loginVirtual(String username, String password);

    String uploadScore(VirtualScoreVo vo);

    String uploadFile(String filePath, String fileName);

    String checkStatus(String username);
}
