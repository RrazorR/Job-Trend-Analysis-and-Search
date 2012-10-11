package ORM;

import java.util.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import objects.JobNode;
import util.DatabaseConnectionUtil;

public class JobMapper {
	
	public static JobNode search(String ID) throws Exception {
		JobNode job = null;
		ResultSet rs = DatabaseConnectionUtil.stmt.executeQuery("SELECT * FROM JOB WHERE (JOBID = '" + ID + "')");
		if(rs.first()) {
			job = new JobNode(ID, rs.getNString("WEBSOURCE"));
			job.setCity(rs.getNString("CITY"));
			job.setCompany(rs.getNString("COMPANY"));
			job.setJobtitle(rs.getNString("JobTITLE"));
			job.setState(rs.getNString("STATE"));
			job.setCountry(rs.getNString("COUNTRY"));
			job.setSource(rs.getNString("SOURCE"));
			SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
			Date date = formatter.parse(rs.getNString("DATE"));
			job.setDate(date);
			job.setDescription(rs.getNString("DESCRIPTION"));
			job.setUrl(rs.getNString("URL"));
			job.setLatitude(rs.getNString("LATITUDE"));
			job.setLongitude(rs.getNString("LONGITUDE"));
		}
		return job;
	}
	
	public static void Update(JobNode job) throws Exception {
		ResultSet rs = DatabaseConnectionUtil.stmt.executeQuery("SELECT * FROM JOB WHERE (JOBID = '" + job.getJobID() + "')");
		if(rs.first()) {
			rs.updateString(1, job.getJobtitle());
			rs.updateString(2, job.getCompany());
			rs.updateString(3, job.getCity());
			rs.updateString(4, job.getState());
			rs.updateString(5, job.getCountry());
			rs.updateString(6, job.getSource());
			rs.updateString(7, job.getDate().toString());
			rs.updateString(8, job.getDescription());
			rs.updateString(9, job.getUrl());
			rs.updateString(10, job.getLatitude());
			rs.updateString(11, job.getLongitude());
			rs.updateRow();
		}
	}
	
	public static void Insert(JobNode job) throws Exception {
		ResultSet rs = DatabaseConnectionUtil.stmt.executeQuery("SELECT * FROM JOB WHERE (JOBID = '" + job.getJobID() + "')");
    	if(!rs.first())
    		DatabaseConnectionUtil.stmt.execute("INSERT INTO JOB VALUES ( '" + job.getJobtitle() + "','" + job.getCompany() + "','" + job.getCity() + "','" + job.getState() + "','" + job.getCountry() + "','" + job.getSource() + "','" + job.getDate() + "','" + job.getDescription() + "','" + job.getUrl() + "','" + job.getLatitude() + "','" + job.getLongitude() + "','" + job.getJobID() + "','" + job.getWebSource() + "')");
	}
	
	public static void Delete(JobNode job) throws Exception {
		DatabaseConnectionUtil.stmt.executeUpdate("DELETE FROM JOB WHERE ID='"+ job.getJobID() +"'");
	}
}
