package com.myapp.azureblob;

import java.io.IOException;
import java.time.OffsetDateTime;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;

@RestController
public class AzureBlobController {

	@PostMapping("/upload")
	public String uploadFile(@RequestParam(value = "file") MultipartFile file) throws IOException {

		String str = "DefaultEndpointsProtocol=https;AccountName=kvpstorageaccount;AccountKey=1Lj/Uq5cMD7eNMIh9R0bRe/ycNFqDpvJTeIw1plVcsW3Z9Mps/kO8XFJXhYri5cpxJcvH7yB+HTr+AStI9yRXw==;EndpointSuffix=core.windows.net";

		OffsetDateTime expiryTime = OffsetDateTime.now().plusDays(1);
		BlobSasPermission permission = new BlobSasPermission().setReadPermission(true);
		BlobServiceSasSignatureValues values = new BlobServiceSasSignatureValues(expiryTime, permission)
				.setStartTime(OffsetDateTime.now());
		BlobContainerClient container = new BlobContainerClientBuilder().connectionString(str)
				.containerName("pravuworkitem").buildClient();

		BlobClient blob = container.getBlobClient(file.getOriginalFilename());
		blob.upload(file.getInputStream(), file.getSize(), true);
		String sasToken = blob.generateSas(values);

		return blob.getBlobUrl() + "?" + sasToken;
	}

}