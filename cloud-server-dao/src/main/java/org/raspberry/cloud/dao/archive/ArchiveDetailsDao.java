package org.raspberry.cloud.dao.archive;

import java.util.List;

import org.raspberry.cloud.model.archive.ArchiveDetails;
import org.raspberry.cloud.model.archive.ArchiveDetailsPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveDetailsDao extends JpaRepository<ArchiveDetails, ArchiveDetailsPK> {

	@Query("SELECT entity FROM ArchiveDetails entity WHERE entity.idUser = :idUser AND entity.idArchive = :idArchive")
	public ArchiveDetails findOneById(@Param("idUser") Long idUser, @Param("idArchive") Long idArchive);

	@Query("SELECT MAX(entity.idArchive) FROM ArchiveDetails entity WHERE entity.idUser = :idUser")
	public Long getMaxIdArchive(@Param("idUser") Long idUser);

	@Query("SELECT entity FROM ArchiveDetails entity WHERE entity.idUser = :idUser AND entity.idParent = :idParent")
	public List<ArchiveDetails> findAllByParent(@Param("idUser") Long idUser, @Param("idParent") Long idParent);

}