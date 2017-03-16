package com.jd.b2b.domain;

/**
 * @author cdtangxi
 * @Date 2017/1/11 17:07
 */
public class TaskResult {

    private String tip;

    private int totalCount;

    private int errorCount;

    private int dealCount;

    private boolean success;

    public TaskResult(){}

    public TaskResult(String tip, int totalCount, int errorCount, int dealCount, boolean success) {
        this.tip = tip;
        this.totalCount = totalCount;
        this.errorCount = errorCount;
        this.dealCount = dealCount;
        this.success = success;
    }

    public int getDealCount() {
        return dealCount;
    }

    public void setDealCount(int dealCount) {
        this.dealCount = dealCount;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
