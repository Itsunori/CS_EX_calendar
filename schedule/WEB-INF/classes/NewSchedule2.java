import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Calendar;

public class NewSchedule2 extends HttpServlet{

    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException{

        res.setContentType("text/html;charset=Shift_Jis");
        PrintWriter out = res.getWriter();

        int year;
        int month;
        int day;

        String param = req.getParameter("YEAR");
        if (param == null || param.length() == 0){
            year = -999;
        }else{
            try{
                year = Integer.parseInt(param);
            }catch (NumberFormatException e){
                year = -999;
            }
        }

        param = req.getParameter("MONTH");
        if (param == null || param.length() == 0){
            month = -999;
        }else{
            try{
                month = Integer.parseInt(param);
            }catch (NumberFormatException e){
                month = -999;
            }
        }

        param = req.getParameter("DAY");
        if (param == null || param.length() == 0){
            day = -999;
        }else{
            try{
                day = Integer.parseInt(param);
            }catch (NumberFormatException e){
                day = -999;
            }
        }

        /* パラメータが指定されていない場合は本日の日付を設定 */
        if (year == -999 || month == -999 || day == -999){
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DATE);
        }

        StringBuffer sb = new StringBuffer();

        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0.1//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");

        sb.append("<html lang=\"ja\">");
        sb.append("<head>");
        sb.append("<meta http-equiv=\"Content-Type\" Content=\"text/html;charset=Shift_JIS\">");

        sb.append("<title>スケジュール登録</title>");

        sb.append("<style>");
        sb.append("table.sche{border:1px solid #a9a9a9;padding:0px;margin:0px;border-collapse:collapse;}");
        sb.append("td{vertical-align:top;margin:0px;padding:2px;font-size:0.75em;height:20px;}");
        sb.append("td.top{border-bottom:1px solid #a9a9a9;text-align:center;}");
        sb.append("td.time{background-color:#f0f8ff;text-align:right;border-right:1px double #a9a9a9;padding-right:5px;}");
        sb.append("td.timeb{background-color:#f0f8ff;border-bottom:1px solid #a9a9a9;border-right:1px double #a9a9a9;}");
        sb.append("td.contents{background-color:#ffffff;border-bottom:1px dotted #a9a9a9;}");
        sb.append("td.contentsb{background-color:#ffffff;border-bottom:1px solid #a9a9a9;}");
        sb.append("td.ex{background-color:#ffebcd;border:1px solid #8b0000;}");
        sb.append("img{border:0px;}");
        sb.append("p{font-size:0.75em;}");

        sb.append("#contents{margin:0;padding:0;width:710px;}");
        sb.append("#left{margin:0;padding:0;float:left;width:400px;}");
        sb.append("#right{margin:0;padding:0;float:right;width:300px;background-color:#ffffff;}");
        sb.append("#contents:after{content:\".\";display:block;height:0;clear:both;visibility:hidden;}");
        sb.append("</style>");

        sb.append("</head>");
        sb.append("<body>");

        sb.append("<p>");
        sb.append("スケジュール登録  ");
        sb.append("[<a href=\"/schedule/MonthView");
        sb.append("?YEAR=");
        sb.append(year);
        sb.append("&MONTH=");
        sb.append(month);
        sb.append("\">カレンダーへ戻る</a>]");
        sb.append("</p>");

        sb.append("<div id=\"contents\">");

        sb.append("<div id=\"left\">");

        sb.append("<table class=\"sche\">");
        sb.append("<tr><td class=\"top\" style=\"width:80px\">時刻</td><td class=\"top\" style=\"width:300px\">予定</td></tr>");
        sb.append("<tr><td class=\"time\">00:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">01:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">02:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">03:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">04:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">05:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">06:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">07:00</td><td class=\"ex\" rowspan=\"3\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td></tr>");
        sb.append("<tr><td class=\"time\">08:00</td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">09:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">10:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">11:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">12:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">13:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">14:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">15:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">16:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">17:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">18:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">19:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">20:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">21:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">22:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");
        sb.append("<tr><td class=\"time\">23:00</td><td class=\"contents\"></td></tr>");
        sb.append("<tr><td class=\"timeb\"></td><td class=\"contentsb\"></td></tr>");

