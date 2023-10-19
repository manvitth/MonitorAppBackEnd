package com.vis.monitor.modal;

import lombok.Data;

@Data
public class SystemInformation {

	private Double cpuUsage;
	private DiskInformation diskInfo;
	private MemoryInformation memoryInfo;
}
