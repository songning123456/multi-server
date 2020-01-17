package com.simple.blog.repository;

import com.simple.blog.entity.PersonalInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by songning on 2019/8/25 2:47 PM
 */
@Repository
public interface PersonalInformationRepository extends JpaRepository<PersonalInformation, String> {

    /**
     * @param userId
     * @return
     */
    @Query(value = "select distinct(info_type) from personal_information where user_id = ?1", nativeQuery = true)
    List<String> findInfoTypeByInfoOwnerNative(String userId);

    @Query(value = "select mechanism, position, start_time as startTime, end_time  as endTime, introduction from personal_information where user_id = ?1 and info_type=?2", nativeQuery = true)
    List<Map<String, Object>> findByUserIdAndInfoTypeNative(String userId, String infoType);

    @Query(value = "select id as infoId, mechanism, position,info_type as infoType, start_time as startTime, end_time  as endTime, introduction from personal_information where username = ?1 order by start_time", nativeQuery = true)
    List<Map<String, Object>> findByUsernameNative(String username);

    @Modifying
    @Transactional
    @Query(value = "update personal_information set end_time=:#{#entity.endTime}, start_time=:#{#entity.startTime}," +
            "info_type=:#{#entity.infoType}, introduction=:#{#entity.introduction}, mechanism=:#{#entity.mechanism}, position=:#{#entity.position} " +
            " where id=:#{#entity.id}", nativeQuery = true)
    void updateNative(@Param("entity") PersonalInformation personalInformation);
}
