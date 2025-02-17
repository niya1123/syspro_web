package index;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.sql.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

@WebServlet("/json/trend_data")
public class TrendDataJson extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        resp.setContentType("text/html; charset=UTF-8"); //①ブラウザへ渡す情報の文字コードを指定

        PrintWriter out = resp.getWriter();
        JSONArray jsonArr = new JSONArray();

        try {   
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://mysql:3306/qiita_rank?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B9:00", 
            "user", "userpass");
            Statement stmt = con.createStatement();
            String sqlStr = "SELECT * FROM trend_data order by like_count";
            ResultSet rs = stmt.executeQuery(sqlStr);

            while(rs.next()){
                JSONObject jsonObj = new JSONObject();
                int tag_id = rs.getInt("tag_id");
                String tag_name = rs.getString("tag_name");
                String trend_title = rs.getString("trend_title");
                String like_count = rs.getString("like_count");
                String trend_url = rs.getString("trend_url");
                jsonObj.put("ranking", String.valueOf(tag_id));
                jsonObj.put("tag_name", tag_name);
                jsonObj.put("trend_title", trend_title);
                jsonObj.put("like_count", String.valueOf(like_count));
                jsonObj.put("trend_url", trend_url);
                jsonArr.put(jsonObj);
            }

            out.println(jsonArr);

            rs.close();
            stmt.close(); 
            con.close();

        } catch (Exception e) {
            out.println("trend_data取得できんかったやよ～");
        }
        
    }
}