package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetStage
{
    private BulletAppState bulletAppState;
    private AssetManager assetManager;
    private Node shootable;
    
    // Constructor
    SetStage(BulletAppState bulletAppState, AssetManager assetManager,
            Node shootable)
    {
        this.bulletAppState = bulletAppState;
        this.assetManager = assetManager;
        this.shootable = shootable;
        
        try
        {
            setupMap();
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(SetStage.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(SetStage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setupMap() throws FileNotFoundException, IOException
    {
        // Setup sky(bg)
        Spatial sky = SkyFactory.createSky(assetManager, "Scenes/spacebgnya.dds", false);
        sky.setQueueBucket(RenderQueue.Bucket.Sky);
        sky.setCullHint(Spatial.CullHint.Never);
        shootable.attachChild(sky);
        
        // Setup light
        PointLight lamp_light = new PointLight();
        lamp_light.setColor(ColorRGBA.White);
        lamp_light.setRadius(4f);
        lamp_light.setPosition(new Vector3f(0, 0, 0));
        shootable.addLight(lamp_light);
        
        // Setup wall
        Material wall_mat = new Material( 
            assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wall_mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        TextureKey wall_key = new TextureKey("Textures/wall.png");
        wall_key.setGenerateMips(true);
        Texture wall_tex = assetManager.loadTexture(wall_key);
        wall_tex.setWrap(Texture.WrapMode.Repeat);        
        wall_mat.setTexture("ColorMap", wall_tex);
        
        setWall(1000, 5, 6000, wall_mat);
        setWall(5, 1000, 6000, wall_mat);
        setWall(1000, 1000, 5, wall_mat);
        
        // Setup planet
        MakePlanet planet = new MakePlanet(bulletAppState, assetManager, shootable);
        
        // Setup obstacle
        MakeObstacle obstacle = new MakeObstacle(bulletAppState, assetManager, shootable);
        
        //Setup item
        MakeItem item = new MakeItem(bulletAppState, assetManager, shootable);
    }
    
    private void setWall(float x, float y, float z, Material mat)
    {
        for(int i=0;i<=2;i+=2)
        {
            Geometry wall = new Geometry("Box", new Box(x, y, z));
            wall.setQueueBucket(RenderQueue.Bucket.Transparent);
            wall.setMaterial(mat);
            shootable.attachChild(wall);
        
            RigidBodyControl phy = new RigidBodyControl(0);
            wall.addControl(phy);
            
            if(x == 5)
                phy.setPhysicsLocation(new Vector3f(1000*i, 1000, 6000));
            else if(y == 5)
                phy.setPhysicsLocation(new Vector3f(1000, 1000*i, 6000));
            else
                phy.setPhysicsLocation(new Vector3f(1000, 1000, 6000*i));
            bulletAppState.getPhysicsSpace().add(phy);
        }
    }
}
