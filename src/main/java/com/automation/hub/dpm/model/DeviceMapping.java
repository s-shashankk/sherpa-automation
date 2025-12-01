package com.automation.hub.dpm.model;
import java.util.List;
public class DeviceMapping {
	private String dpm;
    private List<String> deviceIds;

    public DeviceMapping(String dpm, List<String> deviceIds) {
        this.dpm = dpm;
        this.deviceIds = deviceIds;
    }

    public String getDpm() {
        return dpm;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }
}
