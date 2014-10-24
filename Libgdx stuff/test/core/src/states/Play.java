package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import handlers.GameStateManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.*;
import handlers.B2DVariables;
import static handlers.B2DVariables.PPM;
import handlers.MyContactListener;
import handlers.MyInput;
import main.Game;

/**
 *
 * @author Garrison
 */
public class Play extends GameState {
    
    private World world;
    private final Box2DDebugRenderer bdr;
    
    private OrthographicCamera b2dCam;
    
    private Body playerBody;
    private MyContactListener cl;
    
    public Play(GameStateManager gsm){
        super(gsm);
        world = new World(new Vector2(0, -9.81f), true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        
        bdr = new Box2DDebugRenderer();
        
        // create ground
        BodyDef bdef = new BodyDef();
        bdef.position.set(160/PPM, 120/PPM);
        bdef.type = BodyType.StaticBody;
        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50/PPM,5/PPM);
        FixtureDef fdef = new FixtureDef();   
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVariables.BIT_GROUND;
        fdef.filter.maskBits = B2DVariables.BIT_PLAYER;
        body.createFixture(fdef).setUserData("ground");
        
        // create player
        bdef.position.set(160/PPM, 250/PPM);
        bdef.type = BodyType.DynamicBody;
        playerBody = world.createBody(bdef);
        
        shape.setAsBox(5/PPM, 5/PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVariables.BIT_PLAYER;
        fdef.filter.maskBits = B2DVariables.BIT_GROUND;
        playerBody.createFixture(fdef).setUserData("player");

        // create foot sensor
        shape.setAsBox(2/PPM, 2/PPM, new Vector2(0,-5/PPM),0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVariables.BIT_PLAYER;
        fdef.filter.maskBits = B2DVariables.BIT_GROUND;
        fdef.isSensor = true;
        playerBody.createFixture(fdef).setUserData("foot");
         
        // set up box2d cam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Game.V_WIDTH/PPM, Game.V_HEIGHT/PPM);
    }
    
    public void handleInput(){
        
        // player jump
        if(MyInput.isPressed(MyInput.BUTTON1)){
            if(cl.isPlayerOnGround()){
                playerBody.applyForceToCenter(0, 200, true);
            }
        }
    }
    
    
    public void update(float dt){
        handleInput();
        world.step(dt, 6, 2);
        
    }
    
    public void render(){
        
      // clear screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        bdr.render(world, b2dCam.combined);
    }
    
    public void dispose(){}
   
}
