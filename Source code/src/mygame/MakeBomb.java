package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class MakeBomb
{
    private BulletAppState bulletAppState;
    private AssetManager assetManager;
    private Node shootable, enemy;
    private Spatial bomb;
    private Vector3f spawnPoint, dir;
    
    public MakeBomb(Vector3f spawnPoint, BulletAppState bulletAppState,
            Node shootable, AssetManager assetManager, Vector3f dir)
    {
        this.bulletAppState = bulletAppState;
        this.assetManager = assetManager;
        this.shootable = shootable;
        this.dir = dir;
        this.spawnPoint = spawnPoint;
        makeBomb();
    }
    
     private void makeBomb()
    {
        // Set bomb
        bomb = assetManager.loadModel("Models/Bomb/bomb.mesh.j3o");
        Material mat = new Material( 
            assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", 
            assetManager.loadTexture(new TextureKey("Models/Bomb/bomb.jpg", false)));
        bomb.setMaterial(mat);
        bomb.setLocalTranslation(spawnPoint.add(dir.mult(10)));
        bomb.addControl(new BombControl(bomb, bulletAppState,
                spawnPoint, dir, shootable));
        bomb.setUserData("Damage", 1);
        if(shootable.getName().equals("shootable"))
            shootable.getParent().attachChild(bomb);
    }
}
