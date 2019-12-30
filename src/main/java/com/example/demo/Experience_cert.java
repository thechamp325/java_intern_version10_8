package com.example.demo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//		************************** TEST ALL FUNCTIONS***********************************

public class Experience_cert {
	
	DB db = new DB();
	
	//REQUEST FOR EXPERIENCE CERTIFICATE
	public Map<String,String> exp_request(@RequestBody Map<String, Object> payload) throws Exception{
		
		//CREATE A NEW TABLE SUCH THAT IT CONTAINS APPLICATION NUMBER <EMPID+date>
		 
		
		String emp_id = (String)payload.get("Employee_ID");
		String reason=(String)payload.get("Reason");
		LocalDate localDate = LocalDate.now();
		Date date = java.sql.Date.valueOf(localDate);
		
		String dat=String.valueOf(date);
		
		System.out.println(emp_id + dat);
		
		Calendar cal = Calendar.getInstance();
		java.util.Date date3 = cal.getTime();
		DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
		String formattedDate = dateformat.format(date3);
		

		
		String exp_id=null;
		exp_id=emp_id+dat+formattedDate;
		boolean hod=false;
		boolean principal=false;
		boolean fin=false;
		boolean request=false;
		boolean admin_approval=false;
		
		
		String sql="INSERT INTO public.exprelcert(\r\n" + 
		"	principal,hod,\"Employee_ID\",fin,date,request,admin_approval,exprelid,reason)\r\n" + 
	"	VALUES ( ?, ?, ?, ?, ?, ?,?,?,?);";
		
		try {
				PreparedStatement stmt = db.connect().prepareStatement(sql);
				stmt.setBoolean(1,principal);
				stmt.setBoolean(2, hod);
				stmt.setString(3, emp_id);
				stmt.setBoolean(4, fin);
				stmt.setDate(5, date);
				stmt.setBoolean(6, request);
				stmt.setBoolean(7, admin_approval); 
				stmt.setString(8, exp_id);
				stmt.setString(9, reason);
	
				stmt.executeUpdate();
				
			} catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println(e.getMessage());

			}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", "Your request will be processed");

