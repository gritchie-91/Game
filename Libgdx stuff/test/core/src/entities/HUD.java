
package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import main.Game;

public class HUD {
    private Player player;
    
    private TextureRegion health;
    
    public HUD(Player player){
        this.player = player;
        
//        Texture tex = Game.res.getTexture("hud");
//        
//        health = new TextureRegion();
//            health = new TextureRegion(tex, 0, 94, 53, 45);
    }
    
    public void render(SpriteBatch sb){
        sb.begin();
//        sb.draw(health, 150, 620);
        sb.end();
    }
}
