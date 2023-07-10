package org.raspberry.cloud.pojos.operations.directorydetails;

import java.util.List;

import org.raspberry.cloud.pojos.entities.directory.DirectoryDetailsVO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class FindAllByParent_OUT {

	@JsonProperty("directory_details_list")
	private List<DirectoryDetailsVO> directoryDetailsList;

	public List<DirectoryDetailsVO> getDirectoryDetailsList() {
		return directoryDetailsList;
	}

	public void setDirectoryDetailsList(List<DirectoryDetailsVO> directoryDetailsList) {
		this.directoryDetailsList = directoryDetailsList;
	}

}
