package com.ikhokha.techcheck;

public class Metric {

    private String metricName;

    private String metricContains;

    public String getMetricContains() {
        return metricContains;
    }

    public void setMetricContains(String metricContains) {
        this.metricContains = metricContains;
    }

    public Metric() {
    }

    public Metric(String metricName, String metricContains) {
        this.metricName = metricName;
        this.metricContains = metricContains;
    }

    public String getMetricName() {
        return metricName;
    }

    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }
}
