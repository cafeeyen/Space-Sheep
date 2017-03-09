package mygame;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;

public class SetKeys
{
    private InputManager inputManager;
    private ActionListener actionListener;
    private AnalogListener analogListener;
    
    SetKeys(InputManager inputManager, ActionListener actionListener,
            AnalogListener analogListener)
    {
        this.inputManager = inputManager;
        this.actionListener = actionListener;
        this.analogListener = analogListener;
        setupKeys();
    }
    private void setupKeys()
    {
        // Keyboard control
        inputManager.addMapping("SpeedUp", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("SpeedDown", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("Cheat", new KeyTrigger(KeyInput.KEY_K));
        
        // Mouse control
        inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("Rotate", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        
        // Add Listener
        inputManager.addListener(actionListener, "SpeedUp", "SpeedDown", "Rotate", "Shoot", "Cheat");
    }
}
