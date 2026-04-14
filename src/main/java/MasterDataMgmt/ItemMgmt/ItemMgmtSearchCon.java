package MasterDataMgmt.ItemMgmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/item-search")
public class ItemMgmtSearchCon extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json; charset=UTF-8");

        // 🔥 파라미터 받기
        String type = request.getParameter("type");
        String dateType = request.getParameter("dateType");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String field = request.getParameter("field");
        String keyword = request.getParameter("keyword");
  
        List<ItemMgmtDTO> list = new ItemMgmtService().search(type, dateType, startDate, endDate, field, keyword);

        // 🔥 JSON 만들기
        PrintWriter out = response.getWriter();
        StringBuilder json = new StringBuilder();

        json.append("[");

        for (int i = 0; i < list.size(); i++) {
            ItemMgmtDTO item = list.get(i);

            json.append("{");
            json.append("\"item_id\":").append(item.getItem_id()).append(",");
            json.append("\"item_code\":\"").append(item.getItem_code()).append("\",");
            json.append("\"item_name\":\"").append(item.getItem_name()).append("\",");
            json.append("\"item_type\":\"").append(item.getItem_type()).append("\",");
            json.append("\"unit\":\"").append(item.getUnit()).append("\",");
            json.append("\"spec\":\"").append(item.getSpec()).append("\",");
            json.append("\"supplier_name\":\"").append(item.getSupplier_name()).append("\",");
            json.append("\"safety_stock\":").append(item.getSafety_stock()).append(",");
            json.append("\"use_yn\":\"").append(item.getUse_yn()).append("\",");
            json.append("\"remark\":\"").append(item.getRemark()).append("\",");
            json.append("\"created_at\":\"").append(item.getCreated_at()).append("\",");
            json.append("\"updated_at\":\"").append(item.getUpdated_at()).append("\"");
            json.append("}");

            if (i < list.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        out.print(json.toString());
        out.flush();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
