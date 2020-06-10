package com.linchtech.boot.starter.utils;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.linchtech.boot.starter.config.DingTalkConfig;
import com.taobao.api.ApiException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * @author 107
 * @date 2020-06-10 10:38
 * @since 1.0.0
 **/
@Data
@Slf4j
@Component
@EnableConfigurationProperties(DingTalkConfig.class)
public class DingTalkMessage {

    private DingTalkConfig dingTalkConfig;

    public DingTalkMessage(DingTalkConfig dingTalkConfig) {
        this.dingTalkConfig = dingTalkConfig;
    }

    public OapiRobotSendResponse sendText(String content) {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content);
        request.setText(text);
        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        // isAtAll类型如果不为Boolean，请升级至最新SDK
        if (CollectionUtils.isEmpty(dingTalkConfig.getAtMobiles())) {
            at.setIsAtAll(true);
        } else {
            at.setAtMobiles(dingTalkConfig.getAtMobiles());
        }
        request.setAt(at);
        try {
            return client().execute(request);
        } catch (ApiException e) {
            log.error("dingding send msg fail:{}", e.getErrMsg());
            e.printStackTrace();
        }
        return null;
    }

    public void sendLink(String content, String title, String picUrl, String messageUrl) {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("link");
        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
        link.setMessageUrl(messageUrl);
        link.setPicUrl(picUrl);
        link.setTitle(title);
        link.setText(content);
        request.setLink(link);
        try {
            OapiRobotSendResponse response = client().execute(request);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送markDown消息
     *
     * @param content    内容
     * @param title
     * @param titleLevel
     * @param atMobiles
     */
    public void sendMarkDown(String content,
                             String title,
                             Integer titleLevel,
                             List<String> atMobiles) {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        // isAtAll类型如果不为Boolean，请升级至最新SDK
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(title);
        StringBuilder stringBuilder = new StringBuilder();
        // 多少级标题,就有几个#
        for (int i = 0; i < titleLevel; i++) {
            stringBuilder.append("#");
        }
        stringBuilder.append(" ");
        stringBuilder.append(title);
        stringBuilder.append(" @");
        for (String at : atMobiles) {
            stringBuilder.append(at + "\n");
        }
        stringBuilder.append("> ");
        stringBuilder.append(content);
        stringBuilder.append("\n");

        // TODO
        request.setMarkdown(markdown);
        try {
            OapiRobotSendResponse response = client().execute(request);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public void sendErrorMsg(Exception e, String userId, String requestUri) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(e.toString() + "\n");
        for (StackTraceElement stackTraceElement : stackTrace) {
            String trace = stackTraceElement.toString();
            if (trace.contains("com.linchtech")) {
                stringBuilder.append("**");
                stringBuilder.append(stackTraceElement.toString());
                stringBuilder.append("**");
            }
            stringBuilder.append("\n");
        }
        sendMarkDown(stringBuilder.toString(), "error", 3, Arrays.asList("13890083528"));
    }

    private DingTalkClient client() {
        long timeMillis = System.currentTimeMillis();
        return new DefaultDingTalkClient("https://oapi.dingtalk" +
                ".com/robot/send?access_token=" + dingTalkConfig.getToken() + "&timestamp=" + timeMillis + "&sign" +
                "=" + sign(timeMillis));
    }

    private String sign(Long timestamp) {
        try {
            String stringToSign = timestamp + "\n" + dingTalkConfig.getSecret();
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(dingTalkConfig.getSecret().getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            return URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        } catch (Exception e) {
            log.error("dingding encode for sign error:{}", e.getMessage());
        }
        return null;
    }


}
