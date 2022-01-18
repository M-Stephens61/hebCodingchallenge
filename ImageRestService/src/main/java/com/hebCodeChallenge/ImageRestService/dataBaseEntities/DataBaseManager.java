package com.hebCodeChallenge.ImageRestService.dataBaseEntities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hebCodeChallenge.ImageRestService.jdbc.JDBCController;

public abstract class DataBaseManager {

	public static void saveImageData(ImageMetaData imageMetaData) {

		Connection connection = JDBCController.getConnection();

		try {
			StringBuilder strBuilder = new StringBuilder();

			strBuilder.append("INSERT INTO \"imageSearch\".\"imageMetaData\" VALUES(");

			String imageUploadId = imageMetaData.getUploadId();

			strBuilder.append("'" + imageUploadId + "', ");
			strBuilder.append("'" + imageMetaData.getQualifiedPath() + "');");

			Statement statement = connection.createStatement();
			statement.execute(strBuilder.toString());

			for (DetectedObject detObj : imageMetaData.getDetectedObjects()) {
				strBuilder = new StringBuilder();

				strBuilder.append("INSERT INTO \"imageSearch\".\"detectedObjects\" VALUES(");

				strBuilder.append("'" + detObj.getId() + "', ");
				strBuilder.append("'" + detObj.getObjectName() + "', ");
				strBuilder.append("'" + detObj.getConfidence() + "', ");
				strBuilder.append("'" + imageUploadId + "');");

				statement = connection.createStatement();
				statement.execute(strBuilder.toString());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		JDBCController.closeConnection();
	}

	public static void removeImageData(ImageMetaData imageMetaData) {

		Connection connection = JDBCController.getConnection();

		try {

			String imageUploadId = imageMetaData.getUploadId();

			String deleteQuery = "DELETE FROM \"imageSearch\".\"detectedObjects\" WHERE \"imageUploadId\" = '"
					+ imageUploadId + "';";

			PreparedStatement statement = connection.prepareStatement(deleteQuery);
			statement.execute();

			deleteQuery = "DELETE FROM \"imageSearch\".\"imageMetaData\" WHERE \"uploadId\" = '" + imageUploadId + "';";

			statement = connection.prepareStatement(deleteQuery);
			statement.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		JDBCController.closeConnection();
	}

	public static ImageMetaData getImageDataById(String uploadID) {

		Connection connection = JDBCController.getConnection();

		ImageMetaData loadedData = getImageDataById(uploadID, connection);

		JDBCController.closeConnection();

		return loadedData;

	}

	private static ImageMetaData getImageDataById(String uploadID, Connection connection) {

		ImageMetaData loadedData = null;

		String selectQuery = "SELECT * FROM \"imageSearch\".\"imageMetaData\" WHERE \"uploadId\" = '" + uploadID + "';";

		try {
			PreparedStatement statement = connection.prepareStatement(selectQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet results = statement.executeQuery();

			// there are no results
			if (!results.next()) {
				return null;
			}

			results.first();

			loadedData = new ImageMetaData();

			loadedData.setUploadId(results.getString("uploadId"));
			loadedData.setQualifiedPath(results.getString("qualifiedPath"));

			selectQuery = "SELECT * FROM \"imageSearch\".\"detectedObjects\" WHERE \"imageUploadId\" = '" + uploadID
					+ "';";
			statement = connection.prepareStatement(selectQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			results = statement.executeQuery();

			// there are no results
			if (results.next()) {
				results.first(); // reset

				do {

					DetectedObject detObj = new DetectedObject();

					detObj.setId(results.getString("id"));
					detObj.setObjectName(results.getString("objectName"));
					detObj.setConfidence(results.getDouble("confidence"));

					loadedData.addDetectedObject(detObj);

				} while (results.next());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return loadedData;
	}

	public static List<ImageMetaData> quearyByDetectedObjects(List<String> searchedObjects) {

		List<ImageMetaData> imageResults = new ArrayList<>();

		// return if nothing was searched
		if (searchedObjects.size() <= 0) {
			return null;
		}

		Connection connection = JDBCController.getConnection();

		List<String> uniqueImageUploadIDs = getUniqueMatchingImageIDs(searchedObjects, connection);

		for (String uploadID : uniqueImageUploadIDs) {

			imageResults.add(getImageDataById(uploadID, connection));

		}

		JDBCController.closeConnection();

		return imageResults;
	}

	private static List<String> getUniqueMatchingImageIDs(List<String> searchedObjects, Connection connection) {

		Map<String, List<String>> matchingUploadIDs = new HashMap<>();

		for (String searchedObj : searchedObjects) {
			String selectQuery = "SELECT * FROM \"imageSearch\".\"detectedObjects\" WHERE \"objectName\" LIKE '\"%"
					+ searchedObj + "%'";
			try {
				PreparedStatement statement = connection.prepareStatement(selectQuery, ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				ResultSet results = statement.executeQuery();

				// there are no results
				if (results.next()) {
					results.first(); // reset

					List<String> mathcingIDs = new ArrayList<>();

					do {

						mathcingIDs.add(results.getString("imageUploadId"));

					} while (results.next());

					matchingUploadIDs.put(searchedObj, mathcingIDs);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		List<String> passingImages = new ArrayList<>(matchingUploadIDs.get(searchedObjects.get(0)));

		for (List<String> imagesWithObject : matchingUploadIDs.values()) {

			List<String> tempPassingImgs = new ArrayList<>();

			for (String uploadID : passingImages) {

				if (imagesWithObject.contains(uploadID)) {
					tempPassingImgs.add(uploadID);
				}
			}

			passingImages = tempPassingImgs;
		}

		return passingImages;
	}

}
