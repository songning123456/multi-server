package com.simple.blog.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.simple.blog.entity.Blog;
import com.simple.blog.repository.BlogRepository;
import com.simple.blog.repository.BloggerRepository;
import com.simple.blog.repository.LabelConfigRepository;
import com.sn.common.util.HttpUtil;
import com.sn.common.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 爬虫文章
 *
 * @author songning
 * @date 2019/8/29
 * description
 */
@Component
@Configuration
//@EnableScheduling
@Slf4j
public class CrawlerArticle {

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private LabelConfigRepository labelConfigRepository;
    @Autowired
    private BloggerRepository bloggerRepository;


    private Map<String, String> toutiaoUrls = ImmutableMap.<String, String>builder()
            .put("热点", "https://www.toutiao.com/ch/news_hot/")
            .put("科技", "https://www.toutiao.com/ch/news_tech/")
            .put("娱乐", "https://www.toutiao.com/ch/news_entertainment/")
            .put("游戏", "https://www.toutiao.com/ch/news_game/")
            .put("体育", "https://www.toutiao.com/ch/news_sports/")
            .put("财经", "https://www.toutiao.com/ch/news_finance/")
            .put("搞笑", "https://www.toutiao.com/ch/funny/")
            .put("军事", "https://www.toutiao.com/ch/news_military/")
            .put("国际", "https://www.toutiao.com/ch/news_world/")
            .put("时尚", "https://www.toutiao.com/ch/news_fashion/")
            .put("旅游", "https://www.toutiao.com/ch/news_travel/")
            .put("探索", "https://www.toutiao.com/ch/news_discovery/")
            .put("育儿", "https://www.toutiao.com/ch/news_baby/")
            .put("养生", "https://www.toutiao.com/ch/news_regimen/")
            .put("美文", "https://www.toutiao.com/ch/news_essay/")
            .put("历史", "https://www.toutiao.com/ch/news_history/")
            .put("美食", "https://www.toutiao.com/ch/news_food/").build();

