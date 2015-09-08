package org.markusolsson.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

import org.markusolsson.game.States.PlayState;
import org.markusolsson.game.TenAddaTen;

/**
 * Created by marku on 2015-08-31.
 */
public class Player {
    private static final int GRAVITY = -15;
    private int health = 100;
    private Vector3 position;
    private int id;

    private Vector3 velocity;
    private long lastJump = System.currentTimeMillis();
    private long lastShot = System.currentTimeMillis();
    private Sound jump;
    private boolean isLeft;

    private Texture player;
    private Sprite playerSprite;

    public Player(int x, int y){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        player = new Texture("player.png");
        playerSprite = new Sprite(player);
        jump = Gdx.audio.newSound(Gdx.files.internal("jump.mp3"));
    }

    public void update(float delta){
        if(position.y > TenAddaTen.HEIGHT - (TenAddaTen.HEIGHT - 100))
            velocity.add(0, GRAVITY, 0);
        velocity.scl(delta);
        playerSprite.setX(position.x);
        playerSprite.setY(position.y);
        position.add(velocity.x, velocity.y, 0);
        velocity.x = 0;
        if(position.y < TenAddaTen.HEIGHT - (TenAddaTen.HEIGHT - 100))
            position.y = TenAddaTen.HEIGHT - (TenAddaTen.HEIGHT - 100);

        if(position.x < -0.37658167)
            position.x = -0.37658167f;

        if(position.x > TenAddaTen.WIDTH - 70)
            position.x = TenAddaTen.WIDTH - 70;

        if(position.y > TenAddaTen.HEIGHT - 110)
            position.y = TenAddaTen.HEIGHT - 110;

        velocity.scl(1 /delta);
    }

    public Vector3 getPosition() {
        return position;
    }
    public void SetPosition(Vector3 pos)
    {
        position = pos;
        playerSprite.setPosition(pos.x, pos.y);
    }
    public boolean getIsLeft()
    {
        return isLeft;
    }
    public void setTexture(boolean left)
    {
        player.dispose();
        if(left == true) {
            playerSprite.setTexture(new Texture("playerleft.png"));
            isLeft = true;
        }
        else {
            playerSprite.setTexture(new Texture("player.png"));
            isLeft = false;
        }
    }
    public void jump(){
        if(lastJump < (System.currentTimeMillis() - 200))
        {
            jump.play();
            velocity.y = 300;
            lastJump = System.currentTimeMillis();
        }
    }
    public void walkRight(){
        player.dispose();
        playerSprite.setTexture(new Texture("player.png"));
        velocity.x = 350;
        isLeft = false;
    }
    public void walkLeft(){
        player.dispose();
        playerSprite.setTexture(new Texture("playerleft.png"));
        velocity.x = -350;
        isLeft = true;
    }
    public void shoot(){
        if(lastShot < (System.currentTimeMillis() - 200)) {
            PlayState.bullets.add(new Bullet(position.x, position.y, isLeft, false));
            lastShot = System.currentTimeMillis();
        }
    }
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
    public Sprite getPlayerSprite() {
        return playerSprite;
    }

    public void setPlayerSprite(Sprite playerSprite) {
        this.playerSprite = playerSprite;
    }
    public void dispose(){
        player.dispose();
        jump.dispose();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
