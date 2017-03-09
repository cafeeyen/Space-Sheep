package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

public class BossControl extends AbstractControl
{
    private AssetManager assetManager;
    private BulletAppState bulletAppState;
    private RigidBodyControl phy;
    private Node enemy, player, shootable;
    private boolean detect=false, state=true, alive=true;
    private float shootTimer=0, turnTimer=0, distance=0;
    private Vector3f dir, shootDir;
    private ParticleEmitter flame;
    
    public BossControl(BulletAppState bulletAppState, Node enemy, Node player,
            Node shootable, AssetManager assetManager)
    {
        this.bulletAppState = bulletAppState;
        this.enemy = enemy;
        this.player = player;
        this.shootable = shootable;
        this.assetManager = assetManager;
        
        CollisionShape shape = CollisionShapeFactory.createDynamicMeshShape(enemy);
        phy = new RigidBodyControl(500);
        enemy.addControl(phy);
        phy.setCcdMotionThreshold(6000);
        bulletAppState.getPhysicsSpace().add(phy);
        phy.setAngularFactor(0);
    }

    private void delete()
    {
        alive = false;
        enemy.setCullHint(Spatial.CullHint.Always);
        flame();
    }
    
    private void flame()
    {
        flame = new ParticleEmitter("flame", ParticleMesh.Type.Triangle, 300);
        Material mat = new Material(assetManager, 
                "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture(
                "Effects/Explosion/flame.png"));
        flame.setMaterial(mat);
        
        flame.setImagesX(30); //size texture animation import
        flame.setImagesY(30);
        flame.setStartSize(100); //display size
        flame.setEndSize(20);
        flame.setStartColor(ColorRGBA.Red);
        flame.setEndColor(ColorRGBA.Yellow);
        flame.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2, 0));
        flame.getParticleInfluencer().setVelocityVariation(0.3f);

        flame.setLocalTranslation(enemy.getLocalTranslation());
        
        shootable.getParent().attachChild(flame);
    }
    
    @Override
    protected void controlUpdate(float tpf)
    {
        shootTimer += tpf;
        
        if(alive)
        {
            turnTimer += tpf;
            distance = player.getLocalTranslation().distance(enemy.getLocalTranslation());
            
            if(!detect)
            {
                phy.setPhysicsRotation(phy.getPhysicsRotation().mult(
                        new Quaternion().fromAngleAxis(FastMath.PI/1024, Vector3f.UNIT_Y)));

                if(distance <= 1500)
                    detect = true;
            }
            else
            {
                enemy.lookAt(player.getLocalTranslation(), Vector3f.UNIT_Y);
                dir = enemy.getWorldRotation().multLocal(Vector3f.UNIT_Z.clone());
                
                if(state == true)
                    phy.setLinearVelocity(dir.mult(80));
                else
                    phy.setLinearVelocity(dir.mult(80).negate());
                
                if((distance < 350 || distance > 900) && turnTimer > 6.7f)
                {
                    dir.negateLocal();
                    turnTimer = 0;
                    state = !state;
                }
                else if(distance > 3000)
                    detect = false;
                
                if(shootTimer >= 0.8f)
                {
                    shootDir = enemy.getWorldRotation().mult(Vector3f.UNIT_Z);                    
                    MakeBomb bomb = new MakeBomb(phy.getPhysicsLocation().clone(),
                            bulletAppState, shootable, assetManager,
                            shootDir);                    
                    shootTimer = 0.0f;
                }
            }
            
            if(enemy.getUserData("Health").equals(0))
            {
                SetAudio sound = new SetAudio(assetManager, shootable.getParent(),
                        "explosive");
                delete();
                shootTimer = 0;
            }
        }
        else
        {
            if(shootTimer >= 3.0)
            {
                flame.removeFromParent();
                enemy.removeControl(this);
                enemy = null;
            }
        }
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp)
    {
        // Unused
    }
}
