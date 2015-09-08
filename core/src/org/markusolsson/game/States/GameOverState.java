package org.markusolsson.game.States;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.markusolsson.game.TenAddaTen;

/**
 * Created by markus on 2015-09-07.
 */
public class GameOverState extends State {
    private Texture background;

    public GameOverState(GameStateManager inGsm, boolean didAlfonzDie) {
        super(inGsm);
        background = new Texture("gameover.jpg");
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){
            gsm.set(new PlayState(gsm));
            dispose();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        if(Gdx.app.getType() == Application.ApplicationType.Android)
        {
            sb.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        else {
            sb.draw(background, 0, 0, TenAddaTen.WIDTH, TenAddaTen.HEIGHT);
        }
        sb.end();
    }
    public void dispose(){
        background.dispose();
    }
}
