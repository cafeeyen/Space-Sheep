package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.BulletAppState;
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

public class EnemyControl extends AbstractControl
{
    private AssetManager assetManager;
    private BulletAppState bulletAppState;
    private RigidBodyControl phy;
    private Node enemy, player, shootable;
    private boolean detect=false, state=true, alive=true;
    private float shootTimer=0, turnTimer=0, distance=0;
    private Vector3f dir, shootDir;
    private int skillTimer=0, skillLeft=0;
    private ParticleEmitter flame;
    
    public EnemyControl(BulletAppState bulletAppState, Node enemy, Node player,
            Node shootable, AssetManager assetManager)
    {
        this.bulletAppState = bulletAppState;
        this.enemy = enemy;
        this.player = player;
        this.shootable = shootable;
        this.assetManager = assetManager;
        
        CollisionShape shape = CollisionShapeFactory.createDynamicMeshShape(enemy);
        phy = new RigidBodyControl(50);
        enemy.addControl(phy);
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
        flame = new ParticleEmitter("flame", ParticleMesh.Type.Triangle, 30);
        Material mat = new Material(assetManager, 
                "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture(
                "Effects/Explosion/flame.png"));
        flame.setMaterial(mat);
        
        flame.setImagesX(3); //size texture animation import
        flame.setImagesY(3);
        flame.setStartSize(10); //display size
        flame.setEndSize(2);
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
                skillTimer = 0;
                
                if(distance <= 500)
                    detect = true;               
            }
            else
            {
                enemy.lookAt(player.getLocalTranslation(), Vector3f.UNIT_Y);
                dir = enemy.getWorldRotation().multLocal(Vector3f.UNIT_Z.clone());
                
                // Move
                if(state == true)
                    phy.setLinearVelocity(dir.mult(50));
                else
                    phy.setLinearVelocity(dir.mult(50).negate());
                
                // Check distance for move direction and detect
                if((distance < 250 || distance > 500) && turnTimer > 5.3f)
                {
                    //dir.negateLocal();
                    turnTimer = 0;
                    state = !state;
                }
                else if(distance > 1000)
                    detect = false;
                
                // Shoot
                if(shootTimer >= 1.8f || (skillLeft > 0 && shootTimer >= 0.3))
                {
                    shootDir = enemy.getWorldRotation().mult(Vector3f.UNIT_Z);
                    if(skillTimer == 4)
                    {
                        skillLeft = 3;
                        skillTimer = 0;
                    }
                    else
                    {
                        if(skillLeft == 0)
                            skillTimer += 1;
                        else
                            skillLeft --;
                        
                        MakeBomb bomb = new MakeBomb(phy.getPhysicsLocation().clone(),
                            bulletAppState, shootable, assetManager,
                            shootDir); 
                    } 
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
                enemy.removeFromParent();
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
