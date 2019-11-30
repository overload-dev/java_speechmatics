package com.overload.ex;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SpeechmaticsService {
	private static String USER_ID = "72944";
	private static String TOKEN = "ZGMxZDcxZmItMTk1MS00NzczLTg3ZTktNTY2ZTk3NmY0MWIy";
	private static String UPLOADURI = "https://api.speechmatics.com/v1.0/user/%s/jobs/";
	private static String GETTRANSCRIPTURL = "https://api.speechmatics.com/v1.0/user/%s/jobs/%s/transcript?auth_token=%s";
	private static String GETJOBIDURL = "https://api.speechmatics.com/v1.0/user/%s/jobs/?auth_token=%s";

	public String uploadAudio(MultipartFile file) {
		String res = null;

		try {
			URI uri = new URIBuilder(String.format(UPLOADURI, USER_ID)).addParameter("auth_token", TOKEN).build();

			CloseableHttpClient httpclient = HttpClients.createDefault();

			HttpPost httppost = new HttpPost(uri);
			httppost.setHeader("Accept", "application/json");

			HttpEntity entity = MultipartEntityBuilder.create().addTextBody("model", "en-US")
					.addTextBody("notification", "none")
					.addBinaryBody("data_file", file.getBytes(), ContentType.DEFAULT_BINARY, file.getName()).build();

			httppost.setEntity(entity);

			// System.out.println(entity.getContent());
			System.out.println("Executing request " + httppost.getRequestLine());

			HttpResponse response = httpclient.execute(httppost);
			res = response.toString();
			httpclient.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return res;
	}

	public String getlastestJobID() {
		String jobId = null;

		BufferedReader br = null;

		try {
			URL url = new URL(String.format(GETJOBIDURL, USER_ID, TOKEN));
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

			conn.setRequestMethod("GET");

			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			StringBuffer sb = new StringBuffer();

			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
			
			//jobId = sb.toString();
			
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(sb.toString()); //parse data...
			JSONArray ja = (JSONArray) json.get("jobs");// get jobs field
			
			JSONObject tmp = (JSONObject) ja.get(0);// get 0 index
			jobId = tmp.get("id").toString();// get id field value at 0 index
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jobId;
	}

	public String getTranscriptByJobID(String jobId) {
		System.out.println("getTranscriptByJobID");

		String transcript = null;

		BufferedReader br = null;

		try {

			URL url = new URL(String.format(GETTRANSCRIPTURL, USER_ID, jobId, TOKEN));

			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			StringBuffer sb = new StringBuffer();

			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}

			transcript = sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return transcript;
	}
}
