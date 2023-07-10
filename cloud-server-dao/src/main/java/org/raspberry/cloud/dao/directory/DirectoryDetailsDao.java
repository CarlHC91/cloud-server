package org.raspberry.cloud.dao.directory;

import java.util.List;

import org.raspberry.cloud.model.directory.DirectoryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectoryDetailsDao extends JpaRepository<DirectoryDetails, Long> {

	@Query("SELECT entity FROM DirectoryDetails entity WHERE entity.idDirectory = :idDirectory")
	public DirectoryDetails findOneById(@Param("idDirectory") Long idDirectory);

	@Query("SELECT entity FROM DirectoryDetails entity WHERE entity.filePath = :filePath")
	public DirectoryDetails findOneByFilePath(@Param("filePath") Long filePath);

	@Query("SELECT entity FROM DirectoryDetails entity WHERE entity.idParent = :idParent")
	public List<DirectoryDetails> findAllByParent(@Param("idParent") Long idParent);

}