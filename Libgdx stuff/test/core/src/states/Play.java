package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import handlers.GameStateManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.*;
import entities.HUD;
import entities.Player;
import handlers.B2DVariables;
import static handlers.B2DVariables.PPM;
import handlers.MyContactListener;
import handlers.MyInput;
import main.Game;

public class Play extends GameState {
    
    private World world;
    private final Box2DDebugRenderer bdr;
    
    private OrthographicCamera b2dCam;

    private MyContactListener cl;
    
    private TiledMap tileMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer tmr;
    
    private Player player;
    
    private HUD hud;
    
    public Play(GameStateManager gsm){
        super(gsm);
        
        // set up box2d stuff
        world = new World(new Vector2(0, -9.81f), true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        bdr = new Box2DDebugRenderer();
        
        // create tiles
        createTiles();
        
        // create springs
        createSprings();
        
        // create water
        createWater();
        
        // create spikes
        createSpikes();
        
        // create finish
        createFlag();
        
        // create player
        createPlayer();
        
         
        // set up box2d cam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Game.V_WIDTH/PPM, Game.V_HEIGHT/PPM);
        
        // set up HUD
        hud = new HUD(player);
        
        
        ////////////////////////////////////////////////////
        
        
        
        
    }
    
    public void handleInput(){
        
        // player jump
        if(MyInput.isPressed(MyInput.BUTTON1)){
            if(cl.isPlayerOnGround()){
                if(cl.isPlayerOnSpring()){
                    player.getBody().applyForceToCenter(0, 450, true);
                }
                else{
                    player.getBody().applyForceToCenter(0, 300, true);
                }
            }
        }
        if(MyInput.isDown(MyInput.BUTTON2)){
            player.getBody().setLinearVelocity(new Vector2((float)-2.5, 0));
        }
        if(MyInput.isReleased(MyInput.BUTTON2)){
            player.getBody().setLinearVelocity(new Vector2(0, 0));
        }
        
        if(MyInput.isDown(MyInput.BUTTON3)){
            player.getBody().setLinearVelocity(new Vector2((float)2.5, 0));
        }
        if(MyInput.isReleased(MyInput.BUTTON3)){
            player.getBody().setLinearVelocity(new Vector2(0, 0));
        }
    }
    
    
    public void update(float dt){
        handleInput();
        world.step(dt, 6, 2);
        player.update(dt);
        if(cl.isPlayerDead() || cl.playerWon()){
            gsm.setState(GameStateManager.MENU);
        }
        
    }
    
    public void render(){
        
      // clear screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // set camera to follow player
        
        setcam();
                
        // draw tile map
        tmr.setView(cam);
        tmr.render();
        
        //draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);
        
        //draw hud
        sb.setProjectionMatrix(hudcam.combined);
        hud.render(sb);
        
        //draw box2d
        //bdr.render(world, b2dCam.combined);
        
       
    }
    
    public void dispose(){}
    
    private void createPlayer(){
        PolygonShape shape = new PolygonShape();
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        
        
        bdef.position.set(Game.V_WIDTH/4/PPM, Game.V_HEIGHT/2/PPM);
        bdef.type = BodyType.DynamicBody;
        Body body = world.createBody(bdef);
        
        shape.setAsBox(8/PPM, 15/PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVariables.BIT_PLAYER;
        fdef.filter.maskBits = B2DVariables.BIT_GROUND;
        body.createFixture(fdef).setUserData("player");

        // create foot sensor
        shape.setAsBox(7/PPM, 4/PPM, new Vector2(0,-15/PPM),0);
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVariables.BIT_PLAYER;
        fdef.filter.maskBits = B2DVariables.BIT_GROUND | B2DVariables.BIT_SPRING | B2DVariables.BIT_DEATH | B2DVariables.BIT_FINISH;
        fdef.isSensor = true;
        body.createFixture(fdef).setUserData("foot");
        
        // create player
        player = new Player(body);
        player.setHealth(5);
        player.setAttack(1);
    }
    
    private void createTiles(){
        
        // load tile map
        tileMap = new TmxMapLoader().load("res/maps/Map.tmx");
        tmr = new OrthogonalTiledMapRenderer(tileMap);
        
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get("Ground");
        tileSize = tileMap.getProperties().get("tilewidth", Integer.class);
        createGroundLayer(layer);
    }
    
    private void createGroundLayer(TiledMapTileLayer layer){
        PolygonShape shape = new PolygonShape();
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        
        for(int row=0; row < layer.getHeight(); row++){
            for(int col = 0; col < layer.getWidth(); col++){
                Cell cell = layer.getCell(col, row);
                
                if(cell == null) continue;
                if(cell.getTile() == null) continue;
                
                bdef.type = BodyType.StaticBody;
                bdef.position.set(new Vector2((col+.5f) * tileSize / PPM, (row + .5f) * tileSize / PPM ));
                
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[4];
                v[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);
                v[1] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
                v[2] = new Vector2(tileSize/ 2 / PPM, tileSize / 2 / PPM);
                v[3] = new Vector2(tileSize/ 2 / PPM, -tileSize / 2 / PPM);
                cs.createChain(v);
                fdef.friction = 0;
                fdef.shape = cs;
                fdef.filter.categoryBits = B2DVariables.BIT_GROUND;
                fdef.filter.maskBits = B2DVariables.BIT_PLAYER;
                fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef);
            }
        }
    }
    
    
    private void createSprings(){
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get("Spring");
        tileSize = tileMap.getProperties().get("tilewidth", Integer.class);
        createSpringLayer(layer);
    }
    
    private void createSpringLayer(TiledMapTileLayer layer){
        PolygonShape shape = new PolygonShape();
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        
        for(int row=0; row < layer.getHeight(); row++){
            for(int col = 0; col < layer.getWidth(); col++){
                Cell cell = layer.getCell(col, row);
                
                if(cell == null) continue;
                if(cell.getTile() == null) continue;
                
                bdef.type = BodyType.StaticBody;
                bdef.position.set(new Vector2((col+.5f) * tileSize / PPM, (row + .5f) * tileSize / PPM ));
                
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[2];
                v[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 4 / PPM);
                v[1] = new Vector2(tileSize/ 2 / PPM, -tileSize / 4 / PPM);
                cs.createChain(v);
                fdef.friction = 0;
                fdef.shape = cs;
                fdef.filter.categoryBits = B2DVariables.BIT_SPRING;
                fdef.filter.maskBits = B2DVariables.BIT_PLAYER;
                fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef).setUserData("spring");
            }
        }
    }
    private void createWater(){
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get("Water");
        tileSize = tileMap.getProperties().get("tilewidth", Integer.class);
        createWaterLayer(layer);
    }
    
