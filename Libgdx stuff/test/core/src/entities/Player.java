
package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import main.Game;

public class Player extends B2DSprite {
    private int health;
    private int attack;
    
    
    public Player(Body body) {
        
        super(body);
        
        Texture tex = Game.res.getTexture("KingWalk");
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
        setAnimation(sprites, 1/12f);
    }
    
    public int getHealth(){
        return health;
    }
    public int getAttack(){
        return attack;
    }
    public void setHealth(int heal){
        health = heal;
    }
    public void setAttack(int atk){
        attack = atk;
    }
}