        sb.append("</table>");

        sb.append("</div>");

        sb.append("<div id=\"right\">");

        sb.append("<form method=\"post\" action=\"\">");
        sb.append("<table>");
        sb.append("<tr>");

        sb.append("<td nowrap>日付</td>");
        sb.append("<td>");
        sb.append("<select name=\"YEAR\">");
        for (int i = 2020 ; i <= 2040 ; i++){
            sb.append("<option value=\"");
            sb.append(i);
            sb.append("\"");
            if (i == year){
                sb.append(" selected");
            }
            sb.append(">");
            sb.append(i);
            sb.append("年");
        }
        sb.append("</select> ");

        sb.append("<select name=\"MONTH\">");
        for (int i = 1 ; i <= 12 ; i++){
            sb.append("<option value=\"");
            sb.append(i);
            sb.append("\"");
            if (i == (month + 1)){
                sb.append(" selected");
            }
            sb.append(">");
            sb.append(i);
            sb.append("月");
        }
        sb.append("</select> ");

        sb.append("<select name=\"DAY\">");
        int monthLastDay = getMonthLastDay(year, month, day);
        for (int i = 1 ; i <= monthLastDay ; i++){
            sb.append("<option value=\"");
            sb.append(i);
            sb.append("\"");
            if (i == day){
                sb.append(" selected");
            }
            sb.append(">");
            sb.append(i);
            sb.append("日");
        }
        sb.append("</select>");

        sb.append("</td>");
        sb.append("</tr>");

        sb.append("<tr>");
        sb.append("<td nowrap>時刻</td>");
        sb.append("<td>");
        sb.append("<select name=\"SHOUR\">");
        sb.append("<option value=\"\" selected>--時");
        for (int i = 0 ; i <= 23 ; i++){
            sb.append("<option value=\"");
            sb.append(i);
            sb.append("\">");
            sb.append(i);
            sb.append("時");
        }
        sb.append("</select> ");

        sb.append("<select name=\"SMINUTE\">");
        sb.append("<option value=\"0\">00分");
        sb.append("<option value=\"30\">30分");
        sb.append("</select>");

        sb.append(" -- ");

        sb.append("<select name=\"EHOUR\">");
        sb.append("<option value=\"\" selected>--時");
        for (int i = 0 ; i <= 23 ; i++){
            sb.append("<option value=\"");
            sb.append(i);
            sb.append("\">");
            sb.append(i);
            sb.append("時");
        }
        sb.append("</select> ");

        sb.append("<select name=\"EMINUTE\">");
        sb.append("<option value=\"0\">00分");
        sb.append("<option value=\"30\">30分");
        sb.append("</select>");

        sb.append("</td>");
        sb.append("</tr>");

        sb.append("<tr>");
        sb.append("<td nowrap>予定</td>");
        sb.append("<td><input type=\"text\" name=\"PLAN\" value=\"\" size=\"30\" maxlength=\"100\">");
        sb.append("</td>");
        sb.append("</tr>");

        sb.append("<tr>");
        sb.append("<td valign=\"top\" nowrap>メモ</td>");
        sb.append("<td><textarea name=\"MEMO\" cols=\"30\" rows=\"10\" wrap=\"virtual\"></textarea></td>");
        sb.append("</tr>");
        sb.append("</table>");

        sb.append("<p>");
        sb.append("<input type=\"submit\" name=\"Register\" value=\"登録する\"> <input type=\"reset\" value=\"入力し直す\">");
        sb.append("<p>");
        sb.append("</form>");

        sb.append("</div>");
        sb.append("</div>");

        sb.append("</body>");
        sb.append("</html>");

        out.println(new String(sb));
    }

    protected int getMonthLastDay(int year, int month, int day){

        Calendar calendar = Calendar.getInstance();

        /* 今月が何日までかを確認する */
        calendar.set(year, month + 1, 0);
        int thisMonthlastDay = calendar.get(Calendar.DATE);

        return thisMonthlastDay;
    }

}