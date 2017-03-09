package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.CameraControl;
import org.lwjgl.opengl.Display;

public class PlayerControl extends AbstractControl implements AnimEventListener
{
    private Node player;
    private BulletAppState bulletAppState;
    private AssetManager assetManager;
    private InputManager inputManager;
    private Camera cam;
    private CameraNode camNode;
    private RigidBodyControl phy;
    private AnimControl control;
    private AnimChannel channel;
    private boolean rotate=false, alive=true;
    private int gear=0, last_gear=0;
    private float x=0.0f, y=0.0f;
    private double speed = 0.0;
    
    public PlayerControl(Node player, BulletAppState bulletAppState,
            AssetManager assetManager, InputManager inputManager,
            Camera cam)
    {
        this.player = player;
        this.bulletAppState = bulletAppState;
        this.assetManager = assetManager;
        this.inputManager = inputManager;
        this.cam = cam;
        setPlayer();
        setCam();
    }
    
    private void setPlayer()
    {
        Spatial model = assetManager.loadModel("Models/Sheep/sheep.mesh.j3o");
        player.attachChild(model);
        Material mat = new Material( 
            assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", 
            assetManager.loadTexture(new TextureKey("Models/Sheep/t_sheep3.png", false)));
        player.setMaterial(mat);
        player.setLocalTranslation(1000, 1000, 100);
        player.setUserData("Health", 100);
        
        control = model.getControl(AnimControl.class);
        control.addListener(this);
        channel = control.createChannel();
        channel.setAnim("sky");
        channel.setLoopMode(LoopMode.Loop);
        
        CollisionShape shape = CollisionShapeFactory.createDynamicMeshShape(player);
        phy = new RigidBodyControl(shape, 50);
        player.addControl(phy);
        bulletAppState.getPhysicsSpace().add(phy);
        phy.setAngularFactor(0);
    }
    
    private void setCam()
    {
        camNode = new CameraNode("Camera Node", cam);
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        player.attachChild(camNode);
        camNode.setLocalTranslation(new Vector3f(0, 5, -20));
        camNode.lookAt(player.getLocalTranslation(), Vector3f.UNIT_Y);
    }
    
    void setGear(int gear)
    {
        last_gear = this.gear;
        this.gear = gear;
    }
    
    void setRotate(boolean rotate)
    {
        this.rotate = rotate;
    }

    @Override
    protected void controlUpdate(float tpf)
    {
        if(alive)
        {
            if(rotate)
            {
                x = inputManager.getCursorPosition().x - Display.getWidth()/2;
                y = inputManager.getCursorPosition().y - Display.getHeight()/2;

                phy.setPhysicsRotation(phy.getPhysicsRotation().
                    mult(new Quaternion().fromAngleAxis(
                    -FastMath.PI * y / 200000, Vector3f.UNIT_X)));

                phy.setPhysicsRotation(phy.getPhysicsRotation().
                    mult(new Quaternion().fromAngleAxis(
                    -FastMath.PI * x / 200000, Vector3f.UNIT_Y)));
            }

            if(speed != gear*50)
            {
                if(gear > last_gear)
                    speed = Math.min(gear*50, speed+Math.sqrt(
                            Math.abs(speed))*0.01+0.0001);
                else
                    speed = Math.max(gear*50, speed-Math.sqrt(
                            Math.abs(speed))*0.02-0.0001);
            }
            else
                last_gear = gear;
            phy.setLinearVelocity(cam.getDirection().normalizeLocal().mult((float)speed));
            
            if(player.getUserData("Health").equals(0))
            {
                alive = false;
                SetAudio sound = new SetAudio(assetManager, player.getParent()
                        .getParent(), "explosive");
                player.removeFromParent();
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp)
    {
        //Unused
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName)
    {
        //Unused
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName)
    {
        //Unused
    }
}
