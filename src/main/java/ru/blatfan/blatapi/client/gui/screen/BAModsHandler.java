package ru.blatfan.blatapi.client.gui.screen;

import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.resources.ResourceLocation;
import ru.blatfan.blatapi.BlatApiClient;
import ru.blatfan.blatapi.client.gui.components.BALogoRenderer;
import ru.blatfan.blatapi.client.render.FlatCubeMap;
import ru.blatfan.blatapi.config.BlatApiClientConfig;

import java.util.*;

public class BAModsHandler {
    public static Map<String, BlatMod> mods = new HashMap<>();
    public static Map<String, BAPanorama> panoramas = new HashMap<>();

    public static void registerMod(BlatMod mod) {
        mods.put(mod.getId(), mod);
    }

    public static BlatMod getMod(String id) {
        return mods.get(id);
    }

    public static List<BlatMod> getMods() {
        return mods.values().stream().toList();
    }

    public static void registerPanorama(BAPanorama panorama) {
        panoramas.put(panorama.getId(), panorama);
    }

    public static BAPanorama getPanorama(String id) {
        return panoramas.get(id);
    }

    public static List<BAPanorama> getPanoramas() {
        return new ArrayList<>(panoramas.values());
    }

    public static List<BlatMod> getSortedMods() {
        List<BlatMod> sorted = new ArrayList<>();

        List<String> main = new ArrayList<>();
        List<String> sort = new ArrayList<>();

        for (String id : mods.keySet()) {
            if (!id.equals(BlatApiClient.MOD_INSTANCE.getId())) {
                if (getMod(id).getDev().equals("MaxBogomol")) {
                    main.add(id);
                } else {
                    sort.add(id);
                }
            }
        }

        Collections.sort(main);
        Collections.sort(sort);

        sorted.add(BlatApiClient.MOD_INSTANCE);
        for (String id : main) {
            sorted.add(getMod(id));
        }
        for (String id : sort) {
            sorted.add(getMod(id));
        }

        return sorted;
    }

    public static List<BAPanorama> getSortedPanoramas() {
        List<BAPanorama> sorted = new ArrayList<>();
        List<BAPanorama> panoramas = getPanoramas();
        List<BlatMod> sortedMods = getSortedMods();
        List<BAPanorama> added = new ArrayList<>();

        for (BlatMod mod : sortedMods) {
            List<BAPanorama> modPanoramas = new ArrayList<>();
            List<Integer> sorts = new ArrayList<>();
            for (BAPanorama panorama : panoramas) {
                if (panorama.getMod() == mod) {
                    sorts.add(panorama.getSort());
                    modPanoramas.add(panorama);
                    added.add(panorama);
                }
            }
            Collections.sort(sorts);
            for (int sort : sorts) {
                for (BAPanorama panorama : modPanoramas) {
                    if (panorama.getSort() == sort) {
                        sorted.add(panorama);
                    }
                }
            }
        }
        sorted.add(0, BlatApiClient.VANILLA_PANORAMA);
        for (BAPanorama panorama : panoramas) {
            if (!added.contains(panorama)) {
                if (panorama != BlatApiClient.VANILLA_PANORAMA) {
                    sorted.add(panorama);
                }
            }
        }

        return sorted;
    }

    public static BAPanorama getPanorama() {
        return getPanorama(BlatApiClientConfig.PANORAMA.get());
    }

    public static void setPanorama(BAPanorama panorama) {
        BlatApiClientConfig.PANORAMA.set(panorama.getId());
    }

    public static void setOpenPanorama(TitleScreen titleScreen, BAPanorama panorama) {
        float spin = titleScreen.panorama.spin;
        float bob = titleScreen.panorama.bob;
        ResourceLocation base = ResourceLocation.tryParse("textures/gui/title/background/panorama");
        ResourceLocation overlay = ResourceLocation.tryParse("textures/gui/title/background/panorama_overlay.png");
        if (panorama.getTexture() != null)
            base = panorama.getTexture();
        TitleScreen.CUBE_MAP = panorama.flat ? new FlatCubeMap(base) : new CubeMap(base);
        titleScreen.panorama = new PanoramaRenderer(TitleScreen.CUBE_MAP);
        TitleScreen.PANORAMA_OVERLAY = overlay;
        if (panorama.getLogo() != null) {
            titleScreen.logoRenderer = new BALogoRenderer(panorama.getLogo(), titleScreen.logoRenderer.keepLogoThroughFade);
        } else {
            if (titleScreen.logoRenderer instanceof BALogoRenderer) {
                titleScreen.logoRenderer = new LogoRenderer(titleScreen.logoRenderer.keepLogoThroughFade);
            }
        }
        titleScreen.panorama.spin = spin;
        titleScreen.panorama.bob = bob;
    }
}
