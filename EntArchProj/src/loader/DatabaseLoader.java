package loader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import objects.JobNode;
import util.DatabaseConnectionUtil;

public class DatabaseLoader {
	public void loadObjects(Vector<JobNode> jobs) throws Exception {
	    for(JobNode job : jobs)
	    {
	    	ResultSet rs = DatabaseConnectionUtil.stmt.executeQuery("SELECT * FROM JOB WHERE (JOBID = '" + job.getJobID() + "')");
	    	if(!rs.first())
	    		DatabaseConnectionUtil.stmt.execute("INSERT INTO JOB VALUES ( '" + job.getJobtitle() + "','" + job.getCompany() + "','" + job.getCity() + "','" + job.getState() + "','" + job.getCountry() + "','" + job.getSource() + "','" + job.getDate() + "','" + job.getDescription() + "','" + job.getUrl() + "','" + job.getLatitude() + "','" + job.getLongitude() + "','" + job.getJobID() + "','" + job.getWebSource() + "')");
	    }
	    DatabaseConnectionUtil.conn.commit();
	}

}
