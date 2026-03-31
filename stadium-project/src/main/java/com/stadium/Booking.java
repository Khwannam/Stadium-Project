package com.stadium;

import java.security.SecureRandom;
import java.time.*;

public class Booking {

    private final String id;
    private final String field;
    private final String date;      // เปลี่ยนเป็น String เพื่อให้ตรงกับ StadiumGUI
    private final String startTime; // เปลี่ยนชื่อให้ตรงกับที่ StadiumGUI เรียกใช้
    private final String endTime;   // เปลี่ยนชื่อให้ตรงกับที่ StadiumGUI เรียกใช้
    private final double total;

    // แก้ไข Constructor ให้รับค่าตามที่ StadiumGUI ส่งมา (String, String, String, String, double)
    public Booking(String field, String date, String startTime, String endTime, double total) {
        this.id = generateId();
        this.field = field;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.total = total;
    }

    private String generateId() {
        SecureRandom r = new SecureRandom();
        return String.valueOf(100000 + r.nextInt(900000));
    }

    // Getter methods ที่ StadiumGUI เรียกใช้
    public String getId() { return id; }
    public String getField() { return field; }
    public String getDate() { return date; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public double calculatePrice() { return total; }
}
