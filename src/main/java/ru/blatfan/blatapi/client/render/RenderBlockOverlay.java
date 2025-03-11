package ru.blatfan.blatapi.client.render;

import org.joml.Matrix4f;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ru.blatfan.blatapi.client.render.type.OverlayRenderType;
import ru.blatfan.blatapi.utils.PlayerClickBlockfaceUtil;

public class RenderBlockOverlay {

  private final ResourceLocation overlayLocation;
  private final String id;
  private Class<?> itemClass;

  public RenderBlockOverlay(String id, ResourceLocation overlayLocationIn, Class<?> classIn) {
    this.id = id;
    overlayLocation = overlayLocationIn;
    this.itemClass = classIn;
  }

  private final Vec3[] vs = new Vec3[8];
  {
    for (int i = 0; i < 8; ++i) {
      int x = (i & 1) == 1 ? 1 : 0;
      int y = (i & 2) == 2 ? 1 : 0;
      int z = (i & 4) == 4 ? 1 : 0;
      vs[i] = new Vec3(x, y, z);
    }
  }
  int arrow1 = 0;
  int arrow2 = 1;
  int arrow3 = 2;
  int arrow4 = 3;
  int cross = 4;
  int bullseye = 5;
  int cancel = 6;
  private final float[][][] uvs = new float[7][4][];
  {
    //ararrow1ow 1
    uvs[arrow1][0] = new float[] { 0, 0 };
    uvs[arrow1][1] = new float[] { 0, .5f };
    uvs[arrow1][2] = new float[] { .5f, .5f };
    uvs[arrow1][3] = new float[] { .5f, 0 };
    //arrow 2
    uvs[arrow2][0] = new float[] { 0, .5f };
    uvs[arrow2][1] = new float[] { .5f, .5f };
    uvs[arrow2][2] = new float[] { .5f, 0 };
    uvs[arrow2][3] = new float[] { 0, 0 };
    //arrow 3
    uvs[arrow3][0] = new float[] { .5f, .5f };
    uvs[arrow3][1] = new float[] { .5f, 0 };
    uvs[arrow3][2] = new float[] { 0, 0 };
    uvs[arrow3][3] = new float[] { 0, .5f };
    //arrow 4
    uvs[arrow4][0] = new float[] { .5f, 0 };
    uvs[arrow4][1] = new float[] { 0, 0 };
    uvs[arrow4][2] = new float[] { 0, .5f };
    uvs[arrow4][3] = new float[] { .5f, .5f };
    //cross
    uvs[cross][0] = new float[] { .5f, 0 };
    uvs[cross][1] = new float[] { .5f, .5f };
    uvs[cross][2] = new float[] { 1, .5f };
    uvs[cross][3] = new float[] { 1, 0 };
    //bullseye
    uvs[bullseye][0] = new float[] { 0, .5f };
    uvs[bullseye][1] = new float[] { 0, 1 };
    uvs[bullseye][2] = new float[] { .5f, 1 };
    uvs[bullseye][3] = new float[] { .5f, .5f };
    //cancel
    uvs[cancel][0] = new float[] { .5f, .5f };
    uvs[cancel][1] = new float[] { .5f, 1 };
    uvs[cancel][2] = new float[] { 1, 1 };
    uvs[cancel][3] = new float[] { 1, .5f };
  }

