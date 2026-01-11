package ru.blatfan.blatapi.common.guide_book;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import ru.blatfan.blatapi.client.guide_book.screen.GuideBookPaperScreen;
import ru.blatfan.blatapi.common.GuideManager;
import ru.blatfan.blatapi.common.task.Task;
import ru.blatfan.blatapi.utils.NBTHelper;
import ru.blatfan.blatapi.utils.collection.Text;

import java.util.ArrayList;
import java.util.List;

public class GuidePaperItem extends Item{
    public GuidePaperItem() {
        this(new Properties().stacksTo(1));
    }
    public GuidePaperItem(Properties properties) {
        super(properties);
    }
    
    public static ItemStack getPaper(GuidePaperItem item, ResourceLocation entry, ResourceLocation book, List<Task> tasks){
        ItemStack stack = new ItemStack(item);
        setEntry(stack, entry);
        setBook(stack, book);
        setTasks(stack, tasks);
        return stack;
    }
    public static ItemStack getPaper(GuidePaperItem item, ResourceLocation entry, ResourceLocation book){
        return getPaper(item, entry, book, new ArrayList<>());
    }
    
    public static void setEntry(ItemStack stack, ResourceLocation entry){
        NBTHelper.setString(stack, "entry", entry.toString());
    }
    public static ResourceLocation getEntry(ItemStack stack){
        return ResourceLocation.tryParse(NBTHelper.getString(stack, "entry"));
    }
    
    public static void setBook(ItemStack stack, ResourceLocation book){
        NBTHelper.setString(stack, "book", book.toString());
    }
    public static ResourceLocation getBook(ItemStack stack){
        return ResourceLocation.tryParse(NBTHelper.getString(stack, "book"));
    }
    
    public static void setTasks(ItemStack stack, List<Task> tasks){
        CompoundTag tag = new CompoundTag();
        for (int i = 0; i < tasks.size(); i++)
            tag.put(String.valueOf(i), Task.toNBT(tasks.get(i)));
        stack.getOrCreateTag().put("tasks", tag);
    }
    public static List<Task> getTasks(ItemStack stack){
        List<Task> tasks = new ArrayList<>();
        CompoundTag tag = stack.getOrCreateTag().getCompound("tasks");
        for(int i=0; i<tag.size(); i++)
            tasks.add(Task.fromNBT(tag.getCompound(String.valueOf(i))));
        return tasks;
    }
    
    @Override
    public Component getName(ItemStack itemStack) {
        GuideBookEntry entry = GuideManager.getEntry(getEntry(itemStack));
        if(entry==null) return super.getName(itemStack);
        return Text.create(super.getName(itemStack)).add(" (").add(entry.title()).add(")").asComponent();
    }
    
    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        GuideBookEntry entry = GuideManager.getEntry(getEntry(pStack));
        if(entry!=null)
            pTooltipComponents.add(entry.description());
        if(pIsAdvanced.isAdvanced())
            pTooltipComponents.add(Text.create(getEntry(pStack)).withStyle(ChatFormatting.DARK_GRAY));
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if(pLevel.isClientSide && getEntry(stack)!=null){
            Minecraft.getInstance().setScreen(new GuideBookPaperScreen(getEntry(stack), getBook(stack)));
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.pass(stack);
    }
}