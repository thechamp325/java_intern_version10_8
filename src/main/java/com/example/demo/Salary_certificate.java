package com.example.demo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

public class Salary_certificate {

	public DB db = new DB();
	
public Map<String,String> req(@RequestBody Map<String, Object> payload) throws Exception{
	
	//CREATE A NEW TABLE SUCH THAT IT CONTAINS APPLICATION NUMBER<EMPIDdate>
	 
	
	String emp_id = (String)payload.get("Employee_ID");
	
	
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	java.util.Date date2 = sdf1.parse((String)payload.get("from_date"));
	Date from_date = new java.sql.Date(date2.getTime()); 
	
	java.util.Date date1 = sdf1.parse((String)payload.get("to_date"));
	Date to_date = new java.sql.Date(date1.getTime()); 
	
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	LocalDate localDate = LocalDate.now();
	Date date = java.sql.Date.valueOf(localDate);
	
	String dat=String.valueOf(date);
	
	Calendar cal = Calendar.getInstance();
	java.util.Date date3 = cal.getTime();
	DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
	String formattedDate = dateformat.format(date3);
	
	
	String salary_id=null;
	salary_id=emp_id+dat+formattedDate;
	boolean hod=false;
	boolean principal=false;
	boolean fin=false;
	boolean request=false;
	
	
	String sql="INSERT INTO public.salary(\r\n" + 
	"	principal,hod,\"Employee_ID\",fin,from_date,to_date,date,salary_id,request)\r\n" + 
"	VALUES ( ?, ?, ?, ?, ?, ?,?,?,?);";
	
	try {
			PreparedStatement stmt = db.connect().prepareStatement(sql);
			stmt.setBoolean(1,principal);
			stmt.setBoolean(2, hod);
			stmt.setString(3, emp_id);
			stmt.setBoolean(4, fin);
			stmt.setDate(5, from_date);
			stmt.setDate(6, to_date);
			stmt.setDate(7, date); 
			stmt.setString(8, salary_id);
			stmt.setBoolean(9,request);

			
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println(e.getMessage());

		}
	Map<String, String> map = new HashMap<String, String>();
	map.put("status", "Your request will be processed");

		return map;
}

public Map<String, String> check_req(@RequestBody Map<String, Object> payload) throws Exception{
	
//	String emp_id = (String)payload.get("empid");
	String empid = "Emp01";
	String emp;
	String empname;
	
	Date fromdate;
	Date todate;
	
	
	Map<String, String> map = new HashMap<String, String>();

		
	String sql="SELECT salary_id, principal, hod, \"Employee_ID\", fin, from_date, to_date, date,request\r\n" + 
			"	FROM public.salary;";
	Statement stmt = db.connect().createStatement();
	ResultSet rs=stmt.executeQuery(sql);
	
	while(rs.next())
	{
		try {
			 emp=rs.getString(4);
			 
			 fromdate=rs.getDate(6);
			
			 SimpleDateFormat simpleDateformat = new SimpleDateFormat("MMMM-YYYY");
			
			 todate=rs.getDate(7);
			
			
			//REQUIRED RECORD FOUND
			if(emp.contentEquals(empid) )
			{
				//IF FINAL IS FALSE,THEN IT COULD BE INCOMPLETE PROCESS OR A PREVIOUS REJECTED REQUEST.
				boolean field1=rs.getBoolean("principal"); //PRINCIPAL APPROVAL
				boolean field2=rs.getBoolean("hod"); //HOD APPROVAL
				boolean field3=rs.getBoolean("fin"); //FINAL APPROVAL
				boolean field4=rs.getBoolean("request"); //handled?
				if( field1==true && field2==true && field3==true && field4==false )
				{
					
					String sql4="SELECT \"ID\", \"Salutation\", \"First_Name\", \"Middle_Name\", \"Last_Name\", \"Father_Name\", \"Mother_Name\"\r\n" + 
							"	FROM public.\"Personal\" where \"Employee_ID\"="+emp+";";
					Statement stmt1 = db.connect().createStatement();
					ResultSet rs4=stmt1.executeQuery(sql4);
					empname=rs4.getString(3)+rs4.getString(5);
					
					
					
					String sql2="SELECT dep,designation,pay,paygrade from public.officeinfo where \"Employee_ID\"='"+emp+"';";
					
					Statement stmt2 = db.connect().createStatement();
					ResultSet rs2=stmt2.executeQuery(sql2);
					
					
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
					LocalDate localDate = LocalDate.now();
					Date datetoday = java.sql.Date.valueOf(localDate);
					
					
					
					
					int pay=rs2.getInt(15);
					String designation=rs2.getString(2);
					String dep=(String)rs2.getString(3);
					
					int paygrade=rs2.getInt(16);
					int hra_m1=(int) ((pay+paygrade)*0.2);
					int hra_m2=(int) ((pay+paygrade)*0.21);
					int hra_m3=(int) ((pay+paygrade)*0.22);
					
					int da_m1=(int) ((pay+paygrade)*0.4);
					int da_m2=(int) ((pay+paygrade)*0.41);
					int da_m3=(int) ((pay+paygrade)*0.42);
					
					int dp_m1=(int) ((pay+paygrade)*0.3);
					int dp_m2=(int) ((pay+paygrade)*0.31);
					int dp_m3=(int) ((pay+paygrade)*0.32);
					
					int cca_m1=(int) ((pay+paygrade)*0.2);
					int cca_m2=(int) ((pay+paygrade)*0.21);
					int cca_m3=(int) ((pay+paygrade)*0.22);
					
					int ta_m1=(int) ((pay+paygrade)*0.2);
					int ta_m2=(int) ((pay+paygrade)*0.21);
					int ta_m3=(int) ((pay+paygrade)*0.22);
					
					
					
					//PROVIDENT FUND
					int pf_m1=(int) ((pay+da_m1)*0.12);
					int pf_m2=(int) ((pay+da_m2)*0.12);
					int pf_m3=(int) ((pay+da_m3)*0.12);
					
					//PROFESSIONAL TAX
					int pt_m1=(int) ((paygrade)*0.10);
					int pt_m2=(int) ((paygrade)*0.10);
					int pt_m3=(int) ((paygrade)*0.10);
					
					//INCOME TAX
					int it_m1=(int) ((pay)*0.30);
					int it_m2=(int) ((pay)*0.30);
					int it_m3=(int) ((pay)*0.30);
					
					int revenue_m1=5;
					int revenue_m2=5;
					int revenue_m3=5;//revenue stamp
					
					int other_m1=(int) ((pay)*0.02);
					int other_m2=(int) ((pay)*0.02);
					int other_m3=(int) ((pay)*0.02);
					
					int total_m1=pf_m1+pt_m1+it_m1+revenue_m1+other_m1;
					int total_m2=pf_m2+pt_m2+it_m2+revenue_m2+other_m2;
					int total_m3=pf_m3+pt_m3+it_m3+revenue_m3+other_m3;
									
					
					int gross1=pay+hra_m1+cca_m1+da_m1+cca_m1+dp_m1;
					int gross2=pay+hra_m2+cca_m2+da_m2+cca_m2+dp_m2;
					int gross3=pay+hra_m3+cca_m3+da_m3+cca_m3+dp_m3;
					
					int net1=gross1-total_m1;
					int net2=gross2-total_m2;
					int net3=gross3-total_m3;
					
					
					
					
					map.put("Report_date",String.valueOf(datetoday));
					map.put("name",empname);
					map.put("designation",designation);
					map.put("dep",dep);
					
					map.put("fromdate",simpleDateformat.format(fromdate)); 
					map.put("todate",simpleDateformat.format(todate));
					
					map.put("pay1",String.valueOf(pay));
					map.put("pay2",String.valueOf(pay));
					map.put("pay3",String.valueOf(pay));
					
					map.put("hra_m1",String.valueOf(hra_m1));
					map.put("hra_m2",String.valueOf(hra_m2));
					map.put("hra_m3",String.valueOf(hra_m3));
					
					map.put("da_m1",String.valueOf(da_m1));
					map.put("da_m2",String.valueOf(da_m2));
					map.put("da_m3",String.valueOf(da_m3));
					
					map.put("dp_m1",String.valueOf(dp_m1));
					map.put("dp_m2",String.valueOf(dp_m2));
					map.put("dp_m3",String.valueOf(dp_m3));
					
					map.put("cca_m1",String.valueOf(cca_m1));
					map.put("cca_m2",String.valueOf(cca_m2));
					map.put("cca_m3",String.valueOf(cca_m3));

					
					map.put("ta_m1",String.valueOf(ta_m1));
					map.put("ta_m2",String.valueOf(ta_m2));
					map.put("ta_m3",String.valueOf(ta_m3));
					
					map.put("pf_m1",String.valueOf(pf_m1));
					map.put("pf_m2",String.valueOf(pf_m2));
					map.put("pf_m3",String.valueOf(pf_m3));
					
					map.put("pt_m1",String.valueOf(pt_m1));
					map.put("pt_m2",String.valueOf(pt_m2));
					map.put("pt_m3",String.valueOf(pt_m3));
					
					map.put("it_m1",String.valueOf(it_m1));
					map.put("it_m2",String.valueOf(it_m2));
					map.put("it_m3",String.valueOf(it_m3));
					
					map.put("revenue_m1",String.valueOf(revenue_m1));
					map.put("revenue_m2",String.valueOf(revenue_m2));
					map.put("revenue_m3",String.valueOf(revenue_m3));
					
					map.put("other_m1",String.valueOf(other_m1));
					map.put("other_m2",String.valueOf(other_m2));
					map.put("other_m3",String.valueOf(other_m3));
					
					map.put("total_m1",String.valueOf(total_m1));
					map.put("total_m2",String.valueOf(total_m2));
					map.put("total_m3",String.valueOf(total_m3));
					
					map.put("gross1",String.valueOf(gross1));
					map.put("gross2",String.valueOf(gross2));
					map.put("gross3",String.valueOf(gross3));
					
					map.put("net1",String.valueOf(net1));
					map.put("net2",String.valueOf(net2));
					map.put("net3",String.valueOf(net3));
					
					map.put(empname, "name");
				
					return map;	
				}
				
			}
			
		}
		catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println(e.getMessage());

		}
		
	}
	map.put("Not","Succesful");
	return map;
	
}

