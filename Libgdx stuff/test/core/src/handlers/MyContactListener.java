package handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {
    
    private int numFootContacts;
    private boolean isOnSpring;
    private boolean playerDead;
    private boolean playerWin;
    
    public void beginContact(Contact c){
        
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();
        
        if(fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts++;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts++;
        }
        if(fa.getUserData() != null && fa.getUserData().equals("spikes")) {
            playerDead = true;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("spikes")) {
            playerDead = true;
        }
        if(fa.getUserData() != null && fa.getUserData().equals("water")) {
            playerDead = true;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("water")) {
            playerDead = true;
        }
        if(fa.getUserData() != null && fa.getUserData().equals("flag")) {
            playerWin = true;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("flag")) {
            playerWin = true;
        }
        if(fa.getUserData() != null && fa.getUserData().equals("spring")) {
            isOnSpring = true;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("spring")) {
            isOnSpring = true;
        }
    }
    
    public void endContact(Contact c){
    
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();
        
        if(fa.getUserData() != null && fa.getUserData().equals("foot")) {
            numFootContacts--;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("foot")) {
            numFootContacts--;
        }
        if(fa.getUserData() != null && fa.getUserData().equals("spring")) {
            isOnSpring = false;
        }
        if(fb.getUserData() != null && fb.getUserData().equals("spring")) {
            isOnSpring = false;
        }
    }
    
    public boolean isPlayerOnGround() { return numFootContacts > 0; }
    public boolean isPlayerDead() { return playerDead; }
    public boolean playerWon(){ return playerWin; }
    public boolean isPlayerOnSpring(){
        return isOnSpring;
    }
    
    
    public void preSolve(Contact c, Manifold m){}
    public void postSolve(Contact c, ContactImpulse ci){}
    
}
