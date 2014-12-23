package engine.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;

public class Input extends KeyAdapter {
    private static final HashSet<Integer> keysDown = new HashSet<>();
    
    
    @Override
    public void keyReleased(KeyEvent e) {
        keysDown.remove(e.getKeyCode());
        e.consume();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keysDown.add(e.getKeyCode());
        e.consume();
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Integer> getKeysDown() {
        return new ArrayList<>(keysDown);
    }

    /**
     *
     * @param keyCode - code to check against for downness.
     * @return True or false depending on the result of the check.
     */
    public static boolean isKeyDown(int keyCode) {
        return keysDown.contains(keyCode);
    }
}
