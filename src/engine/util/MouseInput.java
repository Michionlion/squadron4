package engine.util;

import engine.interfaces.InputListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;



public class MouseInput extends MouseAdapter {
    
    private static MouseEvent currentMouseEvent;
    private static int currentX,currentY;
    private static boolean rightMouseDown;
    private static boolean leftMouseDown;
    private static ArrayList<InputListener> Lhits = new ArrayList<>();
    private static ArrayList<InputListener> Rhits = new ArrayList<>();
    
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            for(InputListener t : Lhits) {
                t.trigger();
            }
            leftMouseDown = true;
        }
        if(e.getButton() == MouseEvent.BUTTON3) {
            for(InputListener t : Rhits) {
                t.trigger();
            }
            rightMouseDown = true;
        }
        currentMouseEvent = e;
        
        
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) leftMouseDown = false;
        if(e.getButton() == MouseEvent.BUTTON3) rightMouseDown = false;
        currentMouseEvent = e;
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        currentMouseEvent = e;
        currentX = e.getX();
        currentY = e.getY();
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        currentMouseEvent = e;
        currentX = e.getX();
        currentY = e.getY();
    }
    
    public static boolean isMouseDown() {
        return rightMouseDown || leftMouseDown;
    }
    
    public static boolean isRightMouseDown() {
        return rightMouseDown;
    }
    
    public static boolean isLeftMouseDown() {
        return leftMouseDown;
    }
    
    public static boolean isBothMouseDown() {
        return rightMouseDown && leftMouseDown;
    }

    public static MouseEvent getMouseEvent() {
        return currentMouseEvent;
    }

    public static int getX() {
        return currentX;
    }

    public static int getY() {
        return currentY;
    }
    
    public static void addLeftHitListener(InputListener trigger) {
        if(!Lhits.contains(trigger))Lhits.add(trigger);
    }
    
    public static void addRightHitListener(InputListener trigger) {
        if(!Rhits.contains(trigger))Rhits.add(trigger);
    }
    
    public static void removeLeftHitListener(InputListener trigger) {
        if(Lhits.contains(trigger))Lhits.remove(trigger);
    }
    
    public static void removeRightHitListener(InputListener trigger) {
        if(Rhits.contains(trigger))Rhits.remove(trigger);
    }
}