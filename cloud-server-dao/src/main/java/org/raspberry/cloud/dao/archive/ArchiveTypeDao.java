package org.raspberry.cloud.dao.archive;

import org.raspberry.cloud.model.archive.ArchiveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveTypeDao extends JpaRepository<ArchiveType, Long> {

	@Query("SELECT entity FROM ArchiveType entity WHERE entity.idType = :idType")
	public ArchiveType findOneById(@Param("idType") Long idType);

}