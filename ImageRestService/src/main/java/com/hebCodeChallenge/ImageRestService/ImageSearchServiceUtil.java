package com.hebCodeChallenge.ImageRestService;

import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hebCodeChallenge.ImageRestService.dataBaseEntities.DetectedObject;
import com.hebCodeChallenge.ImageRestService.dataBaseEntities.ImageMetaData;

abstract class ImageSearchServiceUtil {

	static ImageMetaData parseJsonResponse(String jsonResponseString, ImageMetaData imageMD) {

		// return null if the response is null or a empty string
		if (jsonResponseString == null || jsonResponseString.isEmpty())
			return null;

		JSONObject jsonResponse = new JSONObject(jsonResponseString);

		String success = jsonResponse.getJSONObject("status").getString("type");

		if (!success.equalsIgnoreCase("success"))
			return null;

		JSONArray tags = jsonResponse.getJSONObject("result").getJSONArray("tags");

		for (Object tagObj : tags) {

			String uuid = UUID.randomUUID().toString().replaceAll("-", "");

			JSONObject tag = (JSONObject) tagObj;
			String objectName = tag.getJSONObject("tag").getString("en");
			double confidence = tag.getDouble("confidence");

			DetectedObject detObj = new DetectedObject();

			detObj.setId(uuid);
			detObj.setObjectName(objectName);
			detObj.setConfidence(confidence);

			imageMD.addDetectedObject(detObj);

		}

		return imageMD;
	}

}
