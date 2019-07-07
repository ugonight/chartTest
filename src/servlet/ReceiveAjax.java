package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.tuple.Pair;

import com.fasterxml.jackson.databind.ObjectMapper;

import chartTest.entity.Inventory;
import chartTest.util.AnalyzeInventory;
import chartTest.util.CreateInventory;

/**
 * Servlet implementation class ReceiveAjax
 */
@WebServlet("/receive")
public class ReceiveAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReceiveAjax() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		int divnum = Integer.parseInt(request.getParameter("divnum"));
		int shiftnum = Integer.parseInt(request.getParameter("shiftnum"));
		int duration = Integer.parseInt(request.getParameter("duration"));
		String str = "";
		List<Integer> idlist = new ArrayList<>();
		for (int i = 0; (str = request.getParameter("idlist" + i)) != null; i++) {
			idlist.add(Integer.parseInt(str));
		}


		HttpSession session = request.getSession();
		List<Inventory> log = null;
		List<List<ResSet>> resultList = new ArrayList<>();

		// 全てのIDについて処理を行う
		for (int id : idlist) {
			if (session.getAttribute("log" + id) == null) {
				log = CreateInventory.getInventoriyLog();
				session.setAttribute("log" + id, log);
			} else {
				log = (List<Inventory>) session.getAttribute("log" + id);
			}

			List<Pair<Integer, String>> list = AnalyzeInventory.dividePeriod(log, duration, divnum, shiftnum);

			List<ResSet> resSets = new ArrayList<>();
			for (Pair<Integer, String> data : list) {
				ResSet resSet = new ResSet(data.getLeft(), data.getRight());
				resSets.add(resSet);
			}
			resultList.add(resSets);
		}

		// データを書き出して送信
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(resultList);
		// System.out.println(json);
		PrintWriter pw = response.getWriter();
		pw.print(json);
		pw.close();
	}

}

class ResSet {
	public int value;
	public String label;

	public ResSet(int value, String label) {
		super();
		this.value = value;
		this.label = label;
	}
}
