package entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import main.Game;

public class MenuHUD {
    
    private TextureRegion play;
    
    public MenuHUD(){
        Texture tex = Game.res.getTexture("play");
        play = new TextureRegion(tex, 0, 0, 160, 48);
    }
    
    public void render(SpriteBatch sb){
        sb.begin();
        sb.draw(play, 150, 100);
        sb.end();
    }
}
