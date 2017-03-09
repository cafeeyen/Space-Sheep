package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;

public class SetUI
{
    private AssetManager assetManager;
    private int gear=0, hp=100, lasthp=100;
    private float damageTimer;
    private Picture sheep, hpBar, hpProgress, bend;
    private Node guiNode;
    AppSettings settings;
    
    
    public SetUI(AssetManager assetManager, Node guiNode,
            AppSettings settings)
    {
        this.guiNode = guiNode;
        this.assetManager = assetManager;
        this.settings = settings;
        uiInitailize();
    }
    
    private void uiInitailize()
    {
        sheep = new Picture("Sheep");
        sheep.setImage(assetManager, "Interface/sheep0.png", true);
        sheep.setPosition(0, 0);
        sheep.setWidth((150/640f)*settings.getWidth());
        sheep.setHeight((150/640f)*settings.getWidth());
        guiNode.attachChild(sheep);
        
        hpBar = new Picture("HPBar");
        hpBar.setImage(assetManager, "Interface/hpbar.png", true);
        hpBar.setPosition(0, 0);
        hpBar.setWidth((500/640f)*settings.getWidth());
        hpBar.setHeight((35/480f)*settings.getHeight());
        guiNode.attachChild(hpBar);
        
        hpProgress = new Picture("HPprogress");
        hpProgress.setImage(assetManager, "Interface/hpstate.png", true);
        hpProgress.setPosition(0, 0);
        hpProgress.setWidth((500/640f)*settings.getWidth());
        hpProgress.setHeight((35/480f)*settings.getHeight());
        guiNode.attachChild(hpProgress);
    }
    
    private void update(float tpf)
    {
        //setsheep        
        if(hp != lasthp)
        {
            if(damageTimer < 0.3f)
                damageTimer = 0.3f;
            lasthp = hp;
        }
        if(damageTimer > 0.0f)
        {
            sheep.setImage(assetManager, "Interface/sheepi.png", true);
            damageTimer -= tpf;
        }
        else if(gear == 0)
        {
            sheep.setImage(assetManager, "Interface/sheep0.png", true);
        }
        else if(gear == 1)
        {
            sheep.setImage(assetManager, "Interface/sheep1.png", true);
        }
        else if(gear == 2)
        {
            sheep.setImage(assetManager, "Interface/sheep2.png", true);
        }
        else if(gear == -2)
        {
            sheep.setImage(assetManager, "Interface/sheep-2.png", true);
        }
        else if(gear == -1)
        {
            sheep.setImage(assetManager, "Interface/sheep-1.png", true);
        }    
        hpProgress.setWidth((hp/100f)*(500/640f)*settings.getWidth());
    }
    
    public void setStatus(int gear, int hp, float tpf)
    {
        this.gear = gear;
        this.hp = hp;
        update(tpf);
    }
}
