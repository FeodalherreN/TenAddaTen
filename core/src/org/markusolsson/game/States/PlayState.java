package org.markusolsson.game.States;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import org.markusolsson.game.Models.MPPlayerModel;
import org.markusolsson.game.Network.ServerListener;
import org.markusolsson.game.Packets.UpdateBullet;
import org.markusolsson.game.Packets.UpdatePlayer;
import org.markusolsson.game.Sprites.Bullet;
import org.markusolsson.game.Sprites.Player;
import org.markusolsson.game.TenAddaTen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by markus on 2015-08-31.
 */
public class PlayState extends State {
    private Texture background;
    public static Player player;
    public static Player mp;

    private boolean isAndroid = false;

    private BitmapFont playerHP;

    public int connectedPlayers = 0;
    public Map<Integer,MPPlayerModel> mpPlayers;
    public static Map<Integer,Player> players;
    public static ServerListener network;
    public static ArrayList<Bullet> bullets;
    public Map<Integer, UpdateBullet> mPBullets;

    protected PlayState(GameStateManager inGsm) {
        super(inGsm);
        player = new Player(50, 300);
        background = new Texture("bg.jpg");
        mpPlayers = new HashMap<Integer,MPPlayerModel>();
        mPBullets = new HashMap<Integer, UpdateBullet>();
        players = new HashMap<Integer, Player>();
        network = new ServerListener(this);
        network.connect();
        bullets = new ArrayList<Bullet>();
        playerHP = new BitmapFont();
        CheckDevice();
    }

    private void CheckDevice()
    {
        switch(Gdx.app.getType())
        {
            case Android:
                isAndroid = true;
                break;
            case Desktop:
                break;
            default:
            break;
        }
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)){
            player.jump();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.walkRight();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            player.walkLeft();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            player.shoot();
        }
    }
    @Override
    public void update(float delta) {
        if(player.getHealth() <= 0)
        {
            gsm.set(new GameOverState(gsm, false));
            dispose();
        }
        if(!isAndroid)
            handleInput();
        player.update(delta);
        for(Bullet b : bullets)
        {
            b.update(delta);
        }

        //Send player
        UpdatePlayer updatePlayer = new UpdatePlayer();
        updatePlayer.x = player.getPosition().x;
        updatePlayer.y = player.getPosition().y;
        updatePlayer.health = player.getHealth();
        updatePlayer.isLeft = player.getIsLeft();
        network.client.sendUDP(updatePlayer);
    }
    @Override
    public void render(SpriteBatch sb) {
        sb.begin();

        //DRAWS BACKGROUND DEPENDING ON APPLICATIONTYPE
        if(Gdx.app.getType() == Application.ApplicationType.Android)
        {
            sb.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        else {
            sb.draw(background, 0, 0, TenAddaTen.WIDTH, TenAddaTen.HEIGHT);
        }

        //DRAWS THE PLAYER
        sb.draw(player.getPlayerSprite(), player.getPosition().x, player.getPosition().y, 150, 150);

        for(MPPlayerModel mpPlayer : mpPlayers.values())
        {
            if(!players.containsKey(mpPlayer.id)) {
                Vector3 vec = new Vector3();
                vec.add(mpPlayer.x, mpPlayer.y, 0);
                Player newPlayer = new Player(50, 300);
                newPlayer.SetPosition(vec);
                newPlayer.setTexture(mpPlayer.isLeft);
                sb.draw(newPlayer.getPlayerSprite(), newPlayer.getPosition().x, newPlayer.getPosition().y, 150, 150);
                players.put(mpPlayer.id, newPlayer);
            }
            else
            {
                Vector3 vec = new Vector3();
                vec.add(mpPlayer.x, mpPlayer.y, 0);
                players.get(mpPlayer.id).SetPosition(vec);
                players.get(mpPlayer.id).setTexture(mpPlayer.isLeft);
                sb.draw(players.get(mpPlayer.id).getPlayerSprite(), players.get(mpPlayer.id).getPosition().x, players.get(mpPlayer.id).getPosition().y, 150, 150);
            }
        }
        for(UpdateBullet b : mPBullets.values())
        {
            Bullet bull = new Bullet(b.x, b.y, b.isLeft, true);
            bullets.add(bull);
            mPBullets.remove(b.id);
        }


        //ITERATES THROUGH BULLETLIST AND CHECKS IF ANY BULLETS HAVE BEEN DISPOSED,
        // THEN IT GETS REMOVED
        for (Iterator<Bullet> iterator = bullets.iterator(); iterator.hasNext(); ) {
            Bullet b = iterator.next();
            if(b.isAlive()) {
                sb.draw(b.getBulletSprite(), b.getPosition().x, b.getPosition().y);
            }
            else
            {
                iterator.remove();
            }
        }

        //DRAW PLAYER HP
        playerHP.draw(sb, "Your HP: " + player.getHealth(), TenAddaTen.WIDTH - 1890, TenAddaTen.HEIGHT - 20);

        //RENDER ANDROID BUTTONS
        if(isAndroid) {
            float accY = Gdx.input.getAccelerometerY();
            if(accY < 0.0)
            {
                player.walkLeft();
            }
            if(accY > 0.0)
            {
                player.walkRight();
            }
            int x = Gdx.input.getX();
            x = 0;
            if(Gdx.input.isTouched()) {
                if (Gdx.input.getX() < TenAddaTen.WIDTH / 2) {
                    player.jump();
                } else if (Gdx.input.getX() > TenAddaTen.WIDTH / 2) {
                    player.shoot();
                }
            }
        }
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        player.dispose();
        network.disconnect();
        if(mp != null)
            mp.dispose();
        for (Iterator<Bullet> iterator = bullets.iterator(); iterator.hasNext(); ) {
            Bullet b = iterator.next();
            b.dispose();
        }
        for (Player p : players.values())
        {
            p.dispose();
        }
        playerHP.dispose();
        if(isAndroid) {
        }
    }
}
