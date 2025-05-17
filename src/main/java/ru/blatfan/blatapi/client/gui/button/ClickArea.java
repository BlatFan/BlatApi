package ru.blatfan.blatapi.client.gui.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ClickArea extends Button {
	public ClickArea(int x, int y, int width, int height, OnPress onPress) {
		this(x, y, width, height, Component.literal(""), onPress);
	}
	
	public ClickArea(int x, int y, int width, int height, Component text, OnPress onPress) {
		super(x, y, width, height, text, onPress, DEFAULT_NARRATION);
	}
	
	@Override
	public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {}
	@Override
	protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {}
}