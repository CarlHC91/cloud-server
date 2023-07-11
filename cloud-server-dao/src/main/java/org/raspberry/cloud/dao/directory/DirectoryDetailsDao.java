package org.raspberry.cloud.dao.directory;

import java.util.List;

import org.raspberry.cloud.model.directory.DirectoryDetails;
import org.raspberry.cloud.model.directory.DirectoryDetailsPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectoryDetailsDao extends JpaRepository<DirectoryDetails, DirectoryDetailsPK> {

	@Query("SELECT entity FROM DirectoryDetails entity WHERE entity.idUser = :idUser AND entity.idDirectory = :idDirectory")
	public DirectoryDetails findOneById(@Param("idUser") Long idUser, @Param("idDirectory") Long idDirectory);

	@Query("SELECT entity FROM DirectoryDetails entity WHERE entity.idUser = :idUser")
	public List<DirectoryDetails> findAll(@Param("idUser") Long idUser);

	@Query("SELECT entity FROM DirectoryDetails entity WHERE entity.idUser = :idUser AND entity.idParent = :idParent")
	public List<DirectoryDetails> findAllByParent(@Param("idUser") Long idUser, @Param("idParent") Long idParent);

}