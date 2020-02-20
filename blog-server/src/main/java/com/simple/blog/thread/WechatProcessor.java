package com.simple.blog.thread;

import com.alibaba.fastjson.JSONObject;
import com.simple.blog.entity.WechatDialog;
import com.simple.blog.repository.BloggerRepository;
import com.simple.blog.repository.WechatDialogRepository;
import com.sn.common.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Async;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

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

    @Async("WechatExecutor")
    public Future<List<Map<String, Object>>> asyncOnlineWechat(JSONObject jsonObject) {
        String userId = jsonObject.get("userId").toString();
        int online = Integer.parseInt(jsonObject.get("online").toString());
        bloggerRepository.updateByUserIdAndOnlineNative(userId, online);
        List<Map<String, Object>> list = bloggerRepository.findByOnlineNative(online);
        return new AsyncResult<>(list);
    }

    @Async("WechatDialogExecutor")
    public void asyncUpdateDialog(JSONObject jsonObject) {
        String userId = jsonObject.get("userId").toString();
        String author = jsonObject.get("author").toString();
        String headPortrait = jsonObject.get("headPortrait").toString();
        String message = jsonObject.get("message").toString();
        Date updateTime = DateUtil.strToDate(jsonObject.get("updateTime").toString(), "yyyy-MM-dd HH:mm:ss");
        WechatDialog wechatDialog = WechatDialog.builder().userId(userId).author(author).headPortrait(headPortrait).message(message).updateTime(updateTime).build();
        wechatDialogRepository.save(wechatDialog);
    }
}
