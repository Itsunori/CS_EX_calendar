import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class MonthView1 extends HttpServlet{

    public void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException{

        res.setContentType("text/html;charset=Shift_Jis");
        PrintWriter out = res.getWriter();

        StringBuffer sb = new StringBuffer();

        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0.1//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");

        sb.append("<html lang=\"ja\">");
        sb.append("<head>");
        sb.append("<meta http-equiv=\"Content-Type\" Content=\"text/html;charset=Shift_JIS\">");

        sb.append("<title>スケジュール管理</title>");

        sb.append("<style>");
        sb.append("table{border:1px solid #a9a9a9;width:90%;padding:0px;margin:0px;border-collapse:collapse;}");
        sb.append("td{width:12%;border-top:1px solid #a9a9a9;border-left:1px solid #a9a9a9;vertical-align:top;margin:0px;padding:2px;}");
        sb.append("td.week{background-color:#f0f8ff;text-align:center;}");
        sb.append("td.day{background-color:#f5f5f5;text-align:right;font-size:0.75em;}");
        sb.append("td.otherday{background-color:#f5f5f5;color:#d3d3d3;text-align:right;font-size:0.75em;}");
        sb.append("td.sche{background-color:#fffffff;text-align:left;height:80px;}");
        sb.append("img{border:0px;}");
        sb.append("p{font-size:0.75em;}");
        sb.append("</style>");

        sb.append("</head>");
        sb.append("<body>");

        sb.append("<p>2006年11月</p>");

        sb.append("<table>");

        sb.append("<tr><td class=\"week\">日</td><td class=\"week\">月</td><td class=\"week\">火</td><td class=\"week\">水</td><td class=\"week\">木</td><td class=\"week\">金</td><td class=\"week\">土</td></tr>");

        sb.append("<tr><td class=\"otherday\">29</td><td class=\"otherday\">30</td><td class=\"otherday\">31</td><td class=\"day\">1</td><td class=\"day\">2</td><td class=\"day\">3</td><td class=\"day\">4</td></tr>");
        sb.append(createScheduleStr());

        sb.append("<tr><td class=\"day\">5</td><td class=\"day\">6</td><td class=\"day\">7</td><td class=\"day\">8</td><td class=\"day\">9</td><td class=\"day\">10</td><td class=\"day\">11</td></tr>");
        sb.append(createScheduleStr());

        sb.append("<tr><td class=\"day\">12</td><td class=\"day\">13</td><td class=\"day\">14</td><td class=\"day\">15</td><td class=\"day\">16</td><td class=\"day\">17</td><td class=\"day\">18</td></tr>");
        sb.append(createScheduleStr());

        sb.append("<tr><td class=\"day\">19</td><td class=\"day\">20</td><td class=\"day\">21</td><td class=\"day\">22</td><td class=\"day\">23</td><td class=\"day\">24</td><td class=\"day\">25</td></tr>");
        sb.append(createScheduleStr());

        sb.append("<tr><td class=\"day\">26</td><td class=\"day\">27</td><td class=\"day\">28</td><td class=\"day\">29</td><td class=\"day\">30</td><td class=\"otherday\">1</td><td class=\"otherday\">2</td></tr>");
        sb.append(createScheduleStr());

        sb.append("</table>");

        sb.append("</body>");
        sb.append("</html>");

        out.println(new String(sb));
    }

    protected String createScheduleStr(){
        StringBuffer sb = new StringBuffer();

        sb.append("<tr>");
        for (int i = 0 ; i < 7 ; i++){
            sb.append("<td class=\"sche\"><img src=\"./img/memo.png\" width=\"14\" height=\"16\"></td>");
        }
        sb.append("</tr>");

        return (new String(sb));
    }
}