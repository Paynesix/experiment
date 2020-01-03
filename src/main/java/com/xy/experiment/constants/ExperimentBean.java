package com.xy.experiment.constants;

import org.springframework.beans.factory.annotation.Value;

public class ExperimentBean {

    @Value("${mail.port}")
    public Integer port;
    @Value("${mail.host}")
    public String host;
    @Value("${mail.username}")
    public String username;
    @Value("${mail.password}")
    public String password;
    @Value("${mail.form}")
    public String emailForm;
    @Value("${mail.timeout}")
    public String timeOut;
    @Value("${mail.protocol}")
    public String protocol;
    @Value("${mail.auth}")
    public String auth;

    @Value("${virtual.secret}")
    public String secret;
    @Value("${virtual.aeskey}")
    public String aeskey;
    @Value("${virtual.issueId}")
    public Long issueId;
    @Value("${virtual.serverURI}")
    public String serverURI;
    @Value("${virtual.loginId}")
    public String loginId;

    @Value("${virtual.save.urlprefix}")
    public String urlprefix;

//
//    @Bean
//    public ExperimentBean getConstats(){
//        ExperimentBean constants = new ExperimentBean();
//        constants.setEmailForm(emailForm);
//        constants.setHost(host);
//        constants.setPort(port);
//        constants.setUsername(username);
//        constants.setPassword(password);
//        constants.setProtocol(protocol);
//        constants.setAuth(auth);
//        constants.setTimeOut(timeOut);
//
//        constants.setAeskey(aeskey);
//        constants.setIssueId(issueId);
//        constants.setSecret(secret);
//        constants.setServerURI(serverURI);
//        constants.setLoginId(loginId);
//
//        constants.setUrlprefix(urlprefix);
//        return constants;
//    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailForm() {
        return emailForm;
    }

    public void setEmailForm(String emailForm) {
        this.emailForm = emailForm;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAeskey() {
        return aeskey;
    }

    public void setAeskey(String aeskey) {
        this.aeskey = aeskey;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public String getServerURI() {
        return serverURI;
    }

    public void setServerURI(String serverURI) {
        this.serverURI = serverURI;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUrlprefix() {
        return urlprefix;
    }

    public void setUrlprefix(String urlprefix) {
        this.urlprefix = urlprefix;
    }
}
