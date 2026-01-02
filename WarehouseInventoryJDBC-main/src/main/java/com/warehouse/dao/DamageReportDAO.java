package com.warehouse.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.warehouse.bean.DamageReport;

public class DamageReportDAO {
    public int generateDamageID(Connection con) throws Exception {
        String sql = "SELECT IFNULL(MAX(damage_id), 720000) + 1 FROM DAMAGE_REPORT_TBL";
        try (PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }

    public boolean recordDamageReport(Connection con, DamageReport d) throws Exception {
        String sql = "INSERT INTO DAMAGE_REPORT_TBL (damage_id, item_code, report_date, reported_by, reason, damaged_quantity, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, d.getDamageID());
            ps.setString(2, d.getItemCode());
            ps.setDate(3, new java.sql.Date(d.getReportDate().getTime()));
            ps.setString(4, d.getReportedBy());
            ps.setString(5, d.getReason());
            ps.setDouble(6, d.getDamagedQuantity());
            ps.setString(7, d.getStatus());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean updateDamageStatus(Connection con, int damageID, String status) throws Exception {
        String sql = "UPDATE DAMAGE_REPORT_TBL SET status = ? WHERE damage_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, damageID);
            return ps.executeUpdate() == 1;
        }
    }

    public List<DamageReport> findDamageReportsByItem(Connection con, String itemCode) throws Exception {
        List<DamageReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM DAMAGE_REPORT_TBL WHERE item_code = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, itemCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(mapResultSetToBean(rs));
                }
            }
        }
        return reports;
    }

    public List<DamageReport> findActiveDamageReportsByItem(Connection con, String itemCode) throws Exception {
        List<DamageReport> reports = new ArrayList<>();
        String sql = "SELECT * FROM DAMAGE_REPORT_TBL WHERE item_code = ? AND status IN ('OPEN', 'CONFIRMED')";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, itemCode);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reports.add(mapResultSetToBean(rs));
                }
            }
        }
        return reports;
    }

    public DamageReport findDamageReportById(Connection con, int damageID) throws Exception {
        String sql = "SELECT * FROM DAMAGE_REPORT_TBL WHERE damage_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, damageID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBean(rs);
                }
            }
        }
        return null;
    }

    private DamageReport mapResultSetToBean(ResultSet rs) throws SQLException {
        DamageReport d = new DamageReport();
        d.setDamageID(rs.getInt("damage_id"));
        d.setItemCode(rs.getString("item_code"));
        d.setReportDate(new java.util.Date(rs.getDate("report_date").getTime()));
        d.setReportedBy(rs.getString("reported_by"));
        d.setReason(rs.getString("reason"));
        d.setDamagedQuantity(rs.getDouble("damaged_quantity"));
        d.setStatus(rs.getString("status"));
        return d;
    }

    public boolean hasActiveReports(Connection con, String c) throws Exception {
        String sql = "SELECT COUNT(*) FROM DAMAGE_REPORT_TBL WHERE item_code = ? AND status IN ('OPEN', 'CONFIRMED')";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }
}