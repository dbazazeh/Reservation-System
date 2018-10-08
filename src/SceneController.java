/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fancyhotels;

import javafx.scene.Scene;

/**
 *
 * @author nkaru_000
 */
public abstract class SceneController {
    FancyHotelsController fancyHotelsControl;
    Scene rootScene;
    
    public void setFancyHotelsControl(FancyHotelsController fancyHotelsControl){
    this.fancyHotelsControl = fancyHotelsControl;
            
            }
    
    public void setScene (Scene scene){
    this.rootScene = scene;
    }
    
    public Scene getScene (){
    return rootScene;
    }
}
