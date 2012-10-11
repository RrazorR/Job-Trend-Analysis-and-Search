package loader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.xml.xpath.XPathExpressionException;

import objects.JobNode;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


import util.URLConnectionUtil;

public class IndeedJobObjectLoader {
	
	private static String location = "columbus";
	private static String jobtype = "fulltime";
	private static String companyid = "";
	private static String query = "";
	public static int start = 0;
	
	public static ArrayList<String> getJobIDList() throws XPathExpressionException{
		start = 0;
		ArrayList<String> jobIds = new ArrayList<String>();
		String queryURL = "http://api.indeed.com/ads/apisearch?publisher=1880023797625063&q=" + query + "&jcid=" + companyid + "&l=" + location + "&sort=&radius=100&st=&jt=" + jobtype + "&start=" + start + "&limit=100000&fromage=&filter=&latlong=1&co=us&chnl=&v=2";
		Document dom = null;
		try {
			dom = URLConnectionUtil.extractDomFromWebSource(queryURL, false);
		} catch (Exception e) {
			System.out.println("Problem reading from dom!");
			System.exit(1);
			//e.printStackTrace();
		}
		NodeList node = URLConnectionUtil.runXPathOverDOM(dom, "//totalresults");
		int count = Integer.parseInt(node.item(0).getFirstChild().getNodeValue());
		
		System.out.println("Found listings : " + count);
		
		while(start <= count){
			try {
				dom = URLConnectionUtil.extractDomFromWebSource(queryURL, false);
			} catch (Exception e) {
				System.out.println("Error in fetching at start = " + start);
				e.printStackTrace();
			}
			NodeList jobkeynode = URLConnectionUtil.runXPathOverDOM(dom, "//jobkey");
			for (int i = 0; i < jobkeynode.getLength(); i++) {
				jobIds.add(jobkeynode.item(i).getTextContent());
				//System.out.println("jobkey = " + jobkeynode.item(i).getTextContent());
			}
			start = start + 25;
			queryURL = "http://api.indeed.com/ads/apisearch?publisher=1880023797625063&q=" + query + "&jcid=" + companyid + "&l=" + location + "&sort=&radius=10000&st=&jt=" + jobtype + "&start=" + start + "&limit=10000&fromage=&filter=&latlong=1&co=us&chnl=&v=2";
		}
		return jobIds;
	}
	
	public static Vector<JobNode> createJobObjects(ArrayList<String> jobIds) throws XPathExpressionException, ParseException{
		
		int count = 0;
		Vector<JobNode> jobs = new Vector<JobNode>();
		for(String jobId : jobIds){
			System.out.println("Working on " + ++count + " of " + jobIds.size());
			String jobquery = "http://api.indeed.com/ads/apigetjobs?publisher=1880023797625063&jobkeys=" + jobId + "&v=2";
			Document dom = null;
			try {
				dom = URLConnectionUtil.extractDomFromWebSource(jobquery, false);
			} catch (Exception e) {
				System.out.println("Problem reading jobkey " + jobId + " . Moving on..");
				continue;
				//e.printStackTrace();
			}
			
			NodeList status = URLConnectionUtil.runXPathOverDOM(dom, "//expired");
			if(status.item(0).getTextContent().trim().equalsIgnoreCase("true")){
				System.out.println(jobId + " has expired. Moving to next..");
				continue;
			}
			
			
			JobNode jobObject = new JobNode(jobId.trim(), "INDEED");
			
			//Fetching job title
			NodeList titleNode = URLConnectionUtil.runXPathOverDOM(dom, "//jobtitle");
			jobObject.setJobtitle(titleNode.item(0).getTextContent().trim());
			
			//Fetching company
			NodeList companyNode = URLConnectionUtil.runXPathOverDOM(dom, "//company");
			jobObject.setCompany(companyNode.item(0).getTextContent().trim());

			//Fetching city
			NodeList cityNode = URLConnectionUtil.runXPathOverDOM(dom, "//city");
			jobObject.setCity(cityNode.item(0).getTextContent().trim());
			
			//Fetching state
			NodeList stateNode = URLConnectionUtil.runXPathOverDOM(dom, "//state");
			jobObject.setState(stateNode.item(0).getTextContent().trim());
			
			//Fetching country
			NodeList countryNode = URLConnectionUtil.runXPathOverDOM(dom, "//country");
			jobObject.setCountry(countryNode.item(0).getTextContent().trim());
			
			//Fetching source
			NodeList sourceNode = URLConnectionUtil.runXPathOverDOM(dom, "//source");
			jobObject.setSource(sourceNode.item(0).getTextContent().trim());
			
			//Fetching description
			NodeList snippetNode = URLConnectionUtil.runXPathOverDOM(dom, "//snippet");
			String description = "", descriptionModed = "";
			for (int i = 0; i < snippetNode.getLength(); i++) {
				description = description + " " + snippetNode.item(i).getTextContent().trim();
				descriptionModed = description.replaceAll("(\\r|\\n)", "");
			}
			jobObject.setDescription(descriptionModed);
			
			//Fetching url
			NodeList urlNode = URLConnectionUtil.runXPathOverDOM(dom, "//url");
			jobObject.setUrl(urlNode.item(0).getTextContent().trim());
			
			//Fetching latitude
			NodeList latitudeNode = URLConnectionUtil.runXPathOverDOM(dom, "//latitude");
			jobObject.setLatitude(latitudeNode.item(0).getTextContent().trim());
			
			//Fetching longitude
			NodeList longitudeNode = URLConnectionUtil.runXPathOverDOM(dom, "//longitude");
			jobObject.setLongitude(longitudeNode.item(0).getTextContent().trim());
			
			//Fetching date
			NodeList dateNode = URLConnectionUtil.runXPathOverDOM(dom, "//date");
			String dateString = dateNode.item(0).getTextContent().trim();
			SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
			Date date = formatter.parse(dateString);
			jobObject.setDate(date);
			
			//System.out.print("Job created successfully");
			jobs.add(jobObject);

		}
		return jobs;
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		//ArrayList<String> jobIds = getJobIDList();
		
		//For testing only
		ArrayList<String> jobIds = new ArrayList<String>();
		jobIds.add("5e3ad211bbf793d3");
		
		Vector<JobNode> jobs = createJobObjects(jobIds);
		
		DatabaseLoader loader = new DatabaseLoader();
		loader.loadObjects(jobs);

	}

}
