package com.example.demo;

import java.sql.Array;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

public class PastInfo {//tested all functions
	
		DB db = new DB();

		
		
		//*******STORE IN TEMPERORY TABLE*******
		public String entry(Map<String, Object> payload,String log) throws Exception{//tested
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			
			int no_of_institutes  = (int) payload.get("no_of_institutes");
			String[] past_teaching = new String[no_of_institutes];
			Date[] pt_startdate = new Date[no_of_institutes];
			Date[] pt_enddate = new Date[no_of_institutes];
			String[] design_teach=new String[no_of_institutes];
			String[] department = new String[no_of_institutes];
			System.out.println("dates read");

			
			int j = 0;
			while(j < no_of_institutes) {
				String i = String.valueOf(j);
				past_teaching[j]= (String) payload.get("inst"+i);
				System.out.println("dates in array 1");

				java.util.Date tempdate = sdf1.parse((String)payload.get("instsdate"+i));
				System.out.println("dates in array 2");

				pt_startdate[j] = new java.sql.Date(tempdate.getTime());
				System.out.println("dates in array 3");

				
				java.util.Date tempdate1 = sdf1.parse((String)payload.get("instedate"+i));
				pt_enddate[j] = new java.sql.Date(tempdate1.getTime());
				design_teach[j]=(String)payload.get("design_teach"+i);
				department[j]=(String)payload.get("department"+i);

				j++;

			}
			System.out.println("dates in array");

//			Array past_teaching1=db.connect().createArrayOf("text",past_teaching);
//			Array pt_startdate1=db.connect().createArrayOf("date",pt_startdate);
//			Array pt_enddate1=db.connect().createArrayOf("date",pt_enddate);	
//			Array design_teach1=db.connect().createArrayOf("text",design_teach);
//			Array department1=db.connect().createArrayOf("text",department);


			
			j=0;
			int no_of_industries = (int) payload.get("no_of_industries");
			String[] past_industries = new String[no_of_industries];
			Date[] pi_startdate = new Date[no_of_industries];
			Date[] pi_enddate = new Date[no_of_industries];
			while(j < no_of_industries) {
				String i = String.valueOf(j);
				past_industries[j]= (String) payload.get("ind"+i+"");
				java.util.Date tempdate = sdf1.parse((String)payload.get("indsdate"+i+""));
				pi_startdate[j] = new java.sql.Date(tempdate.getTime());
				
				java.util.Date tempdate1 = sdf1.parse((String)payload.get("indedate"+i+""));
				pi_enddate[j] = new java.sql.Date(tempdate1.getTime());
				j++;
			}
			
//			 
//			Array past_industries1=db.connect().createArrayOf("text",past_industries);
//			Array pi_startdate1=db.connect().createArrayOf("date",pi_startdate);
//			Array pi_enddate1=db.connect().createArrayOf("date",pi_enddate);
			
			j=0;
			int no_of_researches = (int) payload.get("no_of_researches");
			String[] past_researches = new String[no_of_researches];
			Date[] pr_startdate = new Date[no_of_researches];
			Date[] pr_enddate = new Date[no_of_researches];
			while(j < no_of_researches) {
				String i = String.valueOf(j);
				past_researches[j]= (String) payload.get("res"+i+"");
				java.util.Date tempdate = sdf1.parse((String)payload.get("ressdate"+i+""));
				pr_startdate[j] = new java.sql.Date(tempdate.getTime());
				
				java.util.Date tempdate1 = sdf1.parse((String)payload.get("resedate"+i+""));
				pr_enddate[j] = new java.sql.Date(tempdate1.getTime());
				j++;
			}
			 
//			Array past_researches1=db.connect().createArrayOf("text",past_researches);
//			Array pr_startdate1=db.connect().createArrayOf("date",pr_startdate);
//			Array pr_enddate1=db.connect().createArrayOf("date",pr_enddate);
			
			System.out.println("whiles completed");
			
			String sqlteach ="INSERT INTO public.pastteaching(\r\n" + 
					"	employee_id, past_teaching, startdate, enddate, design_teach, \"primary\", department,approve)\r\n" + 
					"	VALUES (?, ?, ?, ?, ?, ?, ?,?);";
			j=0;
			while(j<no_of_institutes) {
			try {
				PreparedStatement stmt = db.connect().prepareStatement(sqlteach);
				
				stmt.setString(1,log);
				stmt.setString(2,past_teaching[j]);
				stmt.setDate(3, pt_startdate[j]);
				stmt.setDate(4, pt_enddate[j]);
				stmt.setString(5,design_teach[j]);
				
				Calendar cal = Calendar.getInstance();
				java.util.Date date = cal.getTime();
				DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
				String formattedDate = dateformat.format(date);
				
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				LocalDate localDate = LocalDate.now();
				String date2 = dtf.format(localDate);
				System.out.println(dtf.format(localDate)); //2016/11/16
				String pkey=null;
				pkey=log+formattedDate+date2;//1
				
				stmt.setString(6,pkey);
				stmt.setString(7, department[j]);
				stmt.setBoolean(8,false);




				stmt.executeUpdate();	
			} 
			catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println(e.getMessage());

			}
			j++;
			}
			
			
			
