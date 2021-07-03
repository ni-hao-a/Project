package com.pc.service.mapper;

import com.pc.service.domain.DtsRole;
import com.pc.service.domain.DtsRoleExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DtsRoleMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     */
    long countByExample(DtsRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     */
    int deleteByExample(DtsRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     */
    int insert(DtsRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     */
    int insertSelective(DtsRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    DtsRole selectOneByExample(DtsRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    DtsRole selectOneByExampleSelective(@Param("example") DtsRoleExample example, @Param("selective") DtsRole.Column... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    List<DtsRole> selectByExampleSelective(@Param("example") DtsRoleExample example, @Param("selective") DtsRole.Column... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     */
    List<DtsRole> selectByExample(DtsRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    DtsRole selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") DtsRole.Column... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     */
    DtsRole selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    DtsRole selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") DtsRole record, @Param("example") DtsRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") DtsRole record, @Param("example") DtsRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(DtsRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(DtsRole record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int logicalDeleteByExample(@Param("example") DtsRoleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table dts_role
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int logicalDeleteByPrimaryKey(Integer id);
}