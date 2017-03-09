package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.InputManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class MakeBullet
{
    private BulletAppState bulletAppState;
    private AssetManager assetManager;
    private InputManager inputManager;
    private Node shootable, player;
    private Camera cam;
    private Spatial bullet;
    private SetAudio bgm;
    
    // Getter
    MakeBullet(BulletAppState bulletAppState, AssetManager assetManager,
            InputManager inputManager, Node shootable, Node player, Camera cam,
            SetAudio bgm)
    {
        this.bulletAppState = bulletAppState;
        this.assetManager = assetManager;
        this.inputManager = inputManager;
        this.shootable = shootable;
        this.player = player;
        this.cam = cam;
        this.bgm = bgm;
        makeBullet();
    }
    
    private void makeBullet()
    {
        // Set direction
        Vector2f dir2D = inputManager.getCursorPosition();
        Vector3f dir3D = cam.getWorldCoordinates(new Vector2f(dir2D.x, dir2D.y), 0f)
                .clone();
        Vector3f dir  = cam.getWorldCoordinates(new Vector2f(dir2D.x, dir2D.y), 1f)
                .subtractLocal(dir3D).normalizeLocal();
        
        // Set bullet
        Vector3f spawnPoint = player.getControl(RigidBodyControl.class)
                .getPhysicsLocation().clone();
        bullet = assetManager.loadModel("Models/Star/star.j3o");
        bullet.setLocalRotation(player.getControl(RigidBodyControl.class)
                .getPhysicsRotation().clone()
                .mult(new Quaternion().fromAngleAxis(FastMath.PI/2, Vector3f.UNIT_Y)));
        bullet.setLocalTranslation(spawnPoint);
        bullet.addControl(new BulletControl(bullet, bulletAppState, spawnPoint,
                dir, shootable, assetManager, bgm));
        bullet.setUserData("Damage", 1);
        if(shootable.getName().equals("shootable"))
            shootable.getParent().attachChild(bullet);
    }
}
