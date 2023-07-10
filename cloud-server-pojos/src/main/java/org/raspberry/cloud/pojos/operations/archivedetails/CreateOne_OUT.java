package org.raspberry.cloud.pojos.operations.archivedetails;

import org.raspberry.cloud.pojos.entities.archive.ArchiveDetailsVO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class CreateOne_OUT {

	@JsonProperty("archive_details")
	private ArchiveDetailsVO archiveDetails;

	public ArchiveDetailsVO getArchiveDetails() {
		return archiveDetails;
	}

	public void setArchiveDetails(ArchiveDetailsVO archiveDetails) {
		this.archiveDetails = archiveDetails;
	}

}
