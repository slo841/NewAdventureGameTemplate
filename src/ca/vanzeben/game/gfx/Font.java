package ca.vanzeben.game.gfx;

public class Font {
    private static String chars = "" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " + "0123456789.,:;'\"!?$%()-=+/      ";
    private static SpriteSheet fontSheet = new SpriteSheet("font", "/sprite_sheet.png", 32, 32);
    
    public static void render(String msg, Screen screen, int x, int y, int scale) {
        msg = msg.toUpperCase();

        for (int i = 0; i < msg.length(); i++) {
            int charIndex = chars.indexOf(msg.charAt(i));
            if (charIndex >= 0)
                screen.render(x + (i * 8), y, fontSheet, charIndex + 30 * 32, Screen.MirrorDirection.NONE, fontSheet.getSpriteWidth()*scale, fontSheet.getSpriteHeight()*scale);
        }
    }
}