			String sqlind ="INSERT INTO public.pastindustry(\r\n" + 
					"	employee_id, past_industry, startdate, enddate, design_industry, primaryk,approve)\r\n" + 
					"	VALUES (?, ?, ?, ?, ?, ?,?);";
			j=0;
			while(j<no_of_industries) {
			try {
				PreparedStatement stmt = db.connect().prepareStatement(sqlind);
				stmt.setString(1,log);
				stmt.setString(2,past_industries[j]);
				stmt.setDate(3, pi_startdate[j]);
				stmt.setDate(4, pi_enddate[j]);
				
				
				Calendar cal = Calendar.getInstance();
				java.util.Date date = cal.getTime();
				DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
				String formattedDate = dateformat.format(date);
				
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				LocalDate localDate = LocalDate.now();
				String date2 = dtf.format(localDate);
				System.out.println(dtf.format(localDate)); //2016/11/16
				String pkey=null;
				pkey=log+formattedDate+date2;//1
				
				stmt.setString(5,"past_industry");

				stmt.setString(6,pkey);
				stmt.setBoolean(7,false);



				stmt.executeUpdate();	
			} 
			catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println(e.getMessage());

			}
			j++;
			}
			
			
			
			String sqlres ="INSERT INTO public.pastresearch(\r\n" + 
					"	employee_id, past_research, startdate, enddate, design_research, primaryk,approve)\r\n" + 
					"	VALUES (?, ?, ?, ?, ?, ?,?);";
			j=0;
			while(j<no_of_researches) {
			try {
				PreparedStatement stmt = db.connect().prepareStatement(sqlres);
				stmt.setString(1,log);
				stmt.setString(2,past_researches[j]);
				stmt.setDate(3, pr_startdate[j]);
				stmt.setDate(4, pr_enddate[j]);
			
				
				Calendar cal = Calendar.getInstance();
				java.util.Date date = cal.getTime();
				DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
				String formattedDate = dateformat.format(date);
				
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				LocalDate localDate = LocalDate.now();
				String date2 = dtf.format(localDate);
				System.out.println(dtf.format(localDate)); //2016/11/16
				String pkey=null;
				pkey=log+formattedDate+date2;//1
				
				stmt.setString(5,"past_research");

				stmt.setString(6,pkey);
				stmt.setBoolean(7,false);



				stmt.executeUpdate();	
			} 
			catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println(e.getMessage());

			}
			j++;
			}
			return "done";
		 }
		
		
		//*****VALIDATE PAST TEACHING*****
		
		//CONVERT RS OBJECT TO ARRAY TO STRING!
		//https://stackoverflow.com/questions/14935016/convert-a-result-set-from-sql-array-to-array-of-strings
		
		
		
	public Map<String, String> approval_list( Map<String, Object> payload) throws SQLException {//tested
			
			String empid=(String)payload.get("employee_id");
		   		
			String sql1="SELECT * FROM public.pastteaching where employee_id = '"+empid+"' ;";
			Map<String, String> pastteaching = new HashMap<String, String>();
			
			Statement st = db.connect().createStatement();
			ResultSet rs = st.executeQuery(sql1);
			int j=0;
			String i;
			while (rs.next()){
				j++;
				i=String.valueOf(j);
		
				if(empid.equals(rs.getString("employee_id"))&&rs.getBoolean("approve")==false) {
					pastteaching.put("past_teaching"+i,rs.getString("past_teaching"));
					pastteaching.put("pt_startdate"+i,rs.getString("startdate"));
					pastteaching.put("pt_enddate"+i,rs.getString("enddate"));
					pastteaching.put("design_teach"+i,rs.getString("design_teach"));
					pastteaching.put("department"+i,rs.getString("department"));
					


			}
			}
			
			
			String sql2="SELECT * FROM public.pastindustry where employee_id = '"+empid+"' ;";
			Map<String, String> pastindustry = new HashMap<String, String>();
			
			Statement st2 = db.connect().createStatement();
			ResultSet rs2 = st.executeQuery(sql2);
			j=0;
			while (rs2.next()){
				j++;
				i=String.valueOf(j);
		
				if(empid.equals(rs2.getString("employee_id"))&&rs2.getBoolean("approve")==false) {
					pastindustry.put("past_industry"+i,rs2.getString("past_industry"));
					pastindustry.put("pi_startdate"+i,rs2.getString("startdate"));
					pastindustry.put("pi_enddate"+i,rs2.getString("enddate"));
					pastindustry.put("design_industry"+i,rs2.getString("design_industry"));


			}
			}
			
			
			String sql3="SELECT * FROM public.pastresearch where employee_id = '"+empid+"';";
			Map<String, String> pastresearch = new HashMap<String, String>();
			
			Statement st3 = db.connect().createStatement();
			ResultSet rs3 = st.executeQuery(sql3);
			j=0;
			while (rs3.next()){
				j++;
				i=String.valueOf(j);
		
			if(empid.equals(rs3.getString("employee_id"))&&rs3.getBoolean("approve")==false) {
					pastresearch.put("past_research"+i,rs3.getString("past_research"));
					pastresearch.put("pr_startdate"+i,rs3.getString("startdate"));
					pastresearch.put("pr_enddate"+i,rs3.getString("enddate"));
					pastresearch.put("design_research"+i,rs3.getString("design_research"));
			}
			}
			
			pastteaching.putAll(pastindustry);
			pastteaching.putAll(pastresearch);
			return pastteaching;
		}  
	


public String approve( Map<String, Object> payload) {

	String empid=  (String) payload.get("employee_id");
	String sql1="UPDATE public.pastteaching SET approve=? WHERE employee_id = '"+empid+"';";
	try{
		PreparedStatement stmt = db.connect().prepareStatement(sql1);
	
		stmt.setBoolean(1,true);
		stmt.executeUpdate();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	String sql2="UPDATE public.pastindustry SET approve=? WHERE employee_id = '"+empid+"';";
	try{
		PreparedStatement stmt = db.connect().prepareStatement(sql2);
	
		stmt.setBoolean(1,true);
		stmt.executeUpdate();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	String sql3 ="UPDATE public.pastresearch SET approve=? WHERE employee_id = '"+empid+"';";
	try{
		PreparedStatement stmt = db.connect().prepareStatement(sql3);
	
		stmt.setBoolean(1,true);
		stmt.executeUpdate();
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	
	return "Done";	
}
}


	 
