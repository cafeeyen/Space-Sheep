package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

public class ObstacleControl extends AbstractControl
{
    private Spatial object, model;
    private BulletAppState bulletAppState;
    private String line;
    RigidBodyControl phy;
    private int state = 0;
    private Vector3f start, end, dir;
    private boolean alive = true;
    
    public ObstacleControl(Spatial object, BulletAppState bulletAppState,
            String line)
    {
        this.object = object;
        this.bulletAppState = bulletAppState;
        this.line = line;
        
        CollisionShape shape = CollisionShapeFactory.createDynamicMeshShape(object);
        phy = new RigidBodyControl(shape, 500.0f);
        object.addControl(phy);
        phy.setKinematic(true);
        bulletAppState.getPhysicsSpace().add(phy);
        
        dir = new Vector3f(Integer.parseInt(line.split(" ")[3]),
                Integer.parseInt(line.split(" ")[4]),
                Integer.parseInt(line.split(" ")[5]));
        
        start = object.getLocalTranslation().clone();
        end = start.add(dir.negate());
        
        if(dir.equals(Vector3f.ZERO))
            alive = false;
        
        dir.negateLocal();
    }

    @Override
    protected void controlUpdate(float tpf)
    {
        if(alive)
        {
            object.setLocalTranslation(object.getLocalTranslation().add(dir.mult(0.0025f)));
            if(state == 0 && object.getLocalTranslation().equals(end))
            {
                dir.negateLocal();
                state = 1;
            }
            else if(state == 1 && object.getLocalTranslation().equals(start))
            {
                dir.negateLocal();
                state = 0;
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp)
    {
        //Unused
    }
    
}
