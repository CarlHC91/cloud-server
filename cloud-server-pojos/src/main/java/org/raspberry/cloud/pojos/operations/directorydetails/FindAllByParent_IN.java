package org.raspberry.cloud.pojos.operations.directorydetails;

import org.raspberry.cloud.pojos.entities.directory.DirectoryDetailsVO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class FindAllByParent_IN {

	@JsonProperty("parent_directory")
	private DirectoryDetailsVO parentDirectory;

	public DirectoryDetailsVO getParentDirectory() {
		return parentDirectory;
	}

	public void setParentDirectory(DirectoryDetailsVO parentDirectory) {
		this.parentDirectory = parentDirectory;
	}

}
