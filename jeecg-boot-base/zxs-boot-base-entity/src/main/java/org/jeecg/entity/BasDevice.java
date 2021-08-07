package org.jeecg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecg.entity.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 设备表
 * <p>
 * 
 * @Author Steve
 * @Since  2019-01-22
 */
@Data
@TableName("bas_device")
public class BasDevice implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**ID*/
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;
	/**父机构ID*/
	private Long projectId;
	/**机构/设备名称*/
	private String clientId;
	private String deviceSn;
	/**机构/设备名称*/
	private String deviceCode;
	private String deviceName;
	private String deviceType;
	/**英文名*/
	private String devManufacturer;
	/**缩写*/
	private String deviceModel;
	private String password;
	private String ip;
	private Integer port;
	private String mac;
	private String deviceToken;
	private String version;
	private String mask;
	private String getway;
	private String longitude;
	private String latitude;
	private String source;
	private String position;
	private Double alarmTemperature;
	private String status;
	/**删除状态（0，正常，1已删除）*/
	@Dict(dicCode = "del_flag")
	private String delFlag;
	/**创建人*/
	private String createBy;
	/**创建日期*/
//	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	/**更新人*/
	private String updateBy;
	/**更新日期*/
//	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

}
