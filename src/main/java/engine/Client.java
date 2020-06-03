package engine;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import org.lwjgl.util.vector.Vector2f;

import assets.ShipManager;
import assets.game.objects.Projectile;
import assets.game.objects.Projectile.ProjectileType;
import assets.game.objects.ShipDisplay;

public class Client implements Runnable {

    public static final String sepChar = ":";
    public static int PORT = 8212;
    public static String HOST = "70.160.65.57"; // 70.160.65.57 - home
    private final Socket socket;
    private boolean running = true;
    Scanner chat;
    Scanner in;
    PrintWriter out;

    public Client(Socket s) {
        socket = s;
        try {
            chat = new Scanner(System.in);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void run() {
        try {
            send(Globals.userName());
            String input;
            while (in.hasNextLine()) {
                input = in.nextLine();
                processMSG(input);
            }

        } catch (Exception e) {
            System.out.println("Message: " + e.getMessage());
        }
    }

    public void disconnect() {
        sendMSG("/end", "");
    }

    private void processMSG(String msg) {
        //        System.out.println("recieved msg from server: " + msg);
        if (msg.startsWith("/end" + sepChar)) {
            ShipManager.removeShip(msg.substring(5));
            //            System.out.println("attempting remove on ship: " + msg.substring(5) + "
            // with org msg: " + msg);
        } else if (msg.startsWith("/pos" + sepChar)) {
            processPos(msg);
        } else if (msg.startsWith("/accel" + sepChar)) {
            processAccel(msg);
        } else if (msg.startsWith("/vitals" + sepChar)) {
            processVitals(msg);
        } else if (msg.startsWith("/chatmsg" + sepChar)) {
            processChatMSG(msg);
        } else if (msg.startsWith("/proj" + sepChar)) {
            processProj(msg);
        }
    }

    private void processPos(String input) {
        String[] info = input.substring(5).split(sepChar, 0);
        if (info.length < 4) {
            return;
        }
        String name = info[0];
        try {
            Vector2f pos = new Vector2f(Float.parseFloat(info[1]), Float.parseFloat(info[2]));
            float rot = Float.parseFloat(info[3]);
            if (name.equals(Globals.userName())) {
                return;
            }
            if (ShipManager.getShip(name) == null) {
                ShipManager.addShip(new ShipDisplay(pos, rot, new Vector2f(0, 0), name));
            } else {
                ShipManager.updateShip(name, pos, rot);
            }

        } catch (NumberFormatException e) {
            System.out.println("failed to update pos for " + name);
        }
    }

    //  USE DELTAS
    private void processProj(String input) {
        //        System.out.println("recieved proj: " + input);
        if (input.startsWith("/proj" + sepChar)) {
            input = input.substring(6);
        }
        String[] info = input.split(sepChar, 0);
        String name = null;
        Vector2f pos = null, delta = new Vector2f(0, 0);
        float angle = 0;
        int type = ProjectileType.LASER.getID();
        //        System.out.println(info.length + " length array consists of:" + info[0] + "," +
        // info[1] + "," + info[2] + "," + info[3] + "," + info[4] + "," + info[5] + "," + info[6]);
        try {
            if (info.length < 7) {
                throw new UnsupportedOperationException(
                        "Not enough arguments for projectile creation");
            }
            name = info[0];
            pos = new Vector2f(Float.parseFloat(info[1]), Float.parseFloat(info[2]));
            delta = new Vector2f(Float.parseFloat(info[3]), Float.parseFloat(info[4]));
            angle = Float.parseFloat(info[5]);
            type = Integer.parseInt(info[6]);

        } catch (UnsupportedOperationException | NumberFormatException ex) {
            System.out.println("-----" + ex.getMessage());
            System.out.println("failed to process /proj creation");
            System.out.println(
                    info.length
                            + " long array:"
                            + info[0]
                            + ","
                            + info[1]
                            + ","
                            + info[2]
                            + ","
                            + info[3]
                            + ","
                            + info[4]
                            + ","
                            + info[5]
                            + ","
                            + info[6]
                            + ","
                            + info[7]);
        }

        //        System.out.println("creating new projectile with: " + spawnX + " " + spawnY + " "
        // + deltaX + " " + deltaY + " " + angle + " " + type);

        Globals.add(new Projectile(pos, delta, angle, Projectile.convertID(type)));
    }

    private void processAccel(String input) {
        input = input.substring(7);
        String[] info = input.split(sepChar, 0);

        if (info.length < 2) {
            return;
        }
        String name = info[0];
        boolean accel = Boolean.parseBoolean(info[1]);
        if (name.equals(Globals.userName())) {
            return;
        }
        if (ShipManager.getShip(name) == null) {
            System.out.println(
                    "received accel with name: "
                            + name
                            + ", no ship exists, probably first acceleration (if this message only appears once)!");
        } else {
            ShipManager.updateShip(name, accel);
        }
    }

    private void processVitals(String input) {
        String[] info = input.substring(8).split(sepChar, 0);
        if (info.length < 3) {
            return;
        }
        String name = info[0];
        try {
            float armor = Float.parseFloat(info[1]), shields = Float.parseFloat(info[2]);
            if (name.equals(Globals.userName())) {
                return;
            }
            if (ShipManager.getShip(name) == null) {
                System.out.println(
                        "received vitals with name: "
                                + name
                                + ", no ship exists, may not have been created yet (if this message only appears once)!");
            } else {
                ShipManager.updateShip(name, armor, shields);
            }

        } catch (NumberFormatException e) {
            System.out.println("failed to update vitals for " + name);
        }
    }

    private void processChatMSG(String input) {
        System.out.println("chat: " + input.substring(9));
        // do chat
    }

    public void sendProj(
            double spawnX, double spawnY, double deltaX, double deltaY, double angle, int type) {
        //        System.out.println("sending: " + spawnX + Client.sepChar + spawnY + Client.sepChar
        // + deltaX + Client.sepChar + deltaY + Client.sepChar + angle + Client.sepChar + type);
        sendMSG(
                "/proj",
                spawnX
                        + Client.sepChar
                        + spawnY
                        + Client.sepChar
                        + deltaX
                        + Client.sepChar
                        + deltaY
                        + Client.sepChar
                        + angle
                        + Client.sepChar
                        + type);
    }

    public void sendProj(Projectile p) {
        sendMSG(
                "/proj",
                p.getX()
                        + Client.sepChar
                        + p.getY()
                        + Client.sepChar
                        + p.getDeltaX()
                        + Client.sepChar
                        + p.getDeltaY()
                        + Client.sepChar
                        + p.getRotation()
                        + Client.sepChar
                        + p.getType());
    }

    public void sendChatMSG(Object msg) {
        // regulate
        sendMSG("/chatmsg", msg);
    }

    public void sendPos(double x, double y, double rot) {
        sendMSG("/pos", x + Client.sepChar + y + Client.sepChar + rot);
    }

    public void sendVitals(double armor, double shields) {
        sendMSG("/vitals", armor + Client.sepChar + shields);
    }

    public void sendAccel(boolean accel) {
        sendMSG("/accel", Boolean.toString(accel));
    }

    private void send(String s) {
        out.println(s);
        out.flush();
    }

    /**
     * Sends the msg with the string as the command directly to the other end. USE WITH CAUTION!
     *
     * @param cmd the command to preface the object with
     * @param msg the object to send
     */
    public void sendMSG(String cmd, Object msg) {
        send(cmd + sepChar + msg);
    }

    /**
     * Sends the object directly to the other end. USE WITH CAUTION!
     *
     * @param msg the object to send
     */
    protected void sendMSG(Object msg) {
        send(msg.toString());
    }

    void end() {
        running = false;
        send("/end");
        Thread.currentThread().interrupt();
    }

    protected void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException ex) {
        }
    }
}
