package org.markusolsson.game.Network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import org.markusolsson.game.Models.MPPlayerModel;
import org.markusolsson.game.Packets.AddPlayer;
import org.markusolsson.game.Packets.RemovePlayer;
import org.markusolsson.game.Packets.UpdateBullet;
import org.markusolsson.game.Packets.UpdatePlayer;
import org.markusolsson.game.States.PlayState;

import java.io.IOException;

/**
 * Created by markus on 2015-09-04.
 */
public class ServerListener extends Listener {
    public Client client;
    //public String ip = "192.168.0.139";
    public String ip = "31.211.243.89";
    //public String ip = "localhost";
    int port = 27960;
    private PlayState ps;
    private Sound connected;
    private Sound disconnected;
    private Float lastValue = null;
    public ServerListener(PlayState inPs)
    {
        this.ps = inPs;
    }

    public void connect() {
        client = new Client();
        client.getKryo().register(UpdateBullet.class);
        client.getKryo().register(UpdatePlayer.class);
        client.getKryo().register(AddPlayer.class);
        client.getKryo().register(RemovePlayer.class);
        client.addListener(this);
        client.start();
        connected = Gdx.audio.newSound(Gdx.files.internal("connected.mp3"));
        disconnected = Gdx.audio.newSound(Gdx.files.internal("disconnected.mp3"));
        try {
            client.connect(5000, ip, port, port);
        } catch (IOException e) {
            System.out.println("Unable to connect to server");
        }
    }
    public void disconnect()
    {
        client.stop();
        client.close();
    }
    public void received(Connection c, Object o){
        if(o instanceof AddPlayer){
            AddPlayer packet = (AddPlayer) o;
            connected.play();
            MPPlayerModel newPlayer = new MPPlayerModel();
            ps.mpPlayers.put(packet.id, newPlayer);

        }else if(o instanceof RemovePlayer){
            RemovePlayer packet = (RemovePlayer) o;
            disconnected.play();
            ps.connectedPlayers--;
            ps.mpPlayers.remove(packet.id);
            ps.players.remove(packet.id);

        }else if(o instanceof UpdatePlayer){
            UpdatePlayer packet = (UpdatePlayer) o;
            ps.mpPlayers.get(packet.id).x = packet.x;
            ps.mpPlayers.get(packet.id).y = packet.y;
            ps.mpPlayers.get(packet.id).health = packet.health;
            ps.mpPlayers.get(packet.id).isLeft = packet.isLeft;
        }else if(o instanceof UpdateBullet){
            UpdateBullet packet = (UpdateBullet) o;
            UpdateBullet bullet = new UpdateBullet();
            bullet.x = packet.x;
            bullet.y = packet.y;
            bullet.isLeft = packet.isLeft;
            bullet.id = packet.id;
            ps.mPBullets.put(packet.id, bullet);
        }
    }
}
