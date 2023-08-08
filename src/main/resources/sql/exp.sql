drop database experiment;

create database experiment;
use experiment;

## 学生实验用户表
create table experiment_user
(
  id                  bigint(11) auto_increment comment '主键ID'
    primary key,
  account             varchar(50)                          null,
  password            varchar(50)                          null comment '密码 密码+用户account MD5',
  status              char     default '1'                   null comment '0 禁用 1 启用 2 冻结 3 删除',
  name                varchar(50)                          null comment '用户真实姓名',
  school_name         varchar(100)                         null comment '学校名称',
  freezetime          datetime                             null comment '冻结到什么时间 定时任务在每个小时判断一次时候解冻',
  phone               varchar(20)                          null comment '手机号',
  email               varchar(50)                          null comment '邮箱',
  validate_start      datetime                             null comment '账户有效期开始时间',
  validate_end        datetime                             null comment '账户有效期结束时间',
  password_error_time datetime                             null comment '校验密码错误时间 当前时间和这个时间比，如果时间间隔小于五分钟则不更新这个时间',
  password_error_num  int        default 0                 null comment '五分钟内密码输入错误次数 如果错误达到五次则锁定用户24小时',
  user_tag            int(2)     default 0                 null comment '用户标识（0-学生 1-管理员）',
  version        varchar(2) default 'V1'              null comment '版本（V1-第一版用户  V2-后续新建）',
  memo                varchar(500)                         null comment '备注',
  create_time         datetime   default CURRENT_TIMESTAMP null comment '创建时间',
  update_time         datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
  constraint idx_ex_user_account
    unique (account) comment '登陆账号唯一索引'
)
  comment '学生实验用户表';


## 学生实验分数表
create table experiment_score
(
  id                  bigint(11) auto_increment comment '主键ID' primary key,
  account             varchar(50)                          null,
  school_name         varchar(50)                          null comment '学校名称',
  exp_start         datetime                             null comment '实验开始时间',
  exp_end           datetime                             null comment '实验结束时间',
  exp_time     int            null comment '实验时长 分钟',
  score      int            null comment '实验分数',
  is_qualified    int            null comment '是否合格 0-合格， 1-不合格',
  version        varchar(2) default 'V1'              null comment '版本（V1-第一版用户  V2-后续新建）',
  memo                varchar(500)                         null comment '备注',
  create_time         datetime   default CURRENT_TIMESTAMP null comment '创建时间',
  update_time         datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
  constraint idx_ex_user_account
    unique (account) comment '登陆账号唯一索引'
)
  comment '学生实验分数表';

### 虚拟实验分数表

CREATE TABLE `virtual_score` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_name` varchar(100) DEFAULT NULL COMMENT '用户名',
  `project_title` varchar(200) DEFAULT NULL COMMENT '实验名称',
  `child_project_title` varchar(200) DEFAULT NULL COMMENT '子实验名称',
  `status` int(1) DEFAULT NULL COMMENT '实验结果 1：完成；2：未完成',
  `score` int(11) DEFAULT NULL COMMENT '实验成绩 0 ~100，百分制',
  `start_date` varchar(13) DEFAULT NULL COMMENT '实验开始时间 13位时间戳',
  `end_date` varchar(13) DEFAULT NULL COMMENT '实验结束时间 13位时间戳',
  `time_used` int(11) DEFAULT NULL COMMENT '实验用时 分钟',
  `issuer_id` varchar(50) DEFAULT NULL COMMENT '接入平台编号 由“实验空间”分配给实验教学项目的编号',
  `attachment_id` int(11) DEFAULT NULL COMMENT '实验报告（PDF、DOC等）通过附件上传服务获取到的附件ID',
  `account` varchar(32) DEFAULT NULL COMMENT '学生账号',
  `version` varchar(2) DEFAULT 'V1' COMMENT '版本（V1-第一版用户  V2-后续新建）',
  `memo` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COMMENT='虚拟实验分数表';


## 鼻窍手术实验分数表
create table experiment_exam
(
    `id`              bigint(11) auto_increment comment '主键ID' primary key,
    `account`         varchar(50)                          null comment 'aCId 在启动VR时，由courseId参数传递过来',
    `type`            varchar(50)                          null comment '类型 由VR程序自身决定',
    `start_date`       datetime                             null comment '实验开始时间',
    `stop_date`     datetime                             null comment '实验结束时间',
    `duration`  int            null comment '实验总时长 秒',
    `hint_num`      int            null comment '使用提示总次数',
    `mistake_num`  int            null comment '操作错误总次数',
    `score`   int            null comment '由VR内部的评分机制生成的评分',
    `version`         varchar(2) default 'V1'              null comment '版本（V1-第一版用户  V2-后续新建）',
    `memo`            varchar(2000)                         null comment '实验得到的json文件',
    `create_time`     datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    `update_time`     datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    constraint idx_ex_exam_acid unique (account) comment '考生唯一索引'
) comment '鼻窍手术实验分数表';
