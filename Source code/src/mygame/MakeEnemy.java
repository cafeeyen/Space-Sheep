package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MakeEnemy implements AnimEventListener
{
    private AssetManager assetManager;
    private BulletAppState bulletAppState;
    private Node shootable, enemy, player;
    private AnimControl control;
    private AnimChannel channel;
    
    public MakeEnemy(AssetManager assetManager, BulletAppState bulletAppState,
            Node shootable, Node player)
    {
        this.assetManager = assetManager;
        this.bulletAppState = bulletAppState;
        this.shootable = shootable;
        this.player = player;
        makeEnemy();
        
    }
    private void makeEnemy()
    {
        Spatial model = assetManager.loadModel("Models/Rabbit/rabbit.mesh.j3o");
        Material mat = new Material( 
            assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", 
            assetManager.loadTexture(new TextureKey("Models/Rabbit/rabbit.jpg", false)));
        
        // Open file
        File file = new File("assets\\Textfiles\\enemy.txt");
        try
        {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null)
            {
                // Set enemy
                enemy = new Node("Enemy");
                enemy.attachChild(model.clone());
                
                control = enemy.getChild(0).getControl(AnimControl.class);
                control.addListener(this);
                channel = control.createChannel();
                channel.setAnim("float");
                channel.setLoopMode(LoopMode.Loop);
                
                enemy.setMaterial(mat);
                enemy.setLocalTranslation(Integer.parseInt(line.split("\t")[0])
                        , Integer.parseInt(line.split("\t")[1])
                        , Integer.parseInt(line.split("\t")[2]));
                enemy.scale(3);
                enemy.addControl(new EnemyControl(bulletAppState, enemy, player,
                        shootable, assetManager));
                enemy.setUserData("Health", 5);
                shootable.attachChild(enemy);
            }
            
            // Boss
            enemy = new Node("Boss");
            enemy.attachChild(model.clone());
            enemy.setMaterial(mat);
            enemy.setLocalTranslation(1000, 1000, 9500);
            enemy.scale(20);
            enemy.addControl(new BossControl(bulletAppState, enemy, player,
                            shootable, assetManager));
            enemy.setUserData("Health", 40);
            shootable.attachChild(enemy);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(MakeEnemy.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(MakeEnemy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName)
    {
        // Unused
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName)
    {
        // Unused
    }
}