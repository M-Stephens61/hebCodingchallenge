package com.hebCodeChallenge.ImageRestService.imagga;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.JSONObject;

public abstract class ImaggaController {

	public static String uploadImage(String filePath) {

		String uploadID = null;

		String credentialsToEncode = "acc_32750e785ffb0fc" + ":" + "e249545444dfc77727ee349bb6d01cad";
		String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

		// Change the file path here
		File fileToUpload = new File(filePath);

		String endpoint = "/uploads";

		String crlf = "\r\n";
		String twoHyphens = "--";
		String boundary = "Image Upload";

		URL urlObject;
		try {
			urlObject = new URL("https://api.imagga.com/v2" + endpoint);

			HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
			connection.setRequestProperty("Authorization", "Basic " + basicAuth);
			connection.setUseCaches(false);
			connection.setDoOutput(true);

			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Cache-Control", "no-cache");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			DataOutputStream request = new DataOutputStream(connection.getOutputStream());

			request.writeBytes(twoHyphens + boundary + crlf);
			request.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" + fileToUpload.getName()
					+ "\"" + crlf);
			request.writeBytes(crlf);

			InputStream inputStream = new FileInputStream(fileToUpload);
			int bytesRead;
			byte[] dataBuffer = new byte[1024];
			while ((bytesRead = inputStream.read(dataBuffer)) != -1) {
				request.write(dataBuffer, 0, bytesRead);
			}
			inputStream.close();

			request.writeBytes(crlf);
			request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
			request.flush();
			request.close();

			InputStream responseStream = new BufferedInputStream(connection.getInputStream());

			BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

			String line = "";
			StringBuilder stringBuilder = new StringBuilder();

			while ((line = responseStreamReader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
			responseStreamReader.close();

			String response = stringBuilder.toString();

			JSONObject jsonResponse = new JSONObject(response);
			uploadID = jsonResponse.getJSONObject("result").getString("upload_id");

			System.out.println(response);
			System.out.println("Upload ID is: " + uploadID);

			responseStream.close();
			connection.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return uploadID;
	}

	public static String scanImageByID(String uploadedID) {

		String jsonResponse = null;
		try {
			String credentialsToEncode = "acc_32750e785ffb0fc" + ":" + "e249545444dfc77727ee349bb6d01cad";
			String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

			String endpoint_url = "https://api.imagga.com/v2/tags";

			String url = endpoint_url + "?image_upload_id=" + uploadedID;
			URL urlObject = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

			connection.setRequestProperty("Authorization", "Basic " + basicAuth);

			int responseCode = connection.getResponseCode();

			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			jsonResponse = connectionInput.readLine();

			connectionInput.close();

			System.out.println(jsonResponse);
		} catch (Exception e) {

		}

		return jsonResponse;
	}
	
}
