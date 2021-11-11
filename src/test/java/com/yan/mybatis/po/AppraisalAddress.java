package com.yan.mybatis.po;


import java.io.Serializable;

public class AppraisalAddress implements Serializable {
    private Integer id;

    private String appraisalname;

    /**
     * 别名
     *
     * @mbggenerated
     */
    private String alias;

    private String telephone;

    private String province;

    private String city;

    private String district;

    private String address;

    /**
     * 仓库类型，WarehouseTypeEnum
     *
     * @mbggenerated
     */
    private Integer warehouseType;

    private Boolean isdeleted;

    private Integer updated;

    private Integer created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppraisalname() {
        return appraisalname;
    }

    public void setAppraisalname(String appraisalname) {
        this.appraisalname = appraisalname;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getWarehouseType() {
        return warehouseType;
    }

    public void setWarehouseType(Integer warehouseType) {
        this.warehouseType = warehouseType;
    }

    public Boolean getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Boolean isdeleted) {
        this.isdeleted = isdeleted;
    }

    public Integer getUpdated() {
        return updated;
    }

    public void setUpdated(Integer updated) {
        this.updated = updated;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }
}