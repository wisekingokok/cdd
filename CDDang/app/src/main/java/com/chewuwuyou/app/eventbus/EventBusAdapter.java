package com.chewuwuyou.app.eventbus;

import java.util.List;

import com.chewuwuyou.app.bean.BusinessServicePro;
import com.chewuwuyou.app.bean.TrafficBusinessListBook;

/**
 * 刷新适配器数据
 * @author liuchun
 *
 */
public class EventBusAdapter {
	
	public int position;//适配器下表
	public Object BusinessPersonalCenterCollection;//刷新違章，車輛，驾证服务适配器
	public Object PersonalCenterCollection;//刷新收藏適配器
    public int isPage;//判断是否在线支付设置支付密码
	public int portrait;//头像 1 代表从编辑资料中头像更改发送
	public List<BusinessServicePro> mListComment;
	public List<BusinessServicePro> mCommentsList;
	public List<BusinessServicePro> mBadList;
	public int index;//适配器下表
	public TrafficBusinessListBook  mBusinessListBook;//商家实体
	public String groupIndex;
	public String  groupEstablish;
	public String orderRemind;//订单提醒
	public String Eventimg;//图片放大关闭activtiy

	public String getEventimg() {
		return Eventimg;
	}

	public void setEventimg(String eventimg) {
		Eventimg = eventimg;
	}

	public String getOrderRemind() {
		return orderRemind;
	}

	public void setOrderRemind(String orderRemind) {
		this.orderRemind = orderRemind;
	}

	public String getGroupEstablish() {
		return groupEstablish;
	}

	public void setGroupEstablish(String groupEstablish) {
		this.groupEstablish = groupEstablish;
	}

	public String getGroupIndex() {
		return groupIndex;
	}

	public void setGroupIndex(String groupIndex) {
		this.groupIndex = groupIndex;
	}

	public TrafficBusinessListBook getmBusinessListBook() {
		return mBusinessListBook;
	}

	public void setmBusinessListBook(TrafficBusinessListBook mBusinessListBook) {
		this.mBusinessListBook = mBusinessListBook;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public List<BusinessServicePro> getmListComment() {
		return mListComment;
	}

	public void setmListComment(List<BusinessServicePro> mListComment) {
		this.mListComment = mListComment;
	}

	public List<BusinessServicePro> getmCommentsList() {
		return mCommentsList;
	}

	public void setmCommentsList(List<BusinessServicePro> mCommentsList) {
		this.mCommentsList = mCommentsList;
	}

	public List<BusinessServicePro> getmBadList() {
		return mBadList;
	}

	public void setmBadList(List<BusinessServicePro> mBadList) {
		this.mBadList = mBadList;
	}

	public int getPortrait() {
		return portrait;
	}

	public void setPortrait(int portrait) {
		this.portrait = portrait;
	}

	public EventBusAdapter() {
		
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Object getBusinessPersonalCenterCollection() {
		return BusinessPersonalCenterCollection;
	}

	public void setBusinessPersonalCenterCollection(
			Object businessPersonalCenterCollection) {
		BusinessPersonalCenterCollection = businessPersonalCenterCollection;
	}

	public Object getPersonalCenterCollection() {
		return PersonalCenterCollection;
	}

	public void setPersonalCenterCollection(Object personalCenterCollection) {
		PersonalCenterCollection = personalCenterCollection;
	}

	public int getIsPage() {
		return isPage;
	}

	public void setIsPage(int isPage) {
		this.isPage = isPage;
	}


}