//HOD

public Map<String, String> live_reqhod() throws SQLException {//tested
	
	
   	String false1="false";
	String sql1="SELECT * FROM public.salary where request= false ;";
	Map<String, String> emp_list = new HashMap<String, String>();
	
	Statement st = db.connect().createStatement();
	ResultSet rs = st.executeQuery(sql1);
	rs.next();
	
	System.out.println("Here1");
	String empid=rs.getString("Employee_ID");
	System.out.println(empid);

	String sql2="SELECT \"First_Name\", \"Last_Name\"\r\n" + 
			"	FROM public.\"Personal\" where \"Employee_ID\"='"+empid+"';";
	Statement st2 = db.connect().createStatement();
	ResultSet rs1 = st2.executeQuery(sql2);
	rs1.next();

	System.out.println("Here2");
	String random=rs1.getString("First_Name");
	System.out.println(random);

System.out.println(rs.getBoolean("fin"));


	
	int j=0;
	String i;
	do{
		j++;
		i=String.valueOf(j);
		
		if(rs.getBoolean("fin")==false && rs.getBoolean("request")==false && rs.getBoolean("hod")==false ) {
			
			
			System.out.println("Here3");
			emp_list.put("First_name"+i, rs1.getString("First_Name"));
			emp_list.put("Last_name"+i, rs1.getString("Last_Name"));
			emp_list.put("Employee_ID"+i, empid);
				
	}
	
	}while(rs.next());
	System.out.println("4");
	return emp_list;	
	}  

//PRINCIPAL
	public Map<String, String> live_requestp() throws SQLException {
		
		//String empid=(String)payload.get("employee_id");
	   		
		String sql1="SELECT * FROM public.salary where request = false;";
		Map<String, String> emp_list = new HashMap<String, String>();
		
		Statement st = db.connect().createStatement();
		ResultSet rs = st.executeQuery(sql1);
		int j=0;
		String i;
		while (rs.next()){
			j++;
			i=String.valueOf(j);
			String empid=rs.getString("Employee_ID");
			if(rs.getBoolean("fin")==false && rs.getBoolean("request")==false && rs.getBoolean("hod")==true && rs.getBoolean("principal")==false) {
				
				String sql2="SELECT \"First_Name\", \"Last_Name\" FROM public.\"Personal\" where \"Employee_ID\"='"+empid+"';";
				ResultSet rs1 = st.executeQuery(sql2);
				rs1.next();
				emp_list.put("First_name"+i, rs1.getString("First_Name"));
				emp_list.put("Last_name"+i, rs1.getString("Last_Name"));
				emp_list.put("Employee_ID"+i, empid);
						
		}
			
		
		}  
		return emp_list;

	}
	
	
	
	
	
	
	


}
	
	
	
	
