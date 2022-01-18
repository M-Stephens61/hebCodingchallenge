package com.hebCodeChallenge.ImageRestService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.annotation.MultipartConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.hebCodeChallenge.ImageRestService.dataBaseEntities.DataBaseManager;
import com.hebCodeChallenge.ImageRestService.dataBaseEntities.DetectedObject;
import com.hebCodeChallenge.ImageRestService.dataBaseEntities.ImageMetaData;
import com.hebCodeChallenge.ImageRestService.imagga.ImaggaController;

@Path("image-service")
@MultipartConfig
public class ImageSearchService {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String gettingStarted() {


		System.out.println("Testing getById()...");
		ImageMetaData uploadedData = DataBaseManager.getImageDataById("i03425c9f2ad3dcbc29b5765b7s4ec3i");
		System.out.println(uploadedData);

		System.out.println("Testing remove...");
		DataBaseManager.removeImageData(uploadedData);

		return "<h1>Successful Tests</h1>";
	}

	@GET
	@Path("images/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ImageMetaData getImageByID(@PathParam("id") String imageID) {

		ImageMetaData imageData = null;

		imageData = DataBaseManager.getImageDataById(imageID);

		return imageData;
	}

	@GET
	@Path("images")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ImageMetaData> getImageByContent(@QueryParam("searchedObjects") List<String> searchedObjects) {

		List<ImageMetaData> responseImages = null;

		responseImages = DataBaseManager.quearyByDetectedObjects(searchedObjects);

		return responseImages;
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("images")
	public void uploadImage(@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		System.out.println("Working");
		String fileName = fileDetail.getFileName();
		System.out.println(fileName);

		String uploadedFileLocation = "C:\\Users\\mgstephens\\Development\\REST_API\\ImageRestService\\uploadedImages\\"
				+ fileName.substring(fileName.indexOf(':') + 1);

		writeToFile(uploadedInputStream, uploadedFileLocation);

		String uploadID = ImaggaController.uploadImage(uploadedFileLocation);

		String jsonResponse = ImaggaController.scanImageByID(uploadID);

		ImageMetaData imageMD = new ImageMetaData();

		System.out.println("Upload ID: " + uploadID + ", filePath: " + uploadedFileLocation);
		imageMD.setUploadId(uploadID);
		imageMD.setQualifiedPath(uploadedFileLocation);

		imageMD = ImageSearchServiceUtil.parseJsonResponse(jsonResponse, imageMD);

		if (imageMD != null)
			DataBaseManager.saveImageData(imageMD);
	}

	private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) {
		System.out.println();
		System.out.println("writing file " + uploadedFileLocation);

		try {
			int read = 0;
			byte[] bytes = new byte[1024];

			File outFile = new File(uploadedFileLocation);
			OutputStream out = new FileOutputStream(outFile);

			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			System.out.println("Absolute path " + outFile.getAbsolutePath());
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
		System.out.println("Completed writing");
		System.out.println();
	}

}