    private void createWaterLayer(TiledMapTileLayer layer){
        PolygonShape shape = new PolygonShape();
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        
        for(int row=0; row < layer.getHeight(); row++){
            for(int col = 0; col < layer.getWidth(); col++){
                Cell cell = layer.getCell(col, row);
                
                if(cell == null) continue;
                if(cell.getTile() == null) continue;
                
                bdef.type = BodyType.StaticBody;
                bdef.position.set(new Vector2((col+.5f) * tileSize / PPM, (row + .5f) * tileSize / PPM ));
                
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[2];
                v[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);
                v[1] = new Vector2(tileSize/ 2 / PPM, -tileSize / 2 / PPM);
                cs.createChain(v);
                fdef.friction = 0;
                fdef.shape = cs;
                fdef.filter.categoryBits = B2DVariables.BIT_DEATH;
                fdef.filter.maskBits = B2DVariables.BIT_PLAYER;
                world.createBody(bdef).createFixture(fdef).setUserData("water");
            }
        }
    }
    
    
    private void createSpikes(){
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get("Spikes");
        tileSize = tileMap.getProperties().get("tilewidth", Integer.class);
        createSpikeLayer(layer);
    }
    
    private void createSpikeLayer(TiledMapTileLayer layer){
        PolygonShape shape = new PolygonShape();
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        
        for(int row=0; row < layer.getHeight(); row++){
            for(int col = 0; col < layer.getWidth(); col++){
                Cell cell = layer.getCell(col, row);
                
                if(cell == null) continue;
                if(cell.getTile() == null) continue;
                
                bdef.type = BodyType.StaticBody;
                bdef.position.set(new Vector2((col+.5f) * tileSize / PPM, (row + .5f) * tileSize / PPM ));
                
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[4];
                v[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);
                v[1] = new Vector2(-tileSize / 2 / PPM, -tileSize / 8 / PPM);
                v[2] = new Vector2(tileSize/ 2 / PPM, -tileSize / 8 / PPM);
                v[3] = new Vector2(tileSize/ 2 / PPM, -tileSize / 2 / PPM);
                cs.createChain(v);
                fdef.friction = 0;
                fdef.shape = cs;
                fdef.filter.categoryBits = B2DVariables.BIT_DEATH;
                fdef.filter.maskBits = B2DVariables.BIT_PLAYER;
                world.createBody(bdef).createFixture(fdef).setUserData("spikes");
            }
        }
    }
    
    private void createFlag(){
        TiledMapTileLayer layer = (TiledMapTileLayer) tileMap.getLayers().get("Finish");
        tileSize = tileMap.getProperties().get("tilewidth", Integer.class);
        createFlagLayer(layer);
    }
    
    private void createFlagLayer(TiledMapTileLayer layer){
        PolygonShape shape = new PolygonShape();
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        
        for(int row=0; row < layer.getHeight(); row++){
            for(int col = 0; col < layer.getWidth(); col++){
                Cell cell = layer.getCell(col, row);
                
                if(cell == null) continue;
                if(cell.getTile() == null) continue;
                
                bdef.type = BodyType.StaticBody;
                bdef.position.set(new Vector2((col+.5f) * tileSize / PPM, (row + .5f) * tileSize / PPM ));
                
                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[4];
                v[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);
                v[1] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
                v[2] = new Vector2(tileSize/ 2 / PPM, tileSize / 2 / PPM);
                v[3] = new Vector2(tileSize/ 2 / PPM, -tileSize / 2 / PPM);
                cs.createChain(v);
                fdef.friction = 0;
                fdef.shape = cs;
                fdef.filter.categoryBits = B2DVariables.BIT_FINISH;
                fdef.filter.maskBits = B2DVariables.BIT_PLAYER;
                world.createBody(bdef).createFixture(fdef).setUserData("flag");
            }
        }
    }
    
    
    private void setcam(){
//        if(player.getPosition().y <= Game.V_HEIGHT/2){
//            cam.position.set(player.getPosition().x * PPM + Game.V_WIDTH/4,Game.V_HEIGHT/2,0);
//        }
//        else{
            cam.position.set(player.getPosition().x * PPM + Game.V_WIDTH/4,player.getPosition().y * PPM + Game.V_HEIGHT/8,0);
//        }
        cam.update();
    }
   
}
