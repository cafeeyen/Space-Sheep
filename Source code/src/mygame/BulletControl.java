package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

public class BulletControl extends AbstractControl
{
    private Spatial bullet;
    private BulletAppState bulletAppState;
    private Node shootable, target;
    private boolean alive=true;
    private Vector3f spawnPoint, endPoint, dir;
    private float distance=0, timer=0;
    private AssetManager assetManager;
    private ParticleEmitter flame;
    private CollisionResults results;
    private Ray ray;
    private SetAudio bgm;
    
    public BulletControl(Spatial bullet, BulletAppState bulletAppState,
            Vector3f spawnPoint, Vector3f dir, Node shootable,
            AssetManager assetManager, SetAudio bgm)
    {
        this.bullet = bullet;
        this.bulletAppState = bulletAppState;
        this.spawnPoint = spawnPoint;
        this.dir = dir;
        this.assetManager = assetManager;
        this.shootable = shootable;
        this.bgm = bgm;
        
        RigidBodyControl phy = new RigidBodyControl(0.1f);
        bullet.addControl(phy);
        bulletAppState.getPhysicsSpace().add(phy);
        phy.setLinearVelocity(dir.multLocal(500.0f));
        phy.setAngularVelocity(Vector3f.ZERO);
        
        results = new CollisionResults();
    }
    
    private void delete()
    {
        alive = false;
        endPoint = bullet.getLocalTranslation().clone();
        bullet.setCullHint(Spatial.CullHint.Always);
        if(distance < 500)
            flame();
        else
        {
            bullet.removeFromParent();
            bullet.removeControl(this);
            bullet = null;
        }
    }
    
    private void flame()
    {
        flame = new ParticleEmitter("flame", ParticleMesh.Type.Triangle, 10);
        Material mat = new Material(assetManager, 
                "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture(
                "Effects/Explosion/star.png"));
        flame.setMaterial(mat);
        
        flame.setImagesX(1); //size texture animation import
        flame.setImagesY(1);
        flame.setStartSize(1); //display size
        flame.setEndSize(2);

        flame.setRotateSpeed(4);
        flame.setSelectRandomImage(true);

        flame.setLocalTranslation(endPoint);
        flame.setStartColor(ColorRGBA.Red); //set color
        flame.setEndColor(ColorRGBA.Blue);
        
        shootable.getParent().attachChild(flame); //create star
    }
    
    @Override
    protected void controlUpdate(float tpf)
    {
        if(alive)
        {
            ray = new Ray(bullet.getLocalTranslation(), dir);
            shootable.collideWith(ray, results);
            distance = spawnPoint.distance(bullet.getLocalTranslation());
            
            if(distance >= 500.0f)
                delete();
            
            else if(!bullet.getControl(RigidBodyControl.class).
                    getAngularVelocity().equals(Vector3f.ZERO))
            {
                target = results.getClosestCollision().getGeometry()
                        .getParent().getParent();
                if(target.getName().equals("Enemy")
                        || target.getName().equals("Boss"))
                {
                    target.setUserData("Health",
                            (Integer)target.getUserData("Health")
                            -(Integer)bullet.getUserData("Damage"));
                }
                else if(results.getClosestCollision().getGeometry().getName()
                        .equals("close"))
                {
                    if((Integer)shootable.getChild("Boss").getUserData("Health") <= 0)
                    {
                        System.out.println("........");
                        bgm.setBGM("end");
                    }
                }
                delete();
            }
        }
        else
        {
            timer += tpf;
            if(timer > 2.0f)
            {
                flame.removeFromParent();
                bullet.removeFromParent();
                bullet.removeControl(this);
                bullet = null;
            }
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp)
    {
        //Unused
    }
}
