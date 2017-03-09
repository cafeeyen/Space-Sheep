package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

public class PlanetControl extends AbstractControl
{
    private Spatial planet;
    private BulletAppState bulletAppState;
    private RigidBodyControl phy;
    
    public PlanetControl(Spatial planet, BulletAppState bulletAppState)
    {
        this.planet = planet;
        this.bulletAppState = bulletAppState;
        
        phy = new RigidBodyControl(0);
        planet.addControl(phy);
        bulletAppState.getPhysicsSpace().add(phy);
    }
    
    @Override
    protected void controlUpdate(float tpf)
    {
        phy.setPhysicsRotation(phy.getPhysicsRotation()
                .mult(new Quaternion().fromAngleAxis(FastMath.PI/36000, Vector3f.UNIT_Y)));
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp)
    {
        // Unused
    }
    
}
