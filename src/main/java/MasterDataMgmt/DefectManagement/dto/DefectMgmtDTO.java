package MasterDataMgmt.DefectManagement.dto;

import java.time.LocalDate;

public class DefectMgmtDTO {

	private int defect_code_id;
    public int getDefect_code_id() { return defect_code_id; }
    public void setDefect_code_id(int defect_code_id) { this.defect_code_id = defect_code_id; }

    private String defect_code;
    public String getDefect_code() { return defect_code; }
    public void setDefect_code(String defect_code) { this.defect_code = defect_code; }

    private String defect_name;
    public String getDefect_name() { return defect_name; }
    public void setDefect_name(String defect_name) { this.defect_name = defect_name; }

    private String defect_type;
    public String getDefect_type() { return defect_type; }
    public void setDefect_type(String defect_type) { this.defect_type = defect_type; }

    private String description;
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    private String use_yn;
    public String getUse_yn() { return use_yn; }
    public void setUse_yn(String use_yn) { this.use_yn = use_yn; }

    private String remark;
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    private LocalDate created_at;
    public LocalDate getCreated_at() { return created_at; }
    public void setCreated_at(LocalDate created_at) { this.created_at = created_at; }

    private LocalDate updated_at;
    public LocalDate getUpdated_at() { return updated_at; }
    public void setUpdated_at(LocalDate updated_at) { this.updated_at = updated_at; }

}
