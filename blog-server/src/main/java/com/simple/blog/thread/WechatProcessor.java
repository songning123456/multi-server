package com.simple.blog.thread;

import com.alibaba.fastjson.JSONObject;
import com.simple.blog.entity.WechatDialog;
import com.simple.blog.repository.BloggerRepository;
import com.simple.blog.repository.WechatDialogRepository;
import com.simple.blog.websocket.handler.WechatHandler;
import com.simple.blog.websocket.publish.WechatPublish;
import com.sn.common.util.DateUtil;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: songning
 * @date: 2020/2/19 22:52
 */
@Component
public class WechatProcessor {

    @Autowired
    private BloggerRepository bloggerRepository;
    @Autowired
    private WechatDialogRepository wechatDialogRepository;
    @Autowired
    private WechatPublish wechatPublish;

    @Async("OnlineTotalExecutor")
    public void asyncOnlineTotal(JSONObject jsonObject) {
        try {
            String userId = jsonObject.get("userId").toString();
            int online = Integer.parseInt(jsonObject.get("online").toString());
            bloggerRepository.updateByUserIdAndOnlineNative(userId, online);
            wechatPublish.publish(online);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async("DialogUpdateExecutor")
    public void asyncDialogUpdate(JSONObject jsonObject) {
        try {
            String userId = jsonObject.get("userId").toString();
            String author = jsonObject.get("author").toString();
            String headPortrait = jsonObject.get("headPortrait").toString();
            String message = jsonObject.get("message").toString();
            Date updateTime = DateUtil.strToDate(jsonObject.get("updateTime").toString(), "yyyy-MM-dd HH:mm:ss");
            WechatDialog wechatDialog = WechatDialog.builder().userId(userId).author(author).headPortrait(headPortrait).message(message).updateTime(updateTime).build();
            wechatDialogRepository.save(wechatDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
