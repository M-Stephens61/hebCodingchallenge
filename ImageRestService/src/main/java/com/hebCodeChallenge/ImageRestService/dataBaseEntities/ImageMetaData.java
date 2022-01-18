package com.hebCodeChallenge.ImageRestService.dataBaseEntities;

import java.util.ArrayList;
import java.util.List;

public class ImageMetaData {

	private String uploadId;

	private String qualifiedPath;

	private List<DetectedObject> detectedObjects;

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getQualifiedPath() {
		return qualifiedPath;
	}

	public void setQualifiedPath(String qualifiedPath) {
		this.qualifiedPath = qualifiedPath;
	}

	public List<DetectedObject> getDetectedObjects() {
		return new ArrayList<>(this.detectedObjects);
	}

	public void addDetectedObject(DetectedObject detectedObject) {
		if (this.detectedObjects == null)
			this.detectedObjects = new ArrayList<>();

		this.detectedObjects.add(detectedObject);
	}

	public void removeDetectedObject(String id) {
		if (this.detectedObjects == null)
			return;
		DetectedObject toRemove = null;
		for (DetectedObject obj : this.detectedObjects) {
			if (obj.getId().equals(id)) {
				toRemove = obj;
				break;
			}
		}

		this.detectedObjects.remove(toRemove);

	}

	@Override
	public String toString() {
		return "{Upload ID: " + uploadId + ",\r\n Qualified Name: " + qualifiedPath + ",\r\n Detected Objects: "
				+ detectedObjects + "\r\n}";
	}

}
