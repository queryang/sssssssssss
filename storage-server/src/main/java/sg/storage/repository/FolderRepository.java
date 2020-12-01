package sg.storage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import sg.storage.model.po.FolderInfo;
import java.util.List;

public interface FolderRepository extends JpaRepository<FolderInfo, Long> {

    /**
     * 修改文件夹名称
     *
     * @param id
     * @param name
     * @return
     */
    @Transactional
    @Modifying
    @Query("update FolderInfo set name=:name where id=:id")
    int updateName(@Param("id") Long id, @Param("name") String name);

    /**
     * 修改文件夹类型
     *
     * @param id
     * @param type
     * @return
     */
    @Transactional
    @Modifying
    @Query("update FolderInfo set type=:type where id=:id")
    int updateType(@Param("id") Long id, @Param("type") Integer type);

    /**
     * 修改备注
     *
     * @param id
     * @param remark
     * @return
     */
    @Transactional
    @Modifying
    @Query("update FolderInfo set remark=:remark where id=:id")
    int updateRemark(@Param("id") Long id, @Param("remark") String remark);

    /**
     * 修改父文件夹id
     *
     * @param id
     * @param pid
     */
    @Transactional
    @Modifying
    @Query("update FolderInfo set pid=:pid where id=:id")
    int updatePid(@Param("id") Long id, @Param("pid") Long pid);

    List<FolderInfo> findByPid(Long pid);

    List<FolderInfo> findByPidAndDel(Long pid, Integer del);

    List<FolderInfo> findByAppKeyAndOwnIdAndPidAndDel(String appKey, String ownId, Long pid, Integer del);

    List<FolderInfo> findByAppKeyAndBeShareAndDel(String appKey, Integer beShare, Integer del);

    List<FolderInfo> findByPidAndBeShareAndDel(Long pid, Integer beShare, Integer del);
}