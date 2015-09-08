package org.markusolsson.game.States;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.markusolsson.game.TenAddaTen;

/**
 * Created by marku on 2015-08-31.
 */
public class MenuState extends State {
    private Texture background;
    private Texture playBtn;

    public MenuState(GameStateManager inGsm) {
        super(inGsm);
        background = new Texture("bg.jpg");
        playBtn = new Texture("play_button.png");
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
            sb.draw(playBtn, (Gdx.graphics.getWidth()/ 2) - (playBtn.getWidth() / 2), Gdx.graphics.getHeight() / 2);
        }
        else {
            sb.draw(background, 0, 0, TenAddaTen.WIDTH, TenAddaTen.HEIGHT);
            sb.draw(playBtn, (TenAddaTen.WIDTH / 2) - (playBtn.getWidth() / 2), TenAddaTen.HEIGHT / 2);
        }
        sb.end();
    }
    public void dispose(){
        background.dispose();
        playBtn.dispose();
    }
}
