package com.warehouse.app;

import java.util.Date;
import java.util.Scanner;
import com.warehouse.bean.Item;
import com.warehouse.service.InventoryService;
import com.warehouse.util.*;

public class WarehouseMain {
    private static InventoryService inventoryService;

    public static void main(String[] args) {
        inventoryService = new InventoryService();
        Scanner sc = new Scanner(System.in);
        System.out.println("--- Warehouse Damage & Inventory Console ---");

        // TEST 1: Add a new item
        try {
            Item it = new Item();
            it.setItemCode("ITM2001");
            it.setItemDescription("Safety Helmet");
            it.setUnitOfMeasure("PCS");
            it.setUnitPrice(450.00);
            it.setOnHandQuantity(100.00);
            it.setTotalDamagedQuantity(0.00);
            boolean r = inventoryService.addNewItem(it);
            System.out.println(r ? "ITEM ADDED" : "ITEM ADD FAILED (ALREADY EXISTS)");
        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TEST 2: Log a damage report
        try {
            boolean r = inventoryService.logDamage("ITM2001", new Date(), "Rohit", "BREAKAGE", 5.00);
            System.out.println(r ? "DAMAGE LOGGED" : "DAMAGE LOG FAILED");
        } catch (InsufficientStockException e) {
            System.out.println("Stock Error: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TEST 3: View Details
        try {
            Item i = inventoryService.viewItemDetails("ITM2001");
            if (i != null) {
                System.out.println("Item: " + i.getItemCode() + ", OnHand: " + i.getOnHandQuantity() + ", Damaged: "
                        + i.getTotalDamagedQuantity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TEST 4: Attempt to remove an item with active damage reports
        try {
            boolean r = inventoryService.removeItem("ITM2001");
            System.out.println(r ? "ITEM REMOVED" : "ITEM REMOVAL FAILED");
        } catch (ActiveDamageReportsException e) {
            System.out.println("Removal Blocked: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("System Error: " + e.getMessage());
        }

        // TEST 5: Resolve Damage Report (REVERSE)
        try {
            // Assuming ID 720001 was generated for the first report
            boolean r = inventoryService.resolveDamageReport(720001, "REVERSE");
            System.out.println(r ? "DAMAGE REVERSED" : "REVERSAL FAILED");

            Item i = inventoryService.viewItemDetails("ITM2001");
            if (i != null) {
                System.out.println("After reversal - OnHand: " + i.getOnHandQuantity() + ", Damaged: "
                        + i.getTotalDamagedQuantity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // TEST 6: Final Deletion
        try {
            boolean r = inventoryService.removeItem("ITM2001");
            System.out.println(r ? "ITEM REMOVED SUCCESSFULLY" : "ITEM REMOVAL FAILED");
        } catch (Exception e) {
            System.out.println("System Error: " + e.getMessage());
        }

        sc.close();
    }
}