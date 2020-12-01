package sg.storage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import sg.storage.model.po.FileInfo;

import java.util.List;

public interface FileRepository extends JpaRepository<FileInfo, Long> {

    @Transactional
    @Modifying
    @Query("update FileInfo set folderId=:folderId where id=:id")
    int updateFileFolder(@Param("id") Long id, @Param("folderId") Long folderId);

    Page<FileInfo> findAll(Specification<FileInfo> spec, Pageable pageable);

    List<FileInfo> findByEtagAndBeOrigin(String etag, Integer beOrigin);

    List<FileInfo> findByFolderIdAndBeOrigin(Long folderId, Integer beOrigin);

    List<FileInfo> findByFolderIdAndBeOriginAndDel(Long folderId, Integer beOrigin, Integer del);

    List<FileInfo> findByEtagAndFileSuffixAndFileSizeType(String etag, String fileSuffix, String fileSizeType);
}