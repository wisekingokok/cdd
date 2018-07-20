
package com.chewuwuyou.app.bean;

import java.io.Serializable;

/**
 * @describe:爱车账目实体类
 * @author:yuyong
 * @version 1.1.0
 * @created:2014-10-23下午3:45:37
 */
public class InputOrderType implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
    private String type;// 类型
    private boolean isSelected;// 时间
    
    
    
	public InputOrderType(int id, String type, boolean isSelected) {
		super();
		this.id = id;
		this.type = type;
		this.isSelected = isSelected;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
   

}
