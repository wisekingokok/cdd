package com.chewuwuyou.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/3 0003.
 */
public class OrderStateInfo implements Serializable {

    /**
     * fdCount : 3
     * wdCount : 2
     * taskCounts : [{"td":1,"date":"20160802","fd":3,"wd":2,"cd":0}]
     * cdAvg : 0
     * wdAvg : 0
     * tdAvg : 0
     * tdCount : 1
     * fdAvg : 0
     * cdCount : 0
     */

    private int fdCount;
    private int wdCount;
    private int cdAvg;
    private int wdAvg;
    private int tdAvg;
    private int tdCount;
    private int fdAvg;
    private int cdCount;
    /**
     * td : 1
     * date : 20160802
     * fd : 3
     * wd : 2
     * cd : 0
     */

    private List<TaskCountsBean> taskCounts;

    public int getFdCount() {
        return fdCount;
    }

    public void setFdCount(int fdCount) {
        this.fdCount = fdCount;
    }

    public int getWdCount() {
        return wdCount;
    }

    public void setWdCount(int wdCount) {
        this.wdCount = wdCount;
    }

    public int getCdAvg() {
        return cdAvg;
    }

    public void setCdAvg(int cdAvg) {
        this.cdAvg = cdAvg;
    }

    public int getWdAvg() {
        return wdAvg;
    }

    public void setWdAvg(int wdAvg) {
        this.wdAvg = wdAvg;
    }

    public int getTdAvg() {
        return tdAvg;
    }

    public void setTdAvg(int tdAvg) {
        this.tdAvg = tdAvg;
    }

    public int getTdCount() {
        return tdCount;
    }

    public void setTdCount(int tdCount) {
        this.tdCount = tdCount;
    }

    public int getFdAvg() {
        return fdAvg;
    }

    public void setFdAvg(int fdAvg) {
        this.fdAvg = fdAvg;
    }

    public int getCdCount() {
        return cdCount;
    }

    public void setCdCount(int cdCount) {
        this.cdCount = cdCount;
    }

    public List<TaskCountsBean> getTaskCounts() {
        return taskCounts;
    }

    public void setTaskCounts(List<TaskCountsBean> taskCounts) {
        this.taskCounts = taskCounts;
    }

    public static class TaskCountsBean implements  Serializable{
        private int td;
        private String date;
        private int fd;
        private int wd;
        private int cd;
        private int sd;

        public int getSd() {
            return sd;
        }

        public void setSd(int sd) {
            this.sd = sd;
        }

        public int getTd() {
            return td;
        }

        public void setTd(int td) {
            this.td = td;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getFd() {
            return fd;
        }

        public void setFd(int fd) {
            this.fd = fd;
        }

        public int getWd() {
            return wd;
        }

        public void setWd(int wd) {
            this.wd = wd;
        }

        public int getCd() {
            return cd;
        }

        public void setCd(int cd) {
            this.cd = cd;
        }
    }
}
