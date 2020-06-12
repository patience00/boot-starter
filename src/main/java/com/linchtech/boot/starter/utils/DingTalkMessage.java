package com.linchtech.boot.starter.utils;

import com.alibaba.fastjson.JSON;
import com.linchtech.boot.starter.common.dingding.DingdingMessageMarkdownDTO;
import com.linchtech.boot.starter.common.dingding.DingdingMessageTextDTO;
import com.linchtech.boot.starter.common.dingding.MessageAtDTO;
import com.linchtech.boot.starter.properties.SystemProperties;
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
@Slf4j
@Component
@EnableConfigurationProperties(SystemProperties.class)
public class DingTalkMessage {

    private SystemProperties systemProperties;

    public DingTalkMessage(SystemProperties systemProperties) {
        this.systemProperties = systemProperties;
    }

    public void sendText(String content) {
        DingdingMessageTextDTO.MessageTextDTO textDTO = DingdingMessageTextDTO.MessageTextDTO.builder()
                .content(content).build();
        DingdingMessageTextDTO messageTextDTO = DingdingMessageTextDTO.builder()
                .msgtype("text")
                .text(textDTO)
                .build();
        if (CollectionUtils.isEmpty(systemProperties.getDingTalk().getAtMobiles())) {
            messageTextDTO.setAt(MessageAtDTO.builder().isAtAll(true).build());
        } else {
            messageTextDTO.setAt(MessageAtDTO.builder().atMobiles(systemProperties.getDingTalk().getAtMobiles()).build());
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
        textBuilder.append("\n");
        if (!CollectionUtils.isEmpty(systemProperties.getDingTalk().getAtMobiles())) {
            for (String at : systemProperties.getDingTalk().getAtMobiles()) {
                textBuilder.append(" @");
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
        if (CollectionUtils.isEmpty(systemProperties.getDingTalk().getAtMobiles())) {
            MessageAtDTO atDTO = MessageAtDTO.builder()
                    .isAtAll(true)
                    .atMobiles(systemProperties.getDingTalk().getAtMobiles())
                    .build();
            messageMarkdownDTO.setAt(atDTO);
        } else {
            MessageAtDTO atDTO = MessageAtDTO.builder()
                    .isAtAll(false)
                    .atMobiles(systemProperties.getDingTalk().getAtMobiles())
                    .build();
            messageMarkdownDTO.setAt(atDTO);
        }
        send(messageMarkdownDTO);
    }

    public void sendErrorMsg(Exception e, String userId, String requestUri) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("userId:");
        stringBuilder.append(userId);
        stringBuilder.append("\n");
        stringBuilder.append("requestUri:");
        stringBuilder.append(requestUri);
        stringBuilder.append("\n");
        stringBuilder.append(e.toString());
        stringBuilder.append("\n");
        for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement stackTraceElement = stackTrace[i];
            String trace = stackTraceElement.toString();
            if (trace.contains(systemProperties.getDingTalk().getBoldPackage())) {
                stringBuilder.append("**");
                stringBuilder.append(stackTraceElement.toString());
                stringBuilder.append("**");
            }
            stringBuilder.append(stackTraceElement.toString());
            stringBuilder.append("\n");
            if (i > 10) {
                break;
            }
        }
        sendMarkDown(stringBuilder.toString(), "生产异常", 2);
    }

    public void send(Object params) {
        String result ;
        long timeMillis = System.currentTimeMillis();
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://oapi.dingtalk" +
                    ".com/robot/send?access_token=" + systemProperties.getDingTalk().getToken() + "&timestamp=" + timeMillis + "&sign" +
                    "=" + sign(timeMillis));

            String requestParam = JSON.toJSONString(params);
            StringEntity requestEntity = new StringEntity(requestParam, "utf-8");
            httpPost.setEntity(requestEntity);
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, "utf-8");
                log.info("send dingding mgs result:{}", result);
            }
        } catch (Exception e) {
            log.error("--->{}", e.getMessage());
        }
    }

    private String sign(Long timestamp) {
        try {
            String stringToSign = timestamp + "\n" + systemProperties.getDingTalk().getSecret();
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(systemProperties.getDingTalk().getSecret().getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            return URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        } catch (Exception e) {
            log.error("dingding encode for sign error:{}", e.getMessage());
        }
        return null;
    }


}
