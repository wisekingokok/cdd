package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/3 0003.
 */
public class InOutInfo implements Serializable {


    /**
     * incomeAll : 0
     * yye : -62
     * inOutData : [{"income":246,"pay":50,"date":"20160728"},{"income":50,"pay":0,"date":"20160729"},{"income":0,"pay":62,"date":"20160802"}]
     * sdzb : 0
     * fdAll : 2
     * fdzb : 1
     * sdAll : 0
     * payAll : 62
     */

    private double incomeAll;
    private double yye;
    private int sdzb;
    private int fdAll;
    private int fdzb;
    private int sdAll;
    private double payAll;
    /**
     * income : 246
     * pay : 50
     * date : 20160728
     */

    private List<InOutDataBean> inOutData;

    public double getIncomeAll() {
        return incomeAll;
    }

    public void setIncomeAll(double incomeAll) {
        this.incomeAll = incomeAll;
    }

    public double getYye() {
        return yye;
    }

    public void setYye(double yye) {
        this.yye = yye;
    }

    public int getSdzb() {
        return sdzb;
    }

    public void setSdzb(int sdzb) {
        this.sdzb = sdzb;
    }

    public int getFdAll() {
        return fdAll;
    }

    public void setFdAll(int fdAll) {
        this.fdAll = fdAll;
    }

    public int getFdzb() {
        return fdzb;
    }

    public void setFdzb(int fdzb) {
        this.fdzb = fdzb;
    }

    public int getSdAll() {
        return sdAll;
    }

    public void setSdAll(int sdAll) {
        this.sdAll = sdAll;
    }

    public double getPayAll() {
        return payAll;
    }

    public void setPayAll(double payAll) {
        this.payAll = payAll;
    }

    public List<InOutDataBean> getInOutData() {
        return inOutData;
    }

    public void setInOutData(List<InOutDataBean> inOutData) {
        this.inOutData = inOutData;
    }

    public static class InOutDataBean implements Serializable{
        private float income;
        private float pay;
        private String date;

        public float getIncome() {
            return income;
        }

        public void setIncome(float income) {
            this.income = income;
        }

        public float getPay() {
            return pay;
        }

        public void setPay(float pay) {
            this.pay = pay;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