    @Scheduled(cron = "0 0/20 * * * ?")
    public void theftArticle() {
        try {
//            this.theftNetSimple();
//            this.theftPhoenix();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    /**
     * 暂时有问题...
     */
    private void theftToutiao() {
        Document html;
        List<String> authors = blogRepository.getAllAuthorNative();
        List<String> labels = labelConfigRepository.findAllLabelNameNative();
        String random = RandomUtil.getRandom(0, authors.size() - 1);
        for (Map.Entry<String, String> entry : toutiaoUrls.entrySet()) {
            log.info("~~~开始拉取今日头条 {} 新闻:~~~", entry.getKey());
            html = HttpUtil.getHtmlFromUrl(entry.getValue(), true);
            Document contentHtml;
            Blog blog;
            for (Element a : html.select("a[href~=/group/.*]:not(.comment)")) {
                String href = "https://www.toutiao.com" + a.attr("href");
                contentHtml = HttpUtil.getHtmlFromUrl(href, true);
                String articleInfoHtml = contentHtml.body().getElementsByTag("script").get(3).html();
                if (articleInfoHtml.length() > 45) {
                    JSONObject articleObject = JSON.parseObject("{" + articleInfoHtml.substring(33, articleInfoHtml.length() - 12).replace(".slice(6, -6).replace(/<br \\/>/ig, '')", "").replace(".slice(6, -6)", "").replace("\\", "\\\\").replace("\\\\\"", "\\\"") + "}");
                    String title = StringEscapeUtils.unescapeHtml4(articleObject.getJSONObject("articleInfo").getString("title")).replace("\\\\", "\\");
                    Map<String, Object> totalMap = blogRepository.countArticleByTitleNative(title);
                    if (totalMap.get("total").equals(new BigInteger("0"))) {
                        String author = authors.get(Integer.parseInt(random));
                        Integer readTimes = Integer.parseInt(RandomUtil.getRandom(1, 1000));
                        String kinds = labels.get(Integer.parseInt(RandomUtil.getRandom(0, labels.size() - 1)));
                        Date updateTime = new Date();
                        String content = StringEscapeUtils.unescapeEcmaScript(StringEscapeUtils.unescapeHtml4(articleInfoHtml.substring(articleInfoHtml.indexOf("content") + 10, articleInfoHtml.lastIndexOf(".slice(6, -6),") - 1))).replaceAll("&gt;", ">").replaceAll("&lt;", "<");
                        blog = Blog.builder().author(author).title(title).readTimes(readTimes).kinds(kinds).updateTime(updateTime).content(content).build();
                        blogRepository.save(blog);
                    }
                }
            }
        }
    }

    private void theftNetSimple() {
        Document html;
        List<Map<String, Object>> authorIds = bloggerRepository.findAllAuthorAndUserIdNative();
        List<String> labels = labelConfigRepository.findAllLabelNameNative();
        log.info("~~~开始拉取网易新闻:~~~");
        html = HttpUtil.getHtmlFromUrl("http://news.163.com/latest/", true);
        Document contentHtml;
        Blog blog;
        for (int i = 0; i < html.getElementById("instantPanel").getElementsByTag("li").size(); i++) {
            String href = html.getElementById("instantPanel").getElementsByTag("li").get(i).getElementsByTag("a").get(1).attr("href");
            String title = html.getElementById("instantPanel").getElementsByTag("li").get(i).getElementsByTag("a").get(1).html();
            Map<String, Object> totalMap = blogRepository.countArticleByTitleNative(title);
            if (totalMap.get("total").equals(new BigInteger("0"))) {
                Map<String, Object> randomMap = authorIds.get(Integer.parseInt(RandomUtil.getRandom(0, authorIds.size() - 1)));
                String author = randomMap.get("author").toString();
                String userId = randomMap.get("userId").toString();
                String readTimes = RandomUtil.getRandom(1, 1000);
                String kinds = labels.get(Integer.parseInt(RandomUtil.getRandom(0, labels.size() - 1)));
                Date updateTime = new Date();
                contentHtml = HttpUtil.getHtmlFromUrl(href, false);
                if (contentHtml.getElementById("endText") != null && contentHtml.getElementById("epContentLeft") != null && contentHtml.getElementById("epContentLeft").getElementsByTag("h1") != null) {
                    String content = contentHtml.getElementById("endText").html();
                    blog = Blog.builder().author(author).title(title).readTimes(Integer.parseInt(readTimes)).kinds(kinds).updateTime(updateTime).content(content).userId(userId).build();
                    blogRepository.save(blog);
                }
            }
        }
    }

    private void theftPhoenix() {
        Document html;
        List<Map<String, Object>> authorIds = bloggerRepository.findAllAuthorAndUserIdNative();
        List<String> labels = labelConfigRepository.findAllLabelNameNative();
        log.info("~~~开始拉取网易新闻:~~~");
        html = HttpUtil.getHtmlFromUrl("http://www.ifeng.com/", false);
        Document contentHtml;
        Blog blog;
        Elements elements = html.getElementById("newsList").getElementsByTag("li");
        for (Element element : elements) {
            String title = element.getElementsByTag("a").get(element.getElementsByTag("a").size() - 1).html();
            String href = element.getElementsByTag("a").get(element.getElementsByTag("a").size() - 1).attr("href");
            Map<String, Object> totalMap = blogRepository.countArticleByTitleNative(title);
            if (totalMap.get("total").equals(new BigInteger("0"))) {
                Map<String, Object> randomMap = authorIds.get(Integer.parseInt(RandomUtil.getRandom(0, authorIds.size() - 1)));
                String author = randomMap.get("author").toString();
                String userId = randomMap.get("userId").toString();
                String readTimes = RandomUtil.getRandom(1, 1000);
                String kinds = labels.get(Integer.parseInt(RandomUtil.getRandom(0, labels.size() - 1)));
                Date updateTime = new Date();
                contentHtml = HttpUtil.getHtmlFromUrl(href, false);
                int start = contentHtml.html().indexOf("contentList") + 22;
                int end = contentHtml.html().indexOf("currentPage") - 18;
                if (start < end) {
                    String content = contentHtml.html().substring(start, end);
                    blog = Blog.builder().author(author).title(title).readTimes(Integer.parseInt(readTimes)).kinds(kinds).updateTime(updateTime).content(content).userId(userId).build();
                    blogRepository.save(blog);
                }
            }
        }
    }
}
