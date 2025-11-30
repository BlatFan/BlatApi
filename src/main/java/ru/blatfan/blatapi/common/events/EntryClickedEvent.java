package ru.blatfan.blatapi.common.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import ru.blatfan.blatapi.common.guide_book.GuideBookCategory;
import ru.blatfan.blatapi.common.guide_book.GuideBookData;
import ru.blatfan.blatapi.common.guide_book.GuideBookEntry;

@RequiredArgsConstructor@Cancelable@Getter
public class EntryClickedEvent extends Event {
    private final GuideBookData book;
    private final GuideBookCategory category;
    private final GuideBookEntry entry;
}