package com.warehouse.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.warehouse.bean.Item;

public class ItemDAO {
    public Item findItem(Connection con, String code) throws Exception {
        String sql = "SELECT * FROM ITEM_TBL WHERE item_code = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Item i = new Item();
                    i.setItemCode(rs.getString("item_code"));
                    i.setItemDescription(rs.getString("item_description"));
                    i.setUnitOfMeasure(rs.getString("unit_of_measure"));
                    i.setUnitPrice(rs.getDouble("unit_price"));
                    i.setOnHandQuantity(rs.getDouble("on_hand_quantity"));
                    i.setTotalDamagedQuantity(rs.getDouble("total_damaged_quantity"));
                    return i;
                }
            }
        }
        return null;
    }

    public List<Item> viewAllItems(Connection con) throws Exception {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM ITEM_TBL";
        try (PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Item i = new Item();
                i.setItemCode(rs.getString("item_code"));
                i.setItemDescription(rs.getString("item_description"));
                i.setUnitOfMeasure(rs.getString("unit_of_measure"));
                i.setUnitPrice(rs.getDouble("unit_price"));
                i.setOnHandQuantity(rs.getDouble("on_hand_quantity"));
                i.setTotalDamagedQuantity(rs.getDouble("total_damaged_quantity"));
                items.add(i);
            }
        }
        return items;
    }

    public boolean insertItem(Connection con, Item i) throws Exception {
        String sql = "INSERT INTO ITEM_TBL (item_code, item_description, unit_of_measure, unit_price, on_hand_quantity, total_damaged_quantity) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, i.getItemCode());
            ps.setString(2, i.getItemDescription());
            ps.setString(3, i.getUnitOfMeasure());
            ps.setDouble(4, i.getUnitPrice());
            ps.setDouble(5, i.getOnHandQuantity());
            ps.setDouble(6, i.getTotalDamagedQuantity());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean updateQuantities(Connection con, String c, double o, double d) throws Exception {
        String sql = "UPDATE ITEM_TBL SET on_hand_quantity = ?, total_damaged_quantity = ? WHERE item_code = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, o);
            ps.setDouble(2, d);
            ps.setString(3, c);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean deleteItem(Connection con, String c) throws Exception {
        String sql = "DELETE FROM ITEM_TBL WHERE item_code = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c);
            return ps.executeUpdate() == 1;
        }
    }
}