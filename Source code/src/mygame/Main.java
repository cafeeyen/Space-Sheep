package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

public class Main extends SimpleApplication
{
    int gear=0;
    float shootTimer=0;
    Boolean isRunning=true, rotate=false, shoot=false;
    Node player, shootable;
    BulletAppState bulletAppState;
    SetAudio shootSound, bgm;
    SetUI gui;
    
    
    public static void main(String[] args)
    {
        // Set title
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Space Sheep");
        settings.setSettingsDialogImage("Interface/sheep_bg.png");
        
        // Start game
        Main app = new Main();
        app.setSettings(settings);
        app.start();
        
    }

    @Override
    public void simpleInitApp()
    {
        // Set node
        shootable = new Node("shootable");
        rootNode.attachChild(shootable);
        
        // Set cursor
        JmeCursor cursor = (JmeCursor)assetManager.loadAsset("Textures/cursor.ani");      
        inputManager.setMouseCursor(cursor);
        
        // Set physics
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0, 0, 0));
        
        flyCam.setEnabled(false);
        
        // Set player
        player = new Node("Player");
        player.addControl(new PlayerControl(player, bulletAppState,
            assetManager, inputManager, cam));        
        shootable.attachChild(player);
        
        //Set key mapping
        SetKeys key = new SetKeys(inputManager, actionListener, analogListener);
                
        // Set state and enemy
        SetStage stage = new SetStage(bulletAppState, assetManager, shootable);
        MakeEnemy enemy = new MakeEnemy(assetManager, bulletAppState, shootable, player);
        
        // Set audio
        bgm = new SetAudio(assetManager, rootNode, "bgm");
        shootSound = new SetAudio(assetManager, rootNode, "shoot");
        bgm.setBGM("stage");
        
        // Set user interface
        gui = new SetUI(assetManager, guiNode, settings);
    }
    
    @Override
    public void simpleUpdate(float tpf) 
    {
        if(isRunning)
        {
            // Shoot bullet
            shootTimer += tpf;
            if(shoot)
            {
                if(shootTimer >= 0.1f)
                {
                    MakeBullet bullet = new MakeBullet(bulletAppState, assetManager,
                            inputManager, shootable, player, cam, bgm);
                    shootSound.setShoot();
                    shootTimer = 0.0f;
                }
            }
            gui.setStatus(gear, (Integer)player.getUserData("Health"), tpf);
            if(player.getLocalTranslation().z >= 8200)
                bgm.setBGM("boss");
        }
    }
    
    private ActionListener actionListener = new ActionListener()
    {
        public void onAction(String name, boolean keyPressed, float tpf)
        {
            if(isRunning)
            {
                // Change speed
                if(name.equals("SpeedUp") && keyPressed)
                {
                    if(gear < 2)
                    {
                        gear++;
                        player.getControl(PlayerControl.class).setGear(gear);
                    }
                    
                }
                if(name.equals("SpeedDown") && keyPressed)
                {
                    if(gear > -2)
                    {
                        gear--;
                        player.getControl(PlayerControl.class).setGear(gear);
                    }
                }
                if(name.equals("Rotate") && keyPressed)
                {
                    rotate = !rotate;
                    player.getControl(PlayerControl.class).setRotate(rotate);
                }
                if(name.equals("Shoot"))
                    shoot = keyPressed;
                
                if(name.equals("Cheat"))
                    if(rootNode.getChild("Boss") != null)
                        rootNode.getChild("Boss").setUserData("Health", 0);
            }
        }
    };
    
    private AnalogListener analogListener = new AnalogListener()
    {
        public void onAnalog(String name, float value, float tpf)
        {

        }
    };
}

