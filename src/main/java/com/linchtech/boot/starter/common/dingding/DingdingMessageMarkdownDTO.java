package com.linchtech.boot.starter.common.dingding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 标题
 * # 一级标题
 * ## 二级标题
 * ### 三级标题
 * #### 四级标题
 * ##### 五级标题
 * ###### 六级标题
 * <p>
 * 引用
 * > A man who stands for nothing will fall for anything.
 * <p>
 * 文字加粗、斜体
 * **bold**
 * *italic*
 * <p>
 * 链接
 * [this is a link](http://name.com)
 * <p>
 * 图片
 * ![](http://name.com/pic.jpg)
 * <p>
 * 无序列表
 * - item1
 * - item2
 * <p>
 * 有序列表
 * 1. item1
 * 2. item2
 *
 * @author 107
 * @date 2019-03-19 17:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DingdingMessageMarkdownDTO implements Serializable {

    private String msgtype;
    private MessageAtDTO at;
    private MessageMarkdownDTO markdown;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageMarkdownDTO implements Serializable {
        private String title;
        /**
         * 消息内容,可以包括文字图片,格式加粗等等,比如:
         * #### 杭州天气 @150XXXXXXXX \n> 9度，西北风1级，空气良89，相对温度73%\n
         * > ![screenshot](https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png)\n
         * > ###### 10点20分发布 [天气](https://www.dingalk.com) \n
         */
        private String text;
    }
}