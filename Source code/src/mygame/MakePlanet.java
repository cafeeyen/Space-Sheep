package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class MakePlanet
{
    private AssetManager assetManager;
    private Node shootable;
    
    public MakePlanet(BulletAppState bulletAppState, AssetManager assetManager, Node shootable)
    {
        this.assetManager = assetManager;
        this.shootable = shootable;
        
        Material mat = new Material( 
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", 
        assetManager.loadTexture(new TextureKey("Models/Earth/earth.jpg", false)));
        Spatial earth = assetManager.loadModel("Models/Earth/earth.mesh.j3o");
        earth.setMaterial(mat);
        earth.scale(900);
        earth.setLocalTranslation(1000, 1000, -900);
        earth.addControl(new PlanetControl(earth, bulletAppState));
        shootable.attachChild(earth);
        
        mat = new Material( 
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", 
        assetManager.loadTexture(new TextureKey("Models/Moon/moon.jpg", false)));
        Spatial moon = assetManager.loadModel("Models/Moon/moon.mesh.j3o");
        moon.setMaterial(mat);
        moon.scale(300);
        moon.setLocalTranslation(1000, 1000, 11000);
        moon.addControl(new PlanetControl(moon, bulletAppState));
        shootable.attachChild(moon);
    }
}
