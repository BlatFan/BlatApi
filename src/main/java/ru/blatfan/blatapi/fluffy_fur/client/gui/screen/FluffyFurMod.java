package ru.blatfan.blatapi.fluffy_fur.client.gui.screen;

import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class FluffyFurMod {
    private final String id;
    private final String name;
    private final String version;
    private int edition = 0;
    private String dev = "Unknown";
    private ItemStack itemStack = new ItemStack(Items.DIRT);
    private Color nameColor = Color.WHITE;
    private Color versionColor = Color.GRAY;
    private Component description = Component.empty();
    private List<Link> links = new ArrayList<>();

    public FluffyFurMod(String id, String name, String version) {
        this.id = id;
        this.name = name;
        this.version = version;
    }

    public FluffyFurMod setEdition(int edition) {
        this.edition = edition;
        return this;
    }

    public FluffyFurMod setDev(String dev) {
        this.dev = dev;
        return this;
    }

    public FluffyFurMod setItem(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public FluffyFurMod setNameColor(Color nameColor) {
        this.nameColor = nameColor;
        return this;
    }

    public FluffyFurMod setVersionColor(Color versionColor) {
        this.versionColor = versionColor;
        return this;
    }

    public FluffyFurMod setDescription(Component description) {
        this.description = description;
        return this;
    }

    public static class Link {
        public Component component;
        public String link;

        public Link(Component component, String link) {
            this.component = component;
            this.link = link;
        }

        public Component getComponent() {
            return component;
        }

        public String getLink() {
            return link;
        }
    }
    
    public FluffyFurMod addLink(Component component, String string) {
        links.add(new Link(component, string));
        return this;
    }

    public FluffyFurMod addGithubLink(String string) {
        addLink(Component.literal("Github").withStyle(ChatFormatting.DARK_GRAY), string);
        return this;
    }

    public FluffyFurMod addCurseForgeLink(String string) {
        addLink(Component.literal("CurseForge").withStyle(ChatFormatting.GOLD), string);
        return this;
    }

    public FluffyFurMod addModrinthLink(String string) {
        addLink(Component.literal("Modrinth").withStyle(ChatFormatting.GREEN), string);
        return this;
    }

    public FluffyFurMod addDiscordLink(String string) {
        addLink(Component.literal("Discord").withStyle(ChatFormatting.BLUE), string);
        return this;
    }
}
