import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class total extends HttpServlet { 
public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Cookie[] cookies = request.getCookies();
    String uname = null;
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username")) {
                uname = cookie.getValue();
                break;
            }
        }
    }
    out.println("<header style = 'text-align: right'>" + uname + "&nbsp; &nbsp; &nbsp; &nbsp;");
    out.println("<a href='logout' target='_blank'>Logout</a> ");
    out.println("</header><hr style = 'border:'2px solid black>");
    String[] selectednovels = request.getParameterValues("selected");
    response.setContentType("text/html");
    out.println("<html>");
    out.println("<head>");
    out.println("<title>Checkout</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<h1>novels: </h1>");
    if (selectednovels != null) {
        double totalprice = 0.0;
        out.println("<table cellspacing='0' width='350px' border='1'>");
        out.println("<tr><th>novels</th><th>price</th></tr>");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn=null;
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/novels","root","");
            PreparedStatement stmt = conn.prepareStatement("SELECT price FROM novels WHERE name = ?");
            for (int i = 0; i < selectednovels.length; i++) {
                stmt.setString(1, selectednovels[i]);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    double price = rs.getDouble("price");
                    out.println("<tr><td>" + selectednovels[i] + "</td><td>" + price + "</td></tr>");
                    totalprice += price;
                } else {
                    out.println("<tr><td>" + selectednovels[i] + "</td><td>price not found</td></tr>");
                }
                rs.close();
            }
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            out.println("<p>Database error occurred.</p>");
        }
        out.println("</table>");
        out.println("<h1>Total price: " + totalprice + "</h1>");
     } else {
           out.println("<p>No travelling spots selected.</p>");
     }
     out.println("</body>");
     out.println("</html>");
    }
}