  @SubscribeEvent
  public void renderOverlay(RenderHighlightEvent.Block event) {
    if (event.getTarget().getType() != HitResult.Type.BLOCK) {
      return;
    }
    if (!shouldRender()) {
      return;
    }
    BlockHitResult result = event.getTarget();
    PoseStack poseStack = event.getPoseStack();
    MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
    Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
    BlockPos blockPos = new BlockPos(result.getBlockPos());
    Vec3 hitVec = result.getLocation();
    Direction indexd;
    int[] look = new int[6];
    if (isBadBlock(event)) {
      indexd = Direction.UP;
      look = new int[] { cancel, cancel, cancel, cancel, cancel, cancel };
    }
    else {
      indexd = PlayerClickBlockfaceUtil.getClickLocationDirection(result.getDirection(), hitVec, blockPos);
      if (indexd == null) {
        return;
      }
      indexd = indexd.getOpposite();
      switch (indexd) {
        case DOWN:
          look = new int[] { arrow3, bullseye, arrow2, arrow2, cross, arrow3 };
        break;
        case UP:
          look = new int[] { arrow1, cross, arrow4, arrow4, bullseye, arrow1 };
        break;
        case NORTH:
          look = new int[] { arrow2, arrow3, bullseye, arrow3, arrow2, cross };
        break;
        case SOUTH:
          look = new int[] { arrow4, arrow1, cross, arrow1, arrow4, bullseye };
        break;
        case WEST:
          look = new int[] { bullseye, arrow2, arrow3, cross, arrow3, arrow2 };
        break;
        case EAST:
          look = new int[] { cross, arrow4, arrow1, bullseye, arrow1, arrow4 };
        break;
        default:
        break;
      }
    }
    poseStack.pushPose();
    RenderType renderType = OverlayRenderType.overlayRenderer(id, overlayLocation);
    VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
    poseStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);
    //      .log.info("{} ::  mPos {} ({}  {}  {}) ", mPos, indexd, v.x, v.y, v.z);
    double yDiff = hitVec.y - blockPos.getY();
    if (yDiff > PlayerClickBlockfaceUtil.HI && yDiff < PlayerClickBlockfaceUtil.LO) {
      //edge corner case
      return;
    }
    poseStack.translate(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    //P/N ONLY exist to prevent layer fighting/flashing, push it just outside ontop of the block, so 1 + this fract
    final float P = 1 / 256f, N = -1 / 256f;
    final int X = 1, Y = 2, Z = 4;
    //now draw the box
    int TOP = 1, EAST = 0, SOUTH = 2, WEST = 3, BOTTOM = 4, NORTH = 5;
    //draw east
    poseStack.translate(P, 0, 0);
    drawSide(vertexConsumer, poseStack.last().pose(), X, Y, Z, uvs[look[EAST]]);// this one has to be est or west side
    //draw top
    poseStack.translate(N, P, 0);
    drawSide(vertexConsumer, poseStack.last().pose(), Y, Z, X, uvs[look[TOP]]); // TOP
    //SOUTH
    poseStack.translate(0, N, P);
    drawSide(vertexConsumer, poseStack.last().pose(), Z, X, Y, uvs[look[SOUTH]]);
    //WEST
    poseStack.translate(N, 0, N);
    drawSide(vertexConsumer, poseStack.last().pose(), 0, Z, Y, uvs[look[WEST]]);
    //BOTTOM
    poseStack.translate(P, N, 0);
    drawSide(vertexConsumer, poseStack.last().pose(), 0, X, Z, uvs[look[BOTTOM]]);
    //NORTH
    poseStack.translate(0, P, N);
    drawSide(vertexConsumer, poseStack.last().pose(), 0, Y, X, uvs[look[NORTH]]);
    bufferSource.endBatch(renderType);
    poseStack.popPose();
  }

  public boolean shouldRender() {
    Player player = Minecraft.getInstance().player;
    if (player == null) {
      return false;
    }
    ItemStack mainItemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
    ItemStack offItemStack = player.getItemInHand(InteractionHand.OFF_HAND);
    //
    return (!mainItemStack.isEmpty() && mainItemStack.getItem().getClass() == itemClass) ||
        (!offItemStack.isEmpty() && offItemStack.getItem().getClass() == itemClass);
  }

  /**
   * Override this to exclude blocks by state/tile/replacable etc
   * 
   * @param event
   * @return
   */
  public boolean isBadBlock(RenderHighlightEvent.Block event) {
    return false;
    //    BlockPos pos = event.getTarget().getBlockPos();
    //    World world = event.getPlayer().world;
    //    IBlockState state = world.getBlockState(pos);
    //    Block block = state.getBlock();
    //    return block.hasTileEntity(state) || block.isReplaceable(world, pos);
  }

  private void drawSide(VertexConsumer buffer, Matrix4f matrix, int c, int i, int j, float[][] uv) {
    addVertex(buffer, matrix, uv[0][0], uv[0][1], c);
    addVertex(buffer, matrix, uv[1][0], uv[1][1], c + i);
    addVertex(buffer, matrix, uv[2][0], uv[2][1], c + i + j);
    addVertex(buffer, matrix, uv[3][0], uv[3][1], c + j);
  }

  private void addVertex(VertexConsumer buffer, Matrix4f matrix, double u, double v, int i) {
    buffer.vertex(matrix, (float) vs[i].x, (float) vs[i].y, (float) vs[i].z)
        .color(1.0f, 1.0f, 1.0f, 0.375f)
        .uv((float) u, (float) v).endVertex();
  }
}
