package org.raspberry.cloud.pojos.operations.archivedetails;

import java.util.List;

import org.raspberry.cloud.pojos.entities.archive.ArchiveDetailsVO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class FindAllByParent_OUT {

	@JsonProperty("archive_details_list")
	private List<ArchiveDetailsVO> archiveDetailsList;

	public List<ArchiveDetailsVO> getArchiveDetailsList() {
		return archiveDetailsList;
	}

	public void setArchiveDetailsList(List<ArchiveDetailsVO> archiveDetailsList) {
		this.archiveDetailsList = archiveDetailsList;
	}

}
