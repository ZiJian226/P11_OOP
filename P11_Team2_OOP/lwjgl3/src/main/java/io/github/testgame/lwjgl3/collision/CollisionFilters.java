package io.github.testgame.lwjgl3.collision;

public class CollisionFilters {
    public static final short CATEGORY_PLAYER = 0x0001;
    public static final short CATEGORY_ENEMY = 0x0002;
    public static final short CATEGORY_NEUTRAL = 0x0004;
    public static final short CATEGORY_AGGRESSIVE = 0x0008;
    public static final short CATEGORY_BULLET = 0x0010;

    public static final short MASK_PLAYER = CATEGORY_ENEMY | CATEGORY_NEUTRAL | CATEGORY_AGGRESSIVE;
    public static final short MASK_ENEMY = CATEGORY_PLAYER | CATEGORY_BULLET;
    public static final short MASK_NEUTRAL = CATEGORY_PLAYER | CATEGORY_BULLET;
    public static final short MASK_AGGRESSIVE = CATEGORY_PLAYER | CATEGORY_BULLET;
    public static final short MASK_BULLET = CATEGORY_ENEMY | CATEGORY_NEUTRAL | CATEGORY_AGGRESSIVE;
}
