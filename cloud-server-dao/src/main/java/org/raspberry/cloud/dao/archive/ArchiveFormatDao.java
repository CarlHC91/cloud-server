package org.raspberry.cloud.dao.archive;

import org.raspberry.cloud.model.archive.ArchiveFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveFormatDao extends JpaRepository<ArchiveFormat, Long> {

	@Query("SELECT entity FROM ArchiveFormat entity WHERE entity.idFormat = :idFormat")
	public ArchiveFormat findOneById(@Param("idFormat") Long idFormat);

}