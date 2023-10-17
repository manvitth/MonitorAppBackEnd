package com.vis.monitor.system.service;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.stereotype.Service;
import com.vis.monitor.system.modal.System;

@Service
public class SystemService {
    private final Sigar sigar = new Sigar();

    public System getSystemMetrics() throws SigarException {
        System metrics = new System();

        // For CPU
        CpuPerc cpuPerc = sigar.getCpuPerc();
        double cpuUsage = cpuPerc.getCombined() * 100.0;
        metrics.setCpuUsage(cpuUsage);

        // For RAM
        Mem mem = sigar.getMem();
        long totalRAM = mem.getTotal();
        long freeRAM = mem.getFree();
        long usedRAM = mem.getUsed();

        metrics.setTotalRAM(convertBytesToGB(totalRAM));
        metrics.setFreeRAM(convertBytesToGB(freeRAM));
        metrics.setUsedRAM(convertBytesToGB(usedRAM));

        // Fetch Disk metrics
        org.hyperic.sigar.FileSystemUsage fsUsage = sigar.getFileSystemUsage("/");
        long totalDiskMemory = fsUsage.getTotal();
        long usedDiskMemory = fsUsage.getUsed();

        metrics.setTotalDiskMemory(convertBytesToGB(totalDiskMemory));
        metrics.setUsedDiskMemory(convertBytesToGB(usedDiskMemory));

        return metrics;
    }

 
    private double convertBytesToGB(long bytes) {
        return (double) bytes / (1024 * 1024 * 1024);
    }
}

