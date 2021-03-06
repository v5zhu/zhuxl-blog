package com.mfx.blog.component.common;


import com.github.pagehelper.PageInfo;
import com.mfx.blog.component.constant.WebConst;
import com.mfx.blog.enums.LogActions;
import com.mfx.blog.dto.MetaDto;
import com.mfx.blog.enums.Types;
import com.mfx.blog.modal.bo.ArchiveBo;
import com.mfx.blog.modal.entity.ArticleDO;
import com.mfx.blog.service.MetaService;
import com.mfx.blog.service.SiteService;
import com.mfx.blog.utils.AbstractUUID;
import com.mfx.blog.utils.DateKit;
import com.mfx.blog.utils.TaleUtils;
import com.vdurmont.emoji.EmojiParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 主题公共函数
 * <p>
 *
 * @author 13
 * @date 2017/2/21
 */
@Component
public final class Commons {

    private static final String[] ICONS = {"bg-ico-book", "bg-ico-game", "bg-ico-note", "bg-ico-chat", "bg-ico-code",
            "bg-ico-image", "bg-ico-web", "bg-ico-link", "bg-ico-design", "bg-ico-lock"};
    public static String THEME = "themes/default";
    private static Pattern srcPattern = Pattern.compile("src\\s*=\\s*\'?\"?(.*?)(\'|\"|>|\\s+)");

    @Autowired
    private MetaService metasService;
    @Autowired
    private SiteService siteService;


    /**
     * 判断分页中是否有数据
     *
     * @param paginator
     * @return
     */
    public static boolean isEmpty(PageInfo paginator) {
        return paginator == null || (paginator.getList() == null) || (paginator.getList().size() == 0);
    }

    /**
     * 网站链接
     *
     * @return
     */
    public static String siteUrl() {
        return siteUrl("");
    }

    /**
     * 返回网站链接下的全址
     *
     * @param sub 后面追加的地址
     * @return
     */
    public static String siteUrl(String sub) {
        return siteOption("siteUrl") + sub;
    }

    /**
     * 网站标题
     *
     * @return
     */
    public static String siteTitle() {
        return siteOption("siteTitle");
    }

    /**
     * 网站配置项
     *
     * @param key
     * @return
     */
    public static String siteOption(String key) {
        return siteOption(key, "");
    }

    /**
     * 网站配置项
     *
     * @param key
     * @param defalutValue 默认值
     * @return
     */
    public static String siteOption(String key, String defalutValue) {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        String str = WebConst.initConfig.get(key);
        if (StringUtils.isNotBlank(str)) {
            return str;
        } else {
            return defalutValue;
        }
    }

    /**
     * 截取字符串
     *
     * @param str
     * @param len
     * @return
     */
    public static String substr(String str, int len) {
        if (str.length() > len) {
            return str.substring(0, len);
        }
        return str;
    }

    /**
     * 返回主题URL
     *
     * @return
     */
    public static String themeUrl() {
        return siteUrl(Commons.THEME);
    }

    /**
     * 返回主题下的文件路径
     *
     * @param sub
     * @return
     */
    public static String themeUrl(String sub) {
        return siteUrl(Commons.THEME + sub);
    }

    /**
     * 返回github头像地址
     *
     * @param email
     * @return
     */
    public static String gravatar(String email) {
        String avatarUrl = "https://github.com/identicons/";
        if (StringUtils.isBlank(email)) {
            email = "user@hanshuai.xin";
        }
        String hash = TaleUtils.md5encode(email.trim().toLowerCase());
        return avatarUrl + hash + ".png";
    }

    /**
     * 返回文章链接地址
     *
     * @param contents
     * @return
     */
    public static String permalink(ArticleDO contents) {
        return permalink(contents.getId(), contents.getPath());
    }

    /**
     * 获取随机数
     *
     * @param max
     * @param str
     * @return
     */
    public static String random(int max, String str) {
        return AbstractUUID.random(1, max) + str;
    }

    /**
     * 返回文章链接地址
     *
     * @param articleId
     * @param path
     * @return
     */
    public static String permalink(Long articleId, String path) {
        return siteUrl("/article/" + (StringUtils.isNotBlank(path) ? path : articleId.toString()));
    }

    /**
     * 格式化unix时间戳为日期
     *
     * @param unixTime
     * @return
     */
    public static String fmtdate(Integer unixTime) {
        return fmtdate(unixTime, "yyyy-MM-dd");
    }

    /**
     * 格式化date时间戳为日期
     *
     * @param date
     * @return
     */
    public static String fmtdate(Date date) {
        return fmtdate(date, "yyyy-MM-dd");
    }

    /**
     * 格式化unix时间戳为日期
     *
     * @param unixTime
     * @param patten
     * @return
     */
    public static String fmtdate(Integer unixTime, String patten) {
        if (null != unixTime && StringUtils.isNotBlank(patten)) {
            return DateKit.formatDateByUnixTime(unixTime, patten);
        }
        return "";
    }

