package org.raspberry.cloud.pojos.operations.directorydetails;

import org.raspberry.cloud.pojos.entities.directory.DirectoryDetailsVO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class CreateOne_OUT {

	@JsonProperty("directory_details")
	private DirectoryDetailsVO directoryDetails;

	public DirectoryDetailsVO getDirectoryDetails() {
		return directoryDetails;
	}

	public void setDirectoryDetails(DirectoryDetailsVO directoryDetails) {
		this.directoryDetails = directoryDetails;
	}

}
