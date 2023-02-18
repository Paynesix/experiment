package com.xy.experiment.utils.shunde;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 商户订单总数查询参数
 * User: wxy
 * DateTime:2022/11/23 5:47 PM
 */
@Data
public class OrderCountReqAO {
    private static final long serialVersionUID = -6199489694322149438L;

    /**
     * 用户手机号 加密方式传输
     */
    @NotNull(message = "服务商ID不能为空")
    private Long serviceId;
    /**
     * 用户手机号 加密方式传输
     */
    @NotNull(message = "用户手机号不能为空")
    private String mobile;

    /**
     * 商户号列表，以","号分割，最多不超过100个商户
     */
    @NotBlank(message = "商户号不能为空")
    private String merchantIds;

    /**
     * 开始时间 yyyy-MM-dd HH:mm:ss
     */
    @NotNull(message = "开始时间不能为空")
    private Date startDate;

    /**
     * 结束时间 yyyy-MM-dd HH:mm:ss
     */
    @NotNull(message = "结束时间不能为空")
    private Date endDate;

}
