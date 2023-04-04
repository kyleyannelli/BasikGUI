package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class HelloController {
    private Stage stage;
    private Scene scene;
    private Parent root;

//switches to 5150 Amp model
    public void switchToScene1(ActionEvent event) throws IOException {
       root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
       stage.setScene(scene);
       stage.show();
   }

   //switches to Clean Amp
   public void switchToScene2(ActionEvent event) throws IOException {
         root = FXMLLoader.load(getClass().getResource("clean-amp.fxml"));

       stage = (Stage)((Node)event.getSource()).getScene().getWindow();
       scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    //opens pedal board options
    public void switchToScene3(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("pedal-board.fxml"));

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    TextField set1;
    @FXML
    TextField set2;
    @FXML
    TextField set3;
    @FXML
    TextField set4;
    @FXML
    TextField set5;
    @FXML
    TextField set6;
    @FXML
    TextField set7;

    @FXML
    TextField disToneNum;

    @FXML
    TextField disLvlNum;
    @FXML
    ImageView amp;
    @FXML
    ImageView knob1;
    @FXML
    ImageView knob2;
    @FXML
    ImageView knob3;
    @FXML
    ImageView knob4;
    @FXML
    ImageView knob5;
    @FXML
    ImageView knob6;
    @FXML
    ImageView knob7;
    @FXML
    ImageView redLight;
    @FXML
    ImageView GreenLight;
    @FXML
    ImageView redLightOn;
    @FXML
    ImageView redLightOff;
    @FXML
    //turns 5150 amp model on/off
    public void TurnOn(MouseEvent event) {
        if (redLight.isVisible()) {
            redLight.setVisible(false);
            GreenLight.setVisible(true);
            set1.setVisible(true);
            set2.setVisible(true);
            set3.setVisible(true);
            set4.setVisible(true);
            set5.setVisible(true);
            set6.setVisible(true);
            set7.setVisible(true);
        }
        else {
            redLight.setVisible(true);
            GreenLight.setVisible(false);
            set1.setVisible(false);
            set2.setVisible(false);
            set3.setVisible(false);
            set4.setVisible(false);
            set5.setVisible(false);
            set6.setVisible(false);
            set7.setVisible(false);
        }
    }
    @FXML
    //turns on and off the distortion pedal
    void distortOnOff(MouseEvent event)
    {
        if(redLightOff.isVisible())
        {
            redLightOn.setVisible(true);
            redLightOff.setVisible(false);
        }
        else
        {
            redLightOff.setVisible(true);
            redLightOn.setVisible(false);
        }
    }
    //changes tone number based on where knob value is
    @FXML
    void distorionToneSet(MouseEvent event)
    {
        disToneNum.getPrefColumnCount();


    }

    //changes distortion number based on where knob value is
    @FXML
    void distorionSet(MouseEvent event)
    {
        disLvlNum.getPrefColumnCount();
    }

}
