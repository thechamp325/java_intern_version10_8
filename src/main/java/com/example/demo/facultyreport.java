package com.example.demo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math; 


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class facultyreport {

	AdminDB db = new AdminDB();
	
	public Map<String,Integer> facultyinfo(@RequestBody Map<String, Object> payload,AdminDB adb) throws SQLException {
		Map<String, Integer> faculty = new HashMap<String, Integer>();
		String Department = (String) payload.get("department");
		boolean Shift = (boolean) payload.get("shift");
	    System.out.println(Department);
	    System.out.println(Shift);


		ResultSet rs = null;
		String sql="SELECT * FROM public.students";
	    System.out.println("Query execution");

		
		
			Statement stmt = adb.connect().createStatement();
		     rs = stmt.executeQuery(sql); 
			    System.out.println("Query executed");

		    

		while (rs.next())
		{
		    System.out.println("inside while");

			if((rs.getString(1).equals(Department))&& (Shift==rs.getBoolean(2)))
			{		   

		    System.out.print("Column 1 returned ");
		    int prof=0,asso=0,asst=0,students=0;
		    students = (int)rs.getInt("students");
		    System.out.println(students);

		    int fac=(int) Math.ceil(students/20.0);
		    System.out.println(fac);
		    
		    prof = fac/9;
		    System.out.println(prof);

		    asso = fac*2/9;
		    System.out.println(asso);

		    asst = fac-prof-asso;
		    System.out.println(asst);

		    faculty.put("Proffesor",prof);
		    faculty.put("Assosiate_Professor",asso);
		    faculty.put("Assistant",asst);
		    break;
		    }
		}
		rs.close();
		
		return faculty;
	}
}