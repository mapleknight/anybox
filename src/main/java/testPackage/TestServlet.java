package testPackage;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.anybox.beans.User;
import com.anybox.database.DBConnectionPool;

/**
 * Simple servlet for testing. Generates HTML instead of plain text as with the
 * HelloWorld servlet.
 */

//@WebServlet("/testDataBase")
public class TestServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3625343490443470561L;

	@SuppressWarnings("rawtypes")
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		String sql = "SELECT * FROM anybox.user;";
		Connection conn = null;
		try {
			conn = DBConnectionPool.getInstance().getDataSource()
					.getConnection();
			
			// 创建SQL执行工具
			QueryRunner qRunner = new QueryRunner();
			// 执行SQL查询，并获取结果
			@SuppressWarnings("unchecked")
			List<User> list = (List<User>) qRunner.query(conn, sql,
					new BeanListHandler(User.class));
			// 输出查询结果
			for (User user : list) {
				System.out.println(user);
			}
			// resultSet.close();
			// st.close();
			// conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (Exception ignore) {
				}
		}

		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>\n" + "<html>\n"
				+ "<head><title>A Test Servlet</title></head>\n"
				+ "<body bgcolor=\"#fdf5e6\">\n" + "<h1>Test</h1>\n"
				+ "<p>Simple servlet for testing.</p>\n" + "</body></html>");
	}
}
