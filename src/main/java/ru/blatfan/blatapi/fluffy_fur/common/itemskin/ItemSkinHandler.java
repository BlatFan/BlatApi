package ru.blatfan.blatapi.fluffy_fur.common.itemskin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItemSkinHandler {
    public static Map<String, ItemSkin> skins = new HashMap<>();
    public static ArrayList<ItemSkin> skinList = new ArrayList<>();

    public static void addSkin(String id, ItemSkin skin) {
        skins.put(id, skin);
        skinList.add(skin);
    }

    public static ItemSkin getSkin(int id) {
        return skins.get(id);
    }

    public static ItemSkin getSkin(String id) {
        return skins.get(id);
    }

    public static void register(ItemSkin skin) {
        skins.put(skin.getId(), skin);
        skinList.add(skin);
    }

    public static int size() {
        return skins.size();
    }

    public static ArrayList<ItemSkin> getSkins() {
        return skinList;
    }
}
