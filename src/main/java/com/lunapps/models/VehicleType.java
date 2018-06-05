package com.lunapps.models;

import lombok.Getter;

import java.util.Objects;


@Getter
public enum VehicleType {
    MOTORCYCLE("MOTORCYCLE"),
    COMPACT("COMPACT"),
    SEDAN("SEDAN"),
    STATION_WAGON("STATION_WAGON"),
    COUPE("COUPE"),
    OFF_ROAD("OFF_ROAD"),
    VAN("VAN"),
    TRUCK("TRUCK"),
    BUS("BUS"),
    TRANSPORTER("TRANSPORTER"),
    BIG_TRUCK("BIG_TRUCK");

    private final String type;

    VehicleType(String type) {
        this.type = type;
    }

    public static VehicleType getByVehicleType(String vehicleType) {
        for (VehicleType vType : VehicleType.values()) {
            if (Objects.equals(vType.getType(), vehicleType)) {
                return vType;
            }
        }
        return null;
    }
}