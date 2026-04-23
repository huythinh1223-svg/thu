package Auction.example.model.item;

import Auction.example.enums.ItemCondition;
import Auction.example.enums.ItemType;

import java.util.Map;
import java.util.UUID;

public class ItemFactory {

    public static Item createItem(ItemType type, String name, String desc, double price,
                                  ItemCondition condition, Map<String, Object> extraParams) {

        String generatedId = UUID.randomUUID().toString();

        Item newItem = null;

        switch (type) {
            case ELECTRONICS:
                int warranty = (int) extraParams.getOrDefault("warrantyPeriod", 0);
                String brand = (String) extraParams.getOrDefault("brand", "Unknown");
                newItem = new Electronics(generatedId, name, desc, price, condition, warranty, brand);
                break;

            case ART:
                String artist = (String) extraParams.getOrDefault("artistName", "Unknown");
                int year = (int) extraParams.getOrDefault("creationYear", 0);
                boolean auth = (boolean) extraParams.getOrDefault("isAuthentic", false);
                newItem = new Art(generatedId, name, desc, price, condition, artist, year, auth);
                break;

            case VEHICLE:
                int mileage = (int) extraParams.getOrDefault("mileage", 0);
                String engine = (String) extraParams.getOrDefault("engineType", "Unknown");
                String plate = (String) extraParams.getOrDefault("licensePlate", "N/A");
                newItem = new Vehicle(generatedId, name, desc, price, condition, mileage, engine, plate);
                break;

            default:
                throw new IllegalArgumentException("Loại sản phẩm không được hỗ trợ: " + type);
        }

        if (!newItem.validateItem()) {
            throw new IllegalArgumentException("Thông số sản phẩm không hợp lệ, không thể khởi tạo!");
        }

        return newItem;
    }
}