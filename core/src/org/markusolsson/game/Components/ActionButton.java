package org.markusolsson.game.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.markusolsson.game.Helpers.Actions;
import org.markusolsson.game.Sprites.Player;

/**
 * Created by markus on 2015-09-06.
 */
public class ActionButton {
    private Sprite skin;
    private Texture texture;
    private Player player;
    private Actions action;

    public ActionButton(String piTexture, float x, float y, float width, float height, Player inPlayer, Actions piAction) {
        this.action = piAction;
        player = inPlayer;
        texture = new Texture(piTexture);
        skin = new Sprite(texture); // your image
        skin.setPosition(x, y);
        skin.setSize(width, height);
    }
    public ActionButton(float x, float y, float width, float height, Player inPlayer, Actions piAction) {
        this.action = piAction;
        texture = new Texture("empty.png");
        player = inPlayer;
        skin = new Sprite(texture); // your image
        skin.setPosition(x, y);
        skin.setSize(width, height);
    }
    public void dispose()
    {
        texture.dispose();
        player.dispose();
    }

    public void update (SpriteBatch batch, float input_x, float input_y) {
        checkIfClicked(input_x, input_y);
        skin.draw(batch); // draw the button
    }

    private void checkIfClicked (float ix, float iy) {
        if (ix > skin.getX() && ix < skin.getX() + skin.getWidth()) {
            if (iy > skin.getY() && iy < skin.getY() + skin.getHeight()) {
                switch(action)
                {
                    case UP:
                        player.jump();
                        break;
                    case DOWN:
                        break;
                    case LEFT:
                        player.walkLeft();
                        break;
                    case RIGHT:
                        player.walkRight();
                        break;
                    case SHOOT:
                        player.shoot();
                        break;
                }
            }
        }
    }
    public float convertToPercents_width (float p) {
        return Gdx.graphics.getWidth()*p/100;
    }

    public float convertToPercents_height (float p) {
        return Gdx.graphics.getHeight()*p/100;
    }
}
