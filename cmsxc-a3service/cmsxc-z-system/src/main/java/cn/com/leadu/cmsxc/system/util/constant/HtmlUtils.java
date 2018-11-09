package cn.com.leadu.cmsxc.system.util.constant;

/**
 * Created by LEO on 16/10/13.
 */
public class HtmlUtils {

    public static String produceHtml(String htmlBody){
        String html =
            "<html lang=\"zh-CN\" data-ng-app=\"app\">" +
                "<head>" +
                "<style type=\"text/css\" rel=\"stylesheet\">body { -webkit-overflow-scrolling: touch} " +
                    ".newsWrapper{padding: 10px;line-height: 28px; background: #fff;}"+
                ".newsWrapper p{font-size: 16px;}"+
                ".newsWrapper img{text-align: center}"+
                ".newsWrapper h1,.newsWrapper h2,.newsWrapper h3,.newsWrapper h4,.newsWrapper h5,.newsWrapper h6{line-height: 24px;}"+
                ".newsWrapper hr{border: 1px dashed #ccc;}"+
                    "</style>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\" />" +
                "<link rel=\"stylesheet\" href=\"http://wx.xftm.com/css/app.min.css\" type=\"text/css\" />" +
                "</head>" +
                "<body><div class=\"newsWrapper\">" +
                htmlBody +
            "</div></body></html>";
        return html;
    }
}
