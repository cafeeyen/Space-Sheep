package mygame;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

public class BombControl extends AbstractControl
{
    private Spatial bomb;
    private BulletAppState bulletAppState;
    private boolean alive=true;
    private Vector3f spawnPoint, dir;
    private float distance=0;
    private CollisionResults results;
    private Ray ray;
    private Node shootable;
    private Node target;
    
    public BombControl(Spatial bomb, BulletAppState bulletAppState,
            Vector3f spawnPoint, Vector3f dir, Node shootable)
    {
        this.bomb = bomb;
        this.bulletAppState = bulletAppState;
        this.spawnPoint = spawnPoint;
        this.dir = dir;
        this.shootable = shootable;
        
        RigidBodyControl phy = new RigidBodyControl(0.1f);
        bomb.addControl(phy);
        bulletAppState.getPhysicsSpace().add(phy);
        phy.setLinearVelocity(dir.multLocal(350.0f));
        phy.setAngularVelocity(Vector3f.ZERO);
        
        results = new CollisionResults();
    }

    private void delete()
    {
        alive = false;
        bomb.removeFromParent();
        bomb.removeControl(this);
        bomb = null;
    }
    
    @Override
    protected void controlUpdate(float tpf)
    {
        if(alive)
        {
            ray = new Ray(bomb.getLocalTranslation(), dir);
            shootable.collideWith(ray, results);
            distance = spawnPoint.distance(bomb.getLocalTranslation());
            
            if(distance >= 900.0f)
                delete();
            
            else if(!bomb.getControl(RigidBodyControl.class)
                    .getAngularVelocity().equals(Vector3f.ZERO))
            {
                target = results.getClosestCollision().getGeometry()
                        .getParent().getParent();
                if(target.getName().equals("Player"))
                {                    
                    target.setUserData("Health",
                            (Integer)target.getUserData("Health")
                            -(Integer)bomb.getUserData("Damage"));
                }
                delete();
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp)
    {
        // Unused
    }
}
