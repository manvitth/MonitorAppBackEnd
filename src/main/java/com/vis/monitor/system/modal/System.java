package com.vis.monitor.system.modal;

public class System {
    private double cpuUsage;
    private double totalRAM;
    private double freeRAM;
    private double usedRAM;
    private double totalDiskMemory;
    private double usedDiskMemory;

    public System() {
        super();
    }

    public System(double cpuUsage, double totalRAM, double freeRAM, double usedRAM, double totalDiskMemory, double usedDiskMemory) {
        super();
        this.cpuUsage = cpuUsage;
        this.totalRAM = totalRAM;
        this.freeRAM = freeRAM;
        this.usedRAM = usedRAM;
        this.totalDiskMemory = totalDiskMemory;
        this.usedDiskMemory = usedDiskMemory;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public double getTotalRAM() {
        return totalRAM;
    }

    public void setTotalRAM(double totalRAM) {
        this.totalRAM = totalRAM;
    }

    public double getFreeRAM() {
        return freeRAM;
    }

    public void setFreeRAM(double freeRAM) {
        this.freeRAM = freeRAM;
    }

    public double getUsedRAM() {
        return usedRAM;
    }

    public void setUsedRAM(double usedRAM) {
        this.usedRAM = usedRAM;
    }

    public double getTotalDiskMemory() {
        return totalDiskMemory;
    }

    public void setTotalDiskMemory(double totalDiskMemory) {
        this.totalDiskMemory = totalDiskMemory;
    }

    public double getUsedDiskMemory() {
        return usedDiskMemory;
    }

    public void setUsedDiskMemory(double usedDiskMemory) {
        this.usedDiskMemory = usedDiskMemory;
    }

    @Override
    public String toString() {
        return "System [cpuUsage=" + cpuUsage + ", totalRAM=" + totalRAM + ", freeRAM=" + freeRAM + ", usedRAM=" + usedRAM
                + ", totalDiskMemory=" + totalDiskMemory + ", usedDiskMemory=" + usedDiskMemory + "]";
    }
}
