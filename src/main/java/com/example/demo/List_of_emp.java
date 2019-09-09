package com.example.demo;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class List_of_emp {
	
	DB db = new DB();
	
	public Map<String , String> Employee_list(@RequestBody Map<String, Object> payload) throws Exception {
		Map <String , String> mp = new HashMap<String , String>();
		String dept = (String) payload.get("Department");
		
		String sql1 = "SELECT * FROM public.pastteaching WHERE department = '" + dept + "';";
		Statement st = db.connect().createStatement();
		ResultSet rs = st.executeQuery(sql1);
		rs.next();
		int j = 1;
		String i;
		do {
			String emp_id = rs.getString("employee_id");
			String sql2 = "SELECT * FROM public.\"Personal \" WHERE \"Employee_ID\" = '" + emp_id + "';";
			Statement st2 = db.connect().createStatement();
			ResultSet rs2 = st.executeQuery(sql2);
			rs2.next();
			
			i = String.valueOf(j);
			mp.put("Employee ID"+i , emp_id);
			mp.put("Name"+i , rs2.getString("First_Name") + rs2.getString("Middle_Name") + rs2.getString("Last_Name"));
			mp.put("Designation"+i , rs.getString("design_teach"));
			
		}while(rs.next());
		
		return mp;
	}
}
