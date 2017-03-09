package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import com.jme3.scene.Node;

public class SetAudio
{
    private Node rootNode;
    private AudioNode shoot, stage, boss, explosive, end;
    
    public SetAudio(AssetManager assetManager, Node rootNode, String name)
    {
        this.rootNode = rootNode;
        
        if(name.equals("bgm"))
        {
            stage = new AudioNode(assetManager, "Sounds/Crystal Mine.wav", false);
            stage.setLooping(true);
            stage.setPositional(false);
            rootNode.attachChild(stage);
            stage.stop();

            boss = new AudioNode(assetManager, "Sounds/Dream Island.wav", false);
            boss.setLooping(true);
            boss.setPositional(false);
            rootNode.attachChild(boss);
            boss.stop();
            
            end = new AudioNode(assetManager, "Sounds/end.wav", false);
            end.setLooping(false);
            end.setPositional(false);
            rootNode.attachChild(end);
            end.stop();
        }
        
        if(name.equals("shoot"))
        {
            shoot = new AudioNode(assetManager, "Sounds/shoot.wav", false);
            shoot.setLooping(true);
            shoot.setPositional(false);
            rootNode.attachChild(shoot);
            shoot.stop();
        }
        
        if(name.equals("explosive"))
        {
            explosive = new AudioNode(assetManager, "Sounds/explosive.wav", false);
            explosive.setLooping(false);
            explosive.setPositional(false);
            rootNode.attachChild(explosive);
            explosive.play();
        }
    }
    
    public void setBGM(String state)
    {
        if(state.equals("stage"))
        {
            stage.play();
        }
        else if(state.equals("boss"))
        {
            stage.stop();
            boss.play();
        }
        else if(state.equals("end"))
        {
            if(end.getStatus() == AudioSource.Status.Stopped)
            {
                boss.stop();
                end.play();
            }
        }
    }
    
    public void setShoot()
    {
        shoot.playInstance();
    }
}
