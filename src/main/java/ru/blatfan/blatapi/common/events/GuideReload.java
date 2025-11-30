package ru.blatfan.blatapi.common.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import ru.blatfan.blatapi.common.guide_book.GuideBookCategory;
import ru.blatfan.blatapi.common.guide_book.GuideBookData;
import ru.blatfan.blatapi.common.guide_book.GuideBookEntry;
import ru.blatfan.blatapi.common.guide_book.GuideBookPaper;
import ru.blatfan.blatapi.common.multiblock.Multiblock;

@Cancelable
public class GuideReload extends Event {
    @Getter@AllArgsConstructor
    public static class MultiblockReload extends GuideReload {
        private final Multiblock multiblock;
    }
    @Getter@AllArgsConstructor
    public static class BookReload extends GuideReload {
        private final GuideBookData bookData;
    }
    @Getter@AllArgsConstructor
    public static class CategoryReload extends GuideReload {
        private final GuideBookCategory category;
    }
    @Getter@AllArgsConstructor
    public static class EntryReload extends GuideReload {
        private final GuideBookEntry entry;
    }
    @Getter@AllArgsConstructor
    public static class PaperReload extends GuideReload {
        private final GuideBookPaper paper;
    }
}