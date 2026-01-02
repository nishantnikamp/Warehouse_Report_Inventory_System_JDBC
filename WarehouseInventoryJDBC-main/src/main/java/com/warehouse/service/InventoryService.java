package com.warehouse.service;

import java.sql.Connection;

import java.util.Date;
import java.util.List;
import com.warehouse.bean.*;
import com.warehouse.dao.*;
import com.warehouse.util.*;

public class InventoryService {
    ItemDAO iDAO = new ItemDAO();
    DamageReportDAO dDAO = new DamageReportDAO();

    public Item viewItemDetails(String itemCode) throws Exception {
        if (itemCode == null || itemCode.trim().isEmpty()) {
            throw new ValidationException("Item code cannot be blank.");
        }
        try (Connection con = DBUtil.getDBConnection()) {
            return iDAO.findItem(con, itemCode);
        }
    }

    public List<Item> viewAllItems() throws Exception {
        try (Connection con = DBUtil.getDBConnection()) {
            return iDAO.viewAllItems(con);
        }
    }

    public boolean addNewItem(Item item) throws Exception {
        if (item.getItemCode() == null || item.getItemCode().trim().isEmpty())
            throw new ValidationException("Item code must not be blank.");
        if (item.getItemDescription() == null || item.getItemDescription().trim().isEmpty())
            throw new ValidationException("Item description must not be empty.");
        if (item.getUnitOfMeasure() == null || item.getUnitOfMeasure().trim().isEmpty())
            throw new ValidationException("Unit of measure must be present.");
        if (item.getUnitPrice() <= 0)
            throw new ValidationException("Unit price must be greater than zero.");
        if (item.getOnHandQuantity() < 0)
            throw new ValidationException("Initial on-hand quantity must be zero or positive.");

        try (Connection con = DBUtil.getDBConnection()) {
            if (iDAO.findItem(con, item.getItemCode()) != null) {
                return false; // Item already exists
            }
            item.setTotalDamagedQuantity(0);
            boolean result = iDAO.insertItem(con, item);
            con.commit();
            return result;
        }
    }

    public boolean logDamage(String itemCode, Date reportDate, String reportedBy, String reason, double damagedQuantity)
            throws Exception {
        if (itemCode == null || itemCode.trim().isEmpty() || reportedBy == null || reportedBy.trim().isEmpty())
            throw new ValidationException("Item code and reported by must not be blank.");
        if (reportDate == null)
            throw new ValidationException("Report date must not be null.");
        if (damagedQuantity <= 0)
            throw new ValidationException("Damaged quantity must be greater than zero.");

        Connection con = null;
        try {
            con = DBUtil.getDBConnection();
            Item item = iDAO.findItem(con, itemCode);
            if (item == null)
                return false;

            if (damagedQuantity > item.getOnHandQuantity()) {
                throw new InsufficientStockException("Inventory cannot become negative.");
            }

            DamageReport report = new DamageReport();
            report.setDamageID(dDAO.generateDamageID(con));
            report.setItemCode(itemCode);
            report.setReportDate(reportDate);
            report.setReportedBy(reportedBy);
            report.setReason(reason);
            report.setDamagedQuantity(damagedQuantity);
            report.setStatus("OPEN");

            dDAO.recordDamageReport(con, report);

            double newOnHand = item.getOnHandQuantity() - damagedQuantity;
            double newTotalDamaged = item.getTotalDamagedQuantity() + damagedQuantity;
            iDAO.updateQuantities(con, itemCode, newOnHand, newTotalDamaged);

            con.commit();
            return true;
        } catch (Exception e) {
            if (con != null)
                con.rollback();
            throw e;
        } finally {
            if (con != null)
                con.close();
        }
    }

    public boolean resolveDamageReport(int damageID, String resolutionAction) throws Exception {
        if (damageID <= 0)
            throw new ValidationException("Damage ID must be positive.");

        Connection con = null;
        try {
            con = DBUtil.getDBConnection();
            DamageReport report = dDAO.findDamageReportById(con, damageID);
            if (report == null)
                return false;

            // If current status is REVERSED or CONFIRMED, return false to avoid
            // double-processing
            if ("REVERSED".equals(report.getStatus()) || "CONFIRMED".equals(report.getStatus())) {
                return false;
            }

            Item item = iDAO.findItem(con, report.getItemCode());
            if (item == null)
                return false;

            if ("REVERSE".equalsIgnoreCase(resolutionAction)) {
                dDAO.updateDamageStatus(con, damageID, "REVERSED");
                double newOnHand = item.getOnHandQuantity() + report.getDamagedQuantity();
                double newTotalDamaged = item.getTotalDamagedQuantity() - report.getDamagedQuantity();
                iDAO.updateQuantities(con, report.getItemCode(), newOnHand, newTotalDamaged);
            } else if ("CONFIRM".equalsIgnoreCase(resolutionAction)) {
                dDAO.updateDamageStatus(con, damageID, "CONFIRMED");
            } else {
                return false;
            }

            con.commit();
            return true;
        } catch (Exception e) {
            if (con != null)
                con.rollback();
            throw e;
        } finally {
            if (con != null)
                con.close();
        }
    }

    public boolean removeItem(String itemCode) throws Exception {
        if (itemCode == null || itemCode.trim().isEmpty()) {
            throw new ValidationException("Item code cannot be blank.");
        }
        try (Connection con = DBUtil.getDBConnection()) {
            if (dDAO.hasActiveReports(con, itemCode)) {
                throw new ActiveDamageReportsException(
                        "Active incidents must be resolved or reversed before deletion.");
            }
            boolean result = iDAO.deleteItem(con, itemCode);
            con.commit();
            return result;
        }
    }
}