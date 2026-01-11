package ru.blatfan.blatapi.fluffy_fur.client.gui.screen;

import lombok.Getter;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import ru.blatfan.blatapi.common.BARegistry;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
public class FluffyFurMod {
    public String id;
    public String name;
    public String version;
    public int edition = 0;
    public String dev = "Unknown";
    public ItemStack itemStack = new ItemStack(Items.DIRT);
    public Color nameColor = Color.WHITE;
    public Color versionColor = Color.GRAY;
    public Component description = Component.empty();
    public List<Link> links = new ArrayList<>();
    
    public static Color bfColor = new Color(119, 65, 255);
    public static Color githubColor = new Color(60, 116, 195);
    public static Color curseForgeColor = new Color(241, 100, 54);
    public static Color modrinthColor = new Color(0, 175, 92);
    public static Color discordColor = new Color(88, 101, 242);
    public static Color youTubeColor = new Color(255, 0, 0);
    public static Color blueSkyColor = new Color(41, 94, 246);
    public static Color redditColor = new Color(255, 69, 0);
    public static Color websiteColor = new Color(153, 153, 153);
    public static Color twitterColor = new Color(29, 155, 240);
    public static Color telegramColor = new Color(70, 177, 255);
    
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
    
    @Getter
    public static class Link {
        public Component component;
        public String link;
        
        public Link(Component component, String link) {
            this.component = component;
            this.link = link;
        }
        
    }
    
    public FluffyFurMod addLink(Component component, String string) {
        links.add(new Link(component, string));
        return this;
    }
    
    public FluffyFurMod addIconLink(String string, String name, Color color, String icon, ResourceLocation font) {
        addLink(Component.empty()
                .append(Component.literal(icon).withStyle(Style.EMPTY.withColor(color.getRGB()).withFont(font)))
                .append(CommonComponents.SPACE.copy().withStyle(Style.EMPTY.withColor(color.getRGB())))
                .append(Component.literal(name).withStyle(Style.EMPTY.withColor(color.getRGB()))),
            string);
        return this;
    }
    public FluffyFurMod addIconLink(String string, String name, Color color, String icon) {
        return addIconLink(string, name, color, icon, BARegistry.Fonts.ICONS_LOCATION);
    }
    
    public FluffyFurMod addBFLinks(String mod){
        return addBFLinks(mod.replace(" ", ""), mod.replace(' ', '-').toLowerCase(), mod.replace(' ', '-').toLowerCase());
    }
    public FluffyFurMod addBFLinks(String git, String curse, String modr) {
        addIconLink("https://github.com/BlatFan/"+git, "Github", githubColor, "\u0001\u0002");
        addIconLink("https://www.curseforge.com/minecraft/mc-mods/"+curse, "CurseForge", curseForgeColor, "\u0001\u0003");
        addIconLink("https://modrinth.com/mod/"+modr, "Modrinth", modrinthColor, "\u0001\u0004");
        addIconLink("https://discord.gg/eHJChH9mqH", "Discord", discordColor, "\u0001\u0005");
        addIconLink("https://www.youtube.com/@BlatFan", "YouTube", youTubeColor, "\u0001\u0006");
        addIconLink("https://t.me/BlatFanTG", "Telegram", telegramColor, "\u0001\u0011");
        return this;
    }
    
    public FluffyFurMod addTelegramLink(String string) {
        return addIconLink(string, "Telegram", telegramColor, "\u0011");
    }
    
    public FluffyFurMod addGithubLink(String string) {
        return addIconLink(string, "Github", githubColor, "\u0002");
    }
    
    public FluffyFurMod addCurseForgeLink(String string) {
        return addIconLink(string, "CurseForge", curseForgeColor, "\u0003");
    }
    
    public FluffyFurMod addModrinthLink(String string) {
        return addIconLink(string, "Modrinth", modrinthColor, "\u0004");
    }
    
    public FluffyFurMod addDiscordLink(String string) {
        return addIconLink(string, "Discord", discordColor, "\u0005");
    }
    
    public FluffyFurMod addYouTubeLink(String string) {
        return addIconLink(string, "YouTube", youTubeColor, "\u0006");
    }
    
    public FluffyFurMod addBlueSkyLink(String string) {
        return addIconLink(string, "BlueSky", blueSkyColor, "\u0007");
    }
    
    public FluffyFurMod addRedditLink(String string) {
        return addIconLink(string, "Reddit", redditColor, "\u0008");
    }
    
    public FluffyFurMod addWebsiteLink(String string, String website, Color color) {
        return addIconLink(string, website, color, "\u0009");
    }
    
    public FluffyFurMod addWebsiteLink(String string, String website) {
        return addWebsiteLink(string, website, websiteColor);
    }
    
    public FluffyFurMod addWebsiteLink(String string) {
        return addWebsiteLink(string, "Website", websiteColor);
    }
    
    public FluffyFurMod addTwitterLink(String string) {
        return addIconLink(string, "Twitter", twitterColor, "\u0010");
    }
}