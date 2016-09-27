package ruilin.com.movieeyes.Helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import ruilin.com.movieeyes.Jni.LibJni;
import ruilin.com.movieeyes.modle.HotKey;
import ruilin.com.movieeyes.modle.MovieUrl;

import static android.media.CamcorderProfile.get;

/**
 * Created by Ruilin on 2016/9/22.
 */
public class JsoupHelper {
    public static final int RESULT_CODE_SUCCESS = 0;
    public static final int RESULT_CODE_ERROR = 1;
    public static final int RESULT_CODE_TIMEOUT = 2;
    public static final int RESULT_CODE_EMPTY = 3;
    public static final int FIRST_PAGE_NUM = 1;

    private static String sHost;
    private static String sBadiduHost;

    public static String getHost() {
        if (sHost == null) {
            sHost = LibJni.getHost();
        }
        return sHost;
    }

    public static String getBadiduHost() {
        if (sBadiduHost == null) {
            sBadiduHost = LibJni.getBaiduHost();
        }
        return sBadiduHost;
    }
/*
    <div class="search-classic" style="margin-bottom:0px;" id="1892274" typeid="-1">
        <h4 class="result4">
          <a title="<span style='color:red'>大话西游</span>三" id="607871276" data="2416252861" data1="1ntF0mEl" style="color:#3244ea" class="source-title" href="https://pan.baidu.com/share/home?uk=2416252861" target="_blank"><span class=""><span style="color:red">大话西游</span>三</span>&nbsp;&nbsp;
          </a>
        </h4>
        <div class="result">文件链接：<a title="<span style='color:red'>大话西游</span>三" class="source-title2" href="https://pan.baidu.com/share/link?uk=2416252861&amp;shareid=607871276" target="_blank"><span style="color:red">大话西游</span>三&nbsp;&nbsp;</a>| 所在位置：<span style="color:#3244ea">度盘&nbsp;&nbsp;</span>|分享者：
            <a href="https://pan.baidu.com/share/home?uk=2416252861" target="_blank">
                495211374
            </a>
            <a href="https://pan.baidu.com/share/home?uk=2416252861" target="_blank" style="color:#62636B;">
                他的贡献
            </a>
        </div>
        <div class="result">提示：<span>来自搜索引擎.</span> |分享时间：2015-09-08 15:35</div>
    </div>
*/
    public static int parseHtmlForSearch(String key, int page, ArrayList<MovieUrl> movieList) {
        if (movieList == null) {
            throw new IllegalArgumentException("arrayList can not be null!");
        }
        if (page <= JsoupHelper.FIRST_PAGE_NUM) {
            movieList.clear();
        }
        int count = 0;
        String baiduHost = getBadiduHost();
        try {
            Document doc = Jsoup.connect(getHost() + "/source/search.action").data("q", key).data("currentPage", String.valueOf(page)).get();
//            Elements links = doc.select("a[href]");
            Elements links = doc.select("div[class=search-classic]");
            //注意这里是Elements不是Element。同理getElementById返回Element，getElementsByClass返回时Elements
            for (Element link : links) {
                // 过滤链接
//                String tag = link.text().toString();
//                String url = link.attr("abs:href");
                Elements authorEle = link.select("h4[class=result4]");
                Elements urlEle = authorEle.select("a[href]");
                String url = urlEle.attr("href");
                String tag = authorEle.text();
                MovieUrl movie = new MovieUrl();
                movie.tag = tag;
                movie.url = "";
                if (url.contains(baiduHost)) {
                    movie.url = url;
                } else {
                        /* 载入详情页 */
                    Document subHtml = Jsoup.connect(getHost() + url).get();
                    Elements subLinks = subHtml.select("li[class=list-group-item]");
                    final String TAG_BAIDU_URL = "下载链接";
                    final String TAG_AUTHOR = "分享人：";
                    final String TAG_DATE = "分享日期：";
                    final String TAG_AUTHOR_URL = "分享人贡献";

                    for (Element subLink : subLinks) {
                        String text = subLink.text();
                        if (text.contains(TAG_BAIDU_URL)) {
                            movie.url = getBaiduPanUrl(subLink);
                        } else if (text.startsWith(TAG_AUTHOR)) {
                            movie.author = text.substring(TAG_AUTHOR.length());
                        } else if (text.startsWith(TAG_AUTHOR_URL)) {
                            movie.authorUrl = getBaiduPanUrl(subLink);
                        } else if (text.startsWith(TAG_DATE)) {
                            movie.date = text.substring(TAG_DATE.length());
                        }
                        movie.print();
                    }
                }
                if (movie.url != null && movie.url.startsWith("http")) {
                    movieList.add(movie);
                    count++;
                }
            }
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return RESULT_CODE_TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
            return RESULT_CODE_ERROR;
        }
        if (count == 0) {
            return RESULT_CODE_EMPTY;
        }
        return RESULT_CODE_SUCCESS;
    }

    private static String getBaiduPanUrl(Element subLink) {
        String baiduHost = getBadiduHost();
        Elements baiduLinks = subLink.getElementsByAttribute("href");
        for (Element baiduLink : baiduLinks) {
            String baiduUrl = baiduLink.attr("href");
            if (baiduUrl != null && baiduUrl.contains(baiduHost)) {
                return baiduUrl;
            }
        }
        return "";
    }

    public static int parseHtmlForHotKey(ArrayList<HotKey> keyList) {
        if (keyList == null) {
            throw new IllegalArgumentException("arrayList can not be null!");
        }
        keyList.clear();
        try {
            Document doc = Jsoup.connect(getHost()).get();
            Elements topElents = doc.select("div[class=hot]");
            if (topElents.size() > 0) {
                Elements ulEles = topElents.get(0).getElementsByTag("ul");
                if (ulEles.size() > 0) {
                    Elements liEles = ulEles.get(0).getElementsByTag("li");
                    int index = 0;
                    for (Element element : liEles) {
                        Elements eles = element.select("a[href]");
                        if (eles.size() > 0) {
                            Element ele = eles.get(0);
                            HotKey key = new HotKey();
                            key.setKey(ele.text());
                            key.setUrl(getHost() + ele.attr("href"));
                            key.setId(index);
                            keyList.add(key);
                            index++;
                        }
                    }
                }
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return RESULT_CODE_TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
            return RESULT_CODE_ERROR;
        }
        return RESULT_CODE_SUCCESS;
    }
}