			return map;
	}

	//LIVE REQUESTS FOR HOD'S APPROVAL

	public List exp_live_reqhod() throws SQLException {//tested
		
		
	 	
		String sql1="SELECT * FROM public.exprelcert where request= false and hod = false;";
		
		
		Statement st = db.connect().createStatement();
		ResultSet rs = st.executeQuery(sql1);
		
		String empid = null;
		String sql2=null;
		Statement st2 = null;
		ResultSet rs1 = null;
		String random=null;
		
		
		String sql3=null;
		Statement st3 = null;
		ResultSet rs3 = null;
		
		

	List<Map<String, String>> mymap = new ArrayList<Map<String, String>>();
		
		int j=0;
		while(rs.next()){
			
			
			 empid=rs.getString("Employee_ID");
			 System.out.println(empid);

			 sql2="SELECT \"First_Name\", \"Last_Name\"\r\n" + 
					"	FROM public.\"Personal\" where \"Employee_ID\"='"+empid+"';";
			 st2 = db.connect().createStatement();
			 rs1 = st2.executeQuery(sql2);
			 rs1.next();
			
			
			//CURRENT DESIGNATION
			sql3="SELECT designation FROM public.officeinfo where \"Employee_ID\"='"+empid+"';";
			st3 = db.connect().createStatement();
		    rs3 = st3.executeQuery(sql3);
			rs3.next();
			
			

			random=rs1.getString("First_Name");
			System.out.println(random);
			Map<String, String> emp_list = new HashMap<String, String>();
			
			if(rs.getBoolean("fin")==false && rs.getBoolean("request")==false && rs.getBoolean("hod")==false ) {
				
				
				emp_list.put("name", rs1.getString("First_Name")+" "+rs1.getString("Last_Name"));
			
				emp_list.put("EMPID", empid);
				emp_list.put("designation", rs3.getString("designation"));
				emp_list.put("Type", "Experience Certificate");
				emp_list.put("Certificate_id",rs.getString("exprelid"));
						
				System.out.println(rs3.getString("designation"));
				
				mymap.add(j,emp_list);
				j++;	
		}
		
		}
		return mymap;	
		}  

	//LIVE REQUESTS FOR PRINCIPAL'S APPROVAL
	
	public List exp_live_requestp() throws SQLException {
		
		
	 	//String false1="false";
		String sql1="SELECT * FROM public.exprelcert where request=false and hod = true and fin = false ;";
		
		
		Statement st = db.connect().createStatement();
		ResultSet rs = st.executeQuery(sql1);
		
		String empid = null;
		String sql2=null;
		Statement st2 = null;
		ResultSet rs1 = null;
		String random=null;
		
		
		String sql3=null;
		Statement st3 = null;
		ResultSet rs3 = null;
		
		


	List<Map<String, String>> mymap = new ArrayList<Map<String, String>>();
		
		int j=0;
		while(rs.next()){
			
			
			 empid=rs.getString("Employee_ID");
			 System.out.println(empid);

			 sql2="SELECT \"First_Name\", \"Last_Name\"\r\n" + 
					"	FROM public.\"Personal\" where \"Employee_ID\"='"+empid+"';";
			 st2 = db.connect().createStatement();
			 rs1 = st2.executeQuery(sql2);
			 rs1.next();
			
			
			//CURRENT DESIGNATION
			sql3="SELECT designation FROM public.officeinfo where \"Employee_ID\"='"+empid+"';";
			st3 = db.connect().createStatement();
		    rs3 = st3.executeQuery(sql3);
			rs3.next();
			
			

			random=rs1.getString("First_Name");
			System.out.println(random);
			Map<String, String> emp_list = new HashMap<String, String>();
			
			if(rs.getBoolean("fin")==false && rs.getBoolean("request")==false &&rs.getBoolean("hod")==true) {
				
				
				emp_list.put("name", rs1.getString("First_Name")+" "+rs1.getString("Last_Name"));
				
				emp_list.put("EMPID", empid);
				emp_list.put("designation", rs3.getString("designation"));
				emp_list.put("Type", "Experience Certificate");
				emp_list.put("Certificate_id",rs.getString("exprelid"));
				

				System.out.println(rs3.getString("designation"));
				
				mymap.add(j,emp_list);
				j++;	
		}
		
		}
		return mymap;	
		}  
		
		
	//CHECK REQUEST AND DISPLAY CERTIFICATE
	
	public Map<String, String> Employee_exp(@RequestBody Map<String, Object> payload) throws SQLException
	{
		
		String emp = (String)payload.get("Employee_ID");
		String exprelid=(String)payload.get("expid");
		
		Map<String , String> map = new HashMap <String, String>();
		
		
		String sql="SELECT * FROM public.exprelcert where exprelid='"+exprelid+"';";
				
		Statement stmt = db.connect().createStatement();
		ResultSet rs=stmt.executeQuery(sql);
	
		while(rs.next()){ 
			try {
				 
				
					//IF FINAL IS FALSE,THEN IT COULD BE INCOMPLETE PROCESS OR A PREVIOUS REJECTED REQUEST.
					boolean field1=rs.getBoolean("principal"); //PRINCIPAL APPROVAL
					boolean field2=rs.getBoolean("hod"); //HOD APPROVAL
					boolean field3=rs.getBoolean("fin"); //FINAL APPROVAL
					boolean field4=rs.getBoolean("request"); //handled or not?
					boolean field5= rs.getBoolean("admin_approval");//if admin printed the report
					System.out.println(field1+" "+field2+" "+field3+" "+field4+" "+field5);
					
					if(field3==false) 
					{
						String PRINCIPAL = Boolean.toString(field1);
						String HOD = Boolean.toString(field2);
						String ADMIN = Boolean.toString(field5);
						String TYPE = "Experience Certificate";

						map.put("Principal",PRINCIPAL);
						map.put("HOD",HOD);
						map.put("Admin",ADMIN);
						map.put("Type",TYPE);
						return map;

					}
					else if( field1==true && field2==true && field3==true && field4==false )
					{
						
						String sql1 = "SELECT * FROM public.\"Personal\" where \"Employee_ID\" = '"+emp+"';";
						Statement st = db.connect().createStatement();
						ResultSet rs1 = st.executeQuery(sql1);
						rs1.next();
						
						map.put("Name" , rs1.getString("First_Name") + rs1.getString("Middle_Name") + rs1.getString("Last_Name"));
								
						
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
						LocalDate localDate = LocalDate.now();
						Date datetoday = java.sql.Date.valueOf(localDate);
						
						
						String sql2 = "SELECT * FROM public.pastteaching WHERE employee_id ='"+emp+"';";
						Statement st2 = db.connect().createStatement();
						ResultSet rs2 = st2.executeQuery(sql2);
						rs2.next();
						int j = 1;
						String i;
							do {
								i = String.valueOf(j);
								map.put("Designation"+i , rs2.getString("design_teach"));
								
								DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd"); 
								
								String frm_date = dateFormat.format(rs2.getDate("startdate"));
								map.put("From"+i , frm_date); 
								
								String to_date = dateFormat.format(rs2.getDate("enddate"));
								map.put("To"+i , to_date);
								
								map.put("Department"+i , rs2.getString("department"));
								j++;
							}while(rs2.next()); 
					}
				
			}
			catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println(e.getMessage());

			}
		}
			return map;
	
	}
	}
