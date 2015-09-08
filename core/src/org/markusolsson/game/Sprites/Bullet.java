package org.markusolsson.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

import org.markusolsson.game.Packets.UpdateBullet;
import org.markusolsson.game.Packets.UpdatePlayer;
import org.markusolsson.game.States.PlayState;
import org.markusolsson.game.TenAddaTen;

/**
 * Created by marku on 2015-09-01.
 */

public class Bullet {
    private boolean isAlive = true;
    private Vector3 position;
    private Vector3 velocity;
    private boolean isLeft;
    private Sound gunshot;
    private Sound playerHit;

    private Texture bullet;
    private Sprite bulletSprite;

    public Bullet(float x, float y, boolean inIsLeft, boolean isSent){
        if(!isSent) {
            UpdateBullet updateBullet = new UpdateBullet();
            updateBullet.x = x;
            updateBullet.y = y;
            updateBullet.isLeft = inIsLeft;
            updateBullet.isInstansiated = false;
            PlayState.network.client.sendUDP(updateBullet);
        }
        this.isLeft = inIsLeft;
        if(isLeft)
        {
            velocity = new Vector3(-700, 0, 0);
            position = new Vector3(x - 50, y + 20, 0);
        }
        else {
            velocity = new Vector3(700, 0, 0);
            position = new Vector3(x + 100, y + 20, 0);

        }
        bullet = new Texture("bullet.png");
        bulletSprite = new Sprite(bullet);
        gunshot = Gdx.audio.newSound(Gdx.files.internal("gunshot.mp3"));
        playerHit = Gdx.audio.newSound(Gdx.files.internal("quack.mp3"));
        gunshot.play();
    }
    public void update(float delta){
        velocity.scl(delta);
        bulletSprite.setX(position.x);
        bulletSprite.setY(position.y);
        position.add(velocity.x, velocity.y, 0);
        if(position.y < 70)
            position.y = 70;

        if(position.x < -0.37658167){
            isAlive = false;
            dispose();
        }
        if(position.x > TenAddaTen.WIDTH) {
            isAlive = false;
            dispose();
        }
        velocity.scl(1 / delta);
        checkCollision();
    }
    private void checkCollision()
    {
        if(bulletSprite.getBoundingRectangle().overlaps(PlayState.player.getPlayerSprite().getBoundingRectangle()))
        {
            playerHit.play();
            int oldHealth = PlayState.player.getHealth();
            PlayState.player.setHealth(oldHealth - 10);
            isAlive = false;
            dispose();
        }
        for(Player p : PlayState.players.values())
        {
            if(bulletSprite.getBoundingRectangle().overlaps(p.getPlayerSprite().getBoundingRectangle()))
            {
                playerHit.play();
                int oldHealth = p.getHealth();
                p.setHealth(oldHealth - 10);
                isAlive = false;
                dispose();
            }
        }
    }

    public Vector3 getPosition() {
        return position;
    }
    public Sprite getBulletSprite() {
        return bulletSprite;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void dispose()
    {
        bullet.dispose();
        gunshot.dispose();
        playerHit.dispose();
    }

}
