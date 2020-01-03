package com.xy.experiment.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ExContext implements Serializable {
        private static final long serialVersionUID = 2825955737966966798L;
        private Map<String, Object> attrs;
        private String msgSeq;
        private String clientIP;

        public <T> void setAttr(String key, T value) {
            if (this.attrs == null) {
                this.attrs = new HashMap();
            }

            this.attrs.put(key, value);
        }

        public <T> T getAttr(String key) {
            return this.attrs != null ? (T) this.attrs.get(key) : null;
        }

        public Map<String, Object> getAttrs() {
            return this.attrs;
        }

        public void setAttrs(Map<String, Object> attrs) {
            this.attrs = attrs;
        }

        public String getMsgSeq() {
            return this.msgSeq;
        }

        public void setMsgSeq(String msgSeq) {
            this.msgSeq = msgSeq;
        }

        public String getClientIP() {
            return this.clientIP;
        }

        public void setClientIP(String clientIP) {
            this.clientIP = clientIP;
        }

        public ExContext() {
        }

        public ExContext(String msgSeq) {
            this.msgSeq = msgSeq;
        }

        public String toString() {
            return "[" + this.msgSeq + "]";
        }
}
