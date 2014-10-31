package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import static handlers.B2DVariables.PPM;
import handlers.GameButton;
import handlers.GameStateManager;
import handlers.MyInput;
import main.Game;

public class Menu extends GameState {
    
    private TextureRegion playButton;
    private GameButton play;
    private World world;
    private Box2DDebugRenderer b2dRenderer;
    
    public Menu(GameStateManager gsm){
        super(gsm);
        
        Texture tex = Game.res.getTexture("play");
        playButton = new TextureRegion(tex, 0, 0, 160, 48);
        play = new GameButton(playButton, 5 * PPM, 2 * PPM, cam);
        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);
        world = new World(new Vector2(0, -9.8f * 5), true);
        b2dRenderer = new Box2DDebugRenderer();
        
    }
    
    public void handleInput(){
        if(MyInput.isPressed(MyInput.BUTTON5)) {
            gsm.setState(GameStateManager.PLAY);
        }
        if(play.isClicked()){
            gsm.setState(GameStateManager.PLAY);
        }
    }
    
    public void update(float dt){
        handleInput();
    }
    
    public void render(){
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(cam.combined);
        play.render(sb);
    }
    
    public void dispose(){}
    
}
