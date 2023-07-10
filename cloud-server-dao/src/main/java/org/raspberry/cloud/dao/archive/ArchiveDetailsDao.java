package org.raspberry.cloud.dao.archive;

import java.util.List;

import org.raspberry.cloud.model.archive.ArchiveDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveDetailsDao extends JpaRepository<ArchiveDetails, Long> {

	@Query("SELECT entity FROM ArchiveDetails entity WHERE entity.idArchive = :idArchive")
	public ArchiveDetails findOneById(@Param("idArchive") Long idArchive);

	@Query("SELECT entity FROM ArchiveDetails entity WHERE entity.filePath = :filePath")
	public ArchiveDetails findOneByFilePath(@Param("filePath") Long filePath);

	@Query("SELECT entity FROM ArchiveDetails entity WHERE entity.idParent = :idParent")
	public List<ArchiveDetails> findAllByParent(@Param("idParent") Long idParent);

}