package com.github.evitonative.simplecurrency.utils;

import com.github.evitonative.simplecurrency.SimpleCurrency;

import java.util.UUID;

public class CurrencyPlayer {
    private long amount;
    private UUID uuid;
    private String name;
    private double lastX = 0;
    private double lastY = 0;
    private double lastZ = 0;

    public CurrencyPlayer(long amount, UUID uuid) {
        this.amount = amount;
        this.name = SimpleCurrency.plugin.getServer().getPlayer(uuid).getName();
        this.uuid = uuid;
    }

    public CurrencyPlayer(long amount, UUID uuid, double lastX, double lastY, double lastZ) {
        this.amount = amount;
        this.name = SimpleCurrency.plugin.getServer().getPlayer(uuid).getName();
        this.uuid = uuid;
        this.lastX = lastX;
        this.lastY = lastY;
        this.lastZ = lastZ;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public double getLastX() {
        return lastX;
    }

    public void setLastX(double lastX) {
        this.lastX = lastX;
    }

    public double getLastY() {
        return lastY;
    }

    public void setLastY(double lastY) {
        this.lastY = lastY;
    }

    public double getLastZ() {
        return lastZ;
    }

    public void setLastZ(double lastZ) {
        this.lastZ = lastZ;
    }

    public double[] getLastXYZ(){
        return new double[]{lastX, lastY, lastZ};
    }

    public void setLastXYZ(double lastX, double lastY, double lastZ){
        this.lastX = lastX;
        this.lastY = lastY;
        this.lastZ = lastZ;
    }
}
