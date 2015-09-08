package org.markusolsson.game.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * Created by marku on 2015-08-31.
 */
public class GameStateManager {

    private Stack<State> states;

    public GameStateManager(){
        states = new Stack<State>();
    }

    public void push(State state)
    {
        states.push(state);
    }
    public void pop()
    {
        states.pop();
    }
    public void set(State state)
    {
        states.pop();
        states.push(state);
    }
    public void update(float delta){
        states.peek().update(delta);
    }
    public void render(SpriteBatch sb)
    {
        states.peek().render(sb);
    }
}
