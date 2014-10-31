package handlers;

import java.util.Stack;
import main.Game;
import states.GameState;
import states.Menu;
import states.Play;

public class GameStateManager {
    
    private Game game;
    
    private Stack<GameState> gameStates;
    
    public static final int MENU = 83374329;
    public static final int PLAY = 900010;
    public static final int WIN = 789456;
    
    public GameStateManager(Game game){
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(MENU);
    }
    
    public Game game(){ 
        return game;
    }
    
    public void update(float dt){
        gameStates.peek().update(dt);
    }
    
    public void render(){
        gameStates.peek().render();
    }
    
    private GameState getState(int state){
        if(state == PLAY) 
            return new Play(this);
        else if(state == MENU)
            return new Menu(this);
        return null;
    }
    
    public void pushState(int state){
        gameStates.push(getState(state));
    }
    
    public void popState(int state){
        GameState g = gameStates.pop();
        g.dispose();
    }
    
    public void setState(int state){
        popState(state);
        pushState(state);
    }
}
