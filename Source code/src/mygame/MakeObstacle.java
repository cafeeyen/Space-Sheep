package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MakeObstacle
{
    private BulletAppState bulletAppState;
    private AssetManager assetManager;
    private Node shootable;
    private Spatial model;
    private Material target_mat;
    private String line;
    
    // Constructor
    MakeObstacle(BulletAppState bulletAppState, AssetManager assetManager,
            Node shootable)
    {
        this.bulletAppState = bulletAppState;
        this.assetManager = assetManager;
        this.shootable = shootable;
        try
        {
            setObstacle();
        }
        catch (IOException ex)
        {
            Logger.getLogger(MakeObstacle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setObstacle() throws IOException
    {
        // Open file
        File file = new File("assets\\Textfiles\\obstacle.txt");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(new FileReader(file));
                
        while ((line = br.readLine()) != null)
        {
            // Load model by type
            if(line.equals("cloud"))
            {
                target_mat = new Material( 
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                model = assetManager.loadModel("Models/Cloud/cloud.mesh.j3o");
                target_mat.setTexture("ColorMap", 
                assetManager.loadTexture(new TextureKey("Models/Cloud/cloud.jpg", false))); 
            }
            else if(line.equals("cake"))
            {
                target_mat = new Material( 
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                model = assetManager.loadModel("Models/Cake/cake.mesh.j3o");
                target_mat.setTexture("ColorMap", 
                assetManager.loadTexture(new TextureKey("Models/Cake/cake.jpg", false)));
            }
            else if(line.equals("yamroll"))
            {
                target_mat = new Material( 
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                model = assetManager.loadModel("Models/Yamroll/yamroll.mesh.j3o");
                target_mat.setTexture("ColorMap", 
                assetManager.loadTexture(new TextureKey("Models/Yamroll/yamroll.jpg", false)));
            }
            else if(line.equals("mello"))
            {
                target_mat = new Material( 
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                model = assetManager.loadModel("Models/Mello/mello.mesh.j3o");
                target_mat.setTexture("ColorMap", 
                assetManager.loadTexture(new TextureKey("Models/Mello/mello.jpg", false)));
            }
            else if(line.equals("moji"))
            {
                target_mat = new Material( 
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                model = assetManager.loadModel("Models/Moji/moji.mesh.j3o");
                target_mat.setTexture("ColorMap", 
                assetManager.loadTexture(new TextureKey("Models/Moji/moji.jpg", false)));
            }
            else if(line.equals("choco"))
            {
                target_mat = new Material( 
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                model = assetManager.loadModel("Models/Choco2/choco2.mesh.j3o");
                target_mat.setTexture("ColorMap", 
                assetManager.loadTexture(new TextureKey("Models/Choco2/choco2.jpg", false)));
            }
            else if(line.equals("chocobar"))
            {
                target_mat = new Material( 
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                model = assetManager.loadModel("Models/Choco/choco.mesh.j3o");
                target_mat.setTexture("ColorMap", 
                assetManager.loadTexture(new TextureKey("Models/Choco/choco.jpg", false)));
            }
            else if(line.equals("chocopack"))
            {
                target_mat = new Material( 
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                model = assetManager.loadModel("Models/Chocobar/choco_bar.mesh.j3o");
                target_mat.setTexture("ColorMap", 
                assetManager.loadTexture(new TextureKey("Models/Chocobar/choco_bar.jpg", false)));
            }
            else
            {
                // Set obstacle
                Spatial object = model.clone();
                object.setMaterial(target_mat);
                object.scale(Integer.parseInt(line.split(" ")[6]));
                object.setLocalTranslation(Integer.parseInt(line.split(" ")[0]),
                        Integer.parseInt(line.split(" ")[1]),
                        Integer.parseInt(line.split(" ")[2]));
                object.addControl(new ObstacleControl(object, bulletAppState, line));
                shootable.attachChild(object);
            }
        }
        br.close();
        fr.close();
    }
}
