package com.hebCodeChallenge.ImageRestService.dataBaseEntities;

public class DetectedObject {

	private String id;

	private String objectName;

	private double confidence;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	
	@Override
	public String toString() {
		return "{\r\nid: " + id + ",\r\n object name: " + objectName + ",\r\n confidence: " + confidence + "\r\n}";
	}
}
