package assets;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Vector2f;

import engine.Globals;

import assets.game.objects.PlayerShip;
import assets.game.objects.Ship;

public class ShipManager {

    // doesn't include PLAYER ship
    protected static Map<String, Ship> ships = new HashMap<>();

    public static PlayerShip PLAYER;

    public static void addShip(Ship s) {
        ships.put(s.getName(), s);
    }

    public static void removeShip(Ship s) {
        if (ships.containsKey(s.getName()) && ships.containsValue(s)) ships.remove(s.getName());
    }

    public static void removeShip(String name) {
        if (ships.containsKey(name)) ships.remove(name);
    }

    public static void updateShip(String name, Vector2f pos, Vector2f delta, float rot) {
        getShip(name).updateInfo(pos, delta, rot);
    }

    public static void updateShip(String name, Vector2f pos, float rot) {
        Ship s = getShip(name);
        s.setPos(pos);
        s.setRotation(rot);
    }

    public static void updateShip(String name, float armor, float shields) {
        Ship s = getShip(name);
        s.setStats(armor, shields);
    }

    public static void updateShip(String name, boolean accel) {
        Ship s = getShip(name);
        s.setAccelerating(accel);
    }

    public static Ship getShip(String name) {
        return ships.get(name);
    }

    public static void respawnPlayerShip() {
        if (PLAYER != null) Globals.remove(PLAYER);
        PLAYER = new PlayerShip(0, 0, 0);
        Globals.add(PLAYER);
    }
}
