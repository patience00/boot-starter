package com.linchtech.boot.starter.utils;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.linchtech.boot.starter.common.dingding.DingdingMessageMarkdownDTO;
import com.linchtech.boot.starter.common.dingding.DingdingMessageTextDTO;
import com.linchtech.boot.starter.common.dingding.MessageAtDTO;
import com.linchtech.boot.starter.config.DingTalkConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;

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

    public void sendText(String content) {
        DingdingMessageTextDTO.MessageTextDTO textDTO = DingdingMessageTextDTO.MessageTextDTO.builder()
                .content(content).build();
        DingdingMessageTextDTO messageTextDTO = DingdingMessageTextDTO.builder()
                .msgtype("text")
                .text(textDTO)
                .build();
        if (CollectionUtils.isEmpty(dingTalkConfig.getAtMobiles())) {
            messageTextDTO.setAt(MessageAtDTO.builder().isAtAll(true).build());
        } else {
            messageTextDTO.setAt(MessageAtDTO.builder().atMobiles(dingTalkConfig.getAtMobiles()).build());
        }
        send(messageTextDTO);
    }

    public void sendLink(String content, String title, String picUrl, String messageUrl) {

    }

    /**
     * 发送markDown消息
     *
     * @param content    内容
     * @param title
     * @param titleLevel
     */
    public void sendMarkDown(String content,
                             String title,
                             Integer titleLevel) {
        StringBuilder textBuilder = new StringBuilder();
        // 多少级标题,就有几个#
        for (int i = 0; i < titleLevel; i++) {
            textBuilder.append("#");
        }
        textBuilder.append(" ");
        textBuilder.append(title);
        textBuilder.append(" @");
        if (!CollectionUtils.isEmpty(dingTalkConfig.getAtMobiles())) {
            for (String at : dingTalkConfig.getAtMobiles()) {
                textBuilder.append(at + "\n");
            }
        }
        textBuilder.append("> ");
        textBuilder.append(content);
        textBuilder.append("\n");

        DingdingMessageMarkdownDTO.MessageMarkdownDTO markdownDTO =
                DingdingMessageMarkdownDTO.MessageMarkdownDTO.builder()
                        .title(title)
                        .text(textBuilder.toString())
                        .build();
        DingdingMessageMarkdownDTO messageMarkdownDTO = DingdingMessageMarkdownDTO.builder()
                .markdown(markdownDTO)
                .msgtype("markdown")
                .build();
        if (CollectionUtils.isEmpty(dingTalkConfig.getAtMobiles())) {
            messageMarkdownDTO.setAt(MessageAtDTO.builder().isAtAll(true).build());
        } else {
            messageMarkdownDTO.setAt(MessageAtDTO.builder().atMobiles(dingTalkConfig.getAtMobiles()).build());
        }
        send(messageMarkdownDTO);
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
        sendMarkDown(stringBuilder.toString(), "error", 3);
    }

    public void send(Object params) {
        String result = "";
        long timeMillis = System.currentTimeMillis();
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://oapi.dingtalk" +
                    ".com/robot/send?access_token=" + dingTalkConfig.getToken() + "&timestamp=" + timeMillis + "&sign" +
                    "=" + sign(timeMillis));

            String requestParam = JSON.toJSONString(params);
            StringEntity requestEntity = new StringEntity(requestParam, "utf-8");
            httpPost.setEntity(requestEntity);
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, "utf-8");
            }
        } catch (Exception e) {
            log.error("--->{}", e.getMessage());
        }
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
