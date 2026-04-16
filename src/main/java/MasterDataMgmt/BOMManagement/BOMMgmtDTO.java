package MasterDataMgmt.BOMManagement;

import java.time.LocalDate;

public class BOMMgmtDTO {

    private int bom_id;
    public int getBOM_id() {
        return bom_id;
    }
    public void setBOM_id(int bom_id) {
        this.bom_id = bom_id;
    }

    private int item_id;        
    private String remark;
    private String use_yn;

    
    private String product_code;
    private String product_name;

    private int material_id;
    private String material_code;
    private String material_name;

    private double qty_required;
    private String unit;

   
    private java.time.LocalDate created_at;
    private java.time.LocalDate updated_at;

   

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUse_yn() {
        return use_yn;
    }

    public void setUse_yn(String use_yn) {
        this.use_yn = use_yn;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getMaterial_id() {
        return material_id;
    }

    public void setMaterial_id(int material_id) {
        this.material_id = material_id;
    }

    public String getMaterial_code() {
        return material_code;
    }

    public void setMaterial_code(String material_code) {
        this.material_code = material_code;
    }

    public String getMaterial_name() {
        return material_name;
    }

    public void setMaterial_name(String material_name) {
        this.material_name = material_name;
    }

    public double getQty_required() {
        return qty_required;
    }

    public void setQty_required(double qty_required) {
        this.qty_required = qty_required;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public java.time.LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(java.time.LocalDate created_at) {
        this.created_at = created_at;
    }

    public java.time.LocalDate getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(java.time.LocalDate updated_at) {
        this.updated_at = updated_at;
    }
}
