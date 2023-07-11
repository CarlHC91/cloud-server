package org.raspberry.cloud.dao.archive;

import java.util.List;

import org.raspberry.cloud.model.archive.ArchiveDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveDetailsDao extends JpaRepository<ArchiveDetails, Long> {

	@Query("SELECT entity FROM ArchiveDetails entity WHERE entity.idUser = :idUser AND entity.idArchive = :idArchive")
	public ArchiveDetails findOneById(@Param("idUser") Long idUser, @Param("idArchive") Long idArchive);

	@Query("SELECT entity FROM ArchiveDetails entity WHERE entity.idUser = :idUser")
	public List<ArchiveDetails> findAll(@Param("idUser") Long idUser);

	@Query("SELECT entity FROM ArchiveDetails entity WHERE entity.idUser = :idUser AND entity.idParent = :idParent")
	public List<ArchiveDetails> findAllByParent(@Param("idUser") Long idUser, @Param("idParent") Long idParent);

}