    /**
     * 格式化unix时间戳为日期
     *
     * @param unixTime
     * @param patten
     * @return
     */
    public static String fmtdate(Long unixTime, String patten) {
        if (null != unixTime && StringUtils.isNotBlank(patten)) {
            return DateKit.formatDateByUnixTime(unixTime, patten);
        }
        return "";
    }

    /**
     * 格式化date戳为日期
     *
     * @param unixTime
     * @param patten
     * @return
     */
    public static String fmtdate(Date date, String patten) {
        if (null != date && StringUtils.isNotBlank(patten)) {
            return DateKit.formatDate(date, patten);
        }
        return "";
    }

    /**
     * 显示分类
     *
     * @param categories
     * @return
     */
    public static String showCategories(String categories) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(categories)) {
            String[] arr = categories.split(",");
            StringBuffer sbuf = new StringBuffer();
            for (String c : arr) {
                sbuf.append("<a href=\"/category/" + URLEncoder.encode(c, "UTF-8") + "\">" + c + "</a>");
            }
            return sbuf.toString();
        }
        return showCategories("默认分类");
    }

    /**
     * 显示标签
     *
     * @param tags
     * @return
     */
    public static String showTags(String tags) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(tags)) {
            String[] arr = tags.split(",");
            StringBuffer sbuf = new StringBuffer();
            for (String c : arr) {
                sbuf.append("<a href=\"/tag/" + URLEncoder.encode(c, "UTF-8") + "\" target=\"_blank\">" + c + "</a>");
            }
            return sbuf.toString();
        }
        return "";
    }

    /**
     * 截取文章摘要
     *
     * @param value 文章内容
     * @param len   要截取文字的个数
     * @return
     */
    public static String intro(String value, int len) {
        int pos = value.indexOf("<!--more-->");
        if (pos != -1) {
            String html = value.substring(0, pos);
            return TaleUtils.htmlToText(TaleUtils.mdToHtml(html));
        } else {
            String text = TaleUtils.htmlToText(TaleUtils.mdToHtml(value));
            if (text.length() > len) {
                return text.substring(0, len);
            }
            return text;
        }
    }

    /**
     * 显示文章内容，转换markdown为html
     *
     * @param value
     * @return
     */
    public static String article(String value) {
        if (StringUtils.isNotBlank(value)) {
            value = value.replace("<!--more-->", "\r\n");
            return TaleUtils.mdToHtml(value);
        }
        return "";
    }

    /**
     * 显示文章缩略图，顺序为：文章第一张图 -> 随机获取
     *
     * @return
     */
    public static String showThumb(ArticleDO contents) {
        long articleId = contents.getId();
        long size = articleId % 20;
        size = size == 0 ? 1 : size;
        return "/user/img/rand/" + size + ".jpg";
    }

    /**
     * An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!
     * <p>
     * 这种格式的字符转换为emoji表情
     *
     * @param value
     * @return
     */
    public static String emoji(String value) {
        return EmojiParser.parseToUnicode(value);
    }

    /**
     * 获取文章第一张图片
     *
     * @return
     */
    public static String showThumb(String content) {
        content = TaleUtils.mdToHtml(content);
        if (content.contains("<img")) {
            String img = "";
            String regexImg = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
            Pattern pImage = Pattern.compile(regexImg, Pattern.CASE_INSENSITIVE);
            Matcher mImage = pImage.matcher(content);
            if (mImage.find()) {
                img = img + "," + mImage.group();
                // //匹配src
                Matcher m = srcPattern.matcher(img);
                if (m.find()) {
                    return m.group(1);
                }
            }
        }
        return "";
    }

    /**
     * 显示文章图标
     *
     * @param articleId
     * @return
     */
    public static String showIcon(int articleId) {
        return ICONS[articleId % ICONS.length];
    }

    /**
     * 获取社交的链接地址
     *
     * @return
     */
    public static Map<String, String> social() {
        final String prefix = "social_";
        Map<String, String> map = new HashMap<>(16);
        map.put("weibo", WebConst.initConfig.get(prefix + "weibo"));
        map.put("zhihu", WebConst.initConfig.get(prefix + "zhihu"));
        map.put("github", WebConst.initConfig.get(prefix + "github"));
        map.put("twitter", WebConst.initConfig.get(prefix + "twitter"));
        return map;
    }

    public List<MetaDto> categories() {
        List<MetaDto> categories = metasService.getMetaList(Types.CATEGORY.getType(), null, WebConst.MAX_POSTS);
        return categories;
    }

    public List<MetaDto> tags() {
        List<MetaDto> tags = metasService.getMetaList(Types.TAG.getType(), null, WebConst.MAX_POSTS);
        return tags;
    }

    public List<ArchiveBo> archives() {
        List<ArchiveBo> archives = siteService.getArchives(null, null, null, null);
        return archives;
    }

    public static List<String> logActions() {
        LogActions[] actions = LogActions.values();
        List<String> actionList = new ArrayList<String>();
        for (LogActions action : actions) {
            actionList.add(action.getAction());
        }
        return actionList;
    }
}
