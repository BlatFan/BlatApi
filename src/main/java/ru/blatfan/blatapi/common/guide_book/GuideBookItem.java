package ru.blatfan.blatapi.common.guide_book;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import ru.blatfan.blatapi.BlatApi;
import ru.blatfan.blatapi.client.guide_book.GuideClient;
import ru.blatfan.blatapi.common.BARegistry;
import ru.blatfan.blatapi.common.GuideManager;
import ru.blatfan.blatapi.utils.NBTHelper;
import ru.blatfan.blatapi.utils.collection.Text;

import java.util.List;

public class GuideBookItem extends Item {
    public static final String TAG = "guide_book_id";
    public GuideBookItem(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }
    public GuideBookItem() {
        this(new Properties());
    }
    
    public static GuideBookData getBook(ItemStack stack) {
        ResourceLocation res = getBookId(stack);
        if (res == null) return null;
        return GuideManager.getBook(res);
    }
    
    public static ItemStack getBook(ResourceLocation bookId){
        ItemStack stack = new ItemStack(BARegistry.GUIDE_BOOK.get(),1);
        NBTHelper.setString(stack, TAG, bookId.toString());
        return stack;
    }
    
    private static ResourceLocation getBookId(ItemStack stack) {
        if (!stack.hasTag() || !stack.getTag().contains(TAG))
            return BlatApi.loc("error");
        String bookStr = stack.getTag().getString(TAG);
        return ResourceLocation.tryParse(bookStr);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemInHand = pPlayer.getItemInHand(pUsedHand);
        
        if (pLevel.isClientSide)
            GuideClient.open(getBook(itemInHand));
        
        return InteractionResultHolder.sidedSuccess(itemInHand, pLevel.isClientSide);
    }
    
    @Override
    public Component getName(ItemStack pStack) {
        GuideBookData book = getBook(pStack);
        return book.getTitle();
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        
        GuideBookData book = getBook(stack);
        if (book != null) {
            if (flagIn.isAdvanced())
                tooltip.add(Component.literal("Book ID: ").withStyle(ChatFormatting.DARK_GRAY)
                    .append(Component.literal(getBookId(stack).toString()).withStyle(ChatFormatting.RED)));
            
            if (!book.getTooltip().equals(Component.empty())) {
                tooltip.add(book.getTooltip());
                tooltip.add(Component.empty());
            }
            tooltip.add(Text.create("guide_book.blatapi.author").add(book.getAuthor()));
        }
    }
}