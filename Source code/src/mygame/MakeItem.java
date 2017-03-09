package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class MakeItem
{
    private AssetManager assetManager;
    private Node shootable;
    
    public MakeItem(BulletAppState bulletAppState, AssetManager assetManager, Node shootable)
    {
        this.assetManager = assetManager;
        this.shootable = shootable;
        
        Material mat = new Material( 
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", 
        assetManager.loadTexture(new TextureKey("Models/Open/open.jpg", false)));
        Spatial boxOpen = assetManager.loadModel("Models/Open/open.mesh.j3o");
        boxOpen.setMaterial(mat);
        boxOpen.scale(20);
        boxOpen.setLocalTranslation(1000, 1000, 180);
        boxOpen.setLocalRotation(boxOpen.getLocalRotation()
                .mult(new Quaternion().fromAngleAxis(FastMath.PI/2, Vector3f.UNIT_X)));
        RigidBodyControl phy = new RigidBodyControl(0);
        boxOpen.addControl(phy);
        bulletAppState.getPhysicsSpace().add(phy);
        shootable.attachChild(boxOpen);
        
        mat = new Material( 
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", 
        assetManager.loadTexture(new TextureKey("Models/Close/close.jpg", false)));
        Spatial boxClose = assetManager.loadModel("Models/Close/close.mesh.j3o");
        boxClose.setMaterial(mat);
        boxClose.scale(20);
        phy = new RigidBodyControl(0);
        boxClose.setLocalTranslation(1000, 1000, 10500);
        boxClose.setLocalRotation(boxClose.getLocalRotation()
                .mult(new Quaternion().fromAngleAxis(FastMath.PI/2, Vector3f.UNIT_X)));
        boxClose.addControl(phy);
        bulletAppState.getPhysicsSpace().add(phy);
        shootable.attachChild(boxClose);
    }
}
