package com.xy.experiment.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ExamVo implements Serializable {

    /** aCId 在启动VR时，由courseId参数传递过来  更新最高分 龚磊 */
    private String aCId;
    /** 类型 由VR程序自身决定 */
    private String type;
    /**由VR内部的评分机制生成的评分*/
    private Integer score;

    private List<ActionDetail> actionDetails;

    public String getaCId() {
        return aCId;
    }

    public void setaCId(String aCId) {
        this.aCId = aCId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<ActionDetail> getActionDetails() {
        return actionDetails;
    }

    public void setActionDetails(List<ActionDetail> actionDetails) {
        this.actionDetails = actionDetails;
    }

    public static class ActionDetail{

        /** 实验序号 */
        private Integer sortId;
        /** 实验名称 */
        private String sName;
        /** 实验英文名称 */
        private String sNameEn;
        /** 开始时间 */
        private String startDate;
        /** 结束时间 */
        private String stopDate;
        /**实验总时长 秒*/
        private Integer duration;
        /**使用提示总次数*/
        private Integer hintNum;
        /**操作错误总次数*/
        private Integer mistakeNum;

        public Integer getSortId() {
            return sortId;
        }

        public void setSortId(Integer sortId) {
            this.sortId = sortId;
        }

        public String getsName() {
            return sName;
        }

        public void setsName(String sName) {
            this.sName = sName;
        }

        public String getsNameEn() {
            return sNameEn;
        }

        public void setsNameEn(String sNameEn) {
            this.sNameEn = sNameEn;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getStopDate() {
            return stopDate;
        }

        public void setStopDate(String stopDate) {
            this.stopDate = stopDate;
        }

        public Integer getDuration() {
            return duration;
        }

        public void setDuration(Integer duration) {
            this.duration = duration;
        }

        public Integer getHintNum() {
            return hintNum;
        }

        public void setHintNum(Integer hintNum) {
            this.hintNum = hintNum;
        }

        public Integer getMistakeNum() {
            return mistakeNum;
        }

        public void setMistakeNum(Integer mistakeNum) {
            this.mistakeNum = mistakeNum;
        }
    }
}