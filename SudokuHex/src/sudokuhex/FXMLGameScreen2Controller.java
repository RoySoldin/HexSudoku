/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// #############################################################################
// #############################################################################
//--------------------        imports      -------------------------------------
// #############################################################################
// #############################################################################
package sudokuhex;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 * FXML Controller class
 *
 * @author Roy Soldin
 */
public class FXMLGameScreen2Controller implements Initializable {
    
// #############################################################################
// #############################################################################
//---------------        Global Var and FXML Objects      ----------------------
// #############################################################################
// #############################################################################
    @FXML
    Button button_zero;
    @FXML
    Button button_one;
    @FXML 
    Button button_two;
    @FXML 
    Button button_three;
    @FXML 
    Button button_four;
    @FXML 
    Button button_five;
    @FXML 
    Button button_six;
    @FXML 
    Button button_seven;
    @FXML 
    Button button_eight;
    @FXML 
    Button button_nine;
    @FXML 
    Button button_A;
    @FXML 
    Button button_B;
    @FXML 
    Button button_C;
    @FXML 
    Button button_D;
    @FXML 
    Button button_E;
    @FXML 
    Button button_F;
    @FXML
    Canvas canvas;
    @FXML
    ChoiceBox<String> choiceBox = new ChoiceBox<>();
    @FXML
    Label historyLabel;
    @FXML
    Label welcomeLabel;
    @FXML
    Label timeTest;
    @FXML
    Button newGame;
    @FXML
    GridPane keypad;
    //GameBoard
    GameBoard gameBoard;
    int player_selected_row;
    int player_selected_col;
    GraphicsContext context;
    int levelFlag;
    //History and Time
    List users_data;
    RoyTimer timer;
    Player curr_player;
    Thread t;
    
// #############################################################################
// #############################################################################
//----------------------------        Functios      ----------------------------
// #############################################################################
// #############################################################################
    @Override
    public void initialize(URL url, ResourceBundle rb){
//Create an instance of our gameboard
                timer = new RoyTimer();
                t = new Thread(timer);
                player_selected_row=0;
                player_selected_col=0;
                levelFlag = 0;
                choiceBox.getItems().addAll("Easy" , "Medium" , "Hard", "Demo");
                choiceBox.setValue("Easy");
		gameBoard = new GameBoard();
		context = canvas.getGraphicsContext2D();
		drawOnCanvas(context);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        historyLabel.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                            keyboardInit();
                        });
                    }
                    });
                }
    
    @FXML
    Button exitButton;
    
    @FXML
    private void closeButtonAction(){
        timer.pauseGame();
        // get a handle to the stage
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public void drawOnCanvas(GraphicsContext context) {
        context.clearRect(0, 0, 530, 530);
        for(int row = 0; row<16; row++) {
                    for(int col = 0; col<16; col++) {
                       int position_y;
                       int position_x;
                // make bigger space every 4 squres
                       if(row % 4 == 0)
                            position_y = row * 31 + 5;
                       else position_y = row * 31 + 3;
                // finds the x position of the cell, by multiplying the column number by 31, which is the width of a 					// column in pixels
                // then add 3 / 5, to add some offset
                        if(col % 4 == 0)
                            position_x = col * 31 + 5;
                        else position_x = col * 31 + 3;
                        int width = 26;

                        context.setFill(Color.WHITE);
                //Draw the shape
                        context.fillRoundRect(position_x, position_y, width, width, 10, 10);
                    }}
            // draw highlight around selected cell
            // set stroke color to res
            context.setStroke(Color.RED);
            // set stroke width to 5px
            context.setLineWidth(5);
            context.strokeRoundRect(player_selected_col * 31 + 2, player_selected_row * 31 + 2, 26, 26, 10, 10);
            int[][] initial = null;
            if(levelFlag!= 0){    
                int[][] demo = new int[16][16];
                if(levelFlag == 1) initial = gameBoard.getEasy();
                if(levelFlag == 2) initial = gameBoard.getMedium();
                if(levelFlag == 3) initial = gameBoard.getHard();
                if(levelFlag == 4) initial = gameBoard.getDemo();
                for(int row = 0; row<16; row++) 
                {
                    for(int col = 0; col<16; col++) 
                    {
                        int position_y = row * 31 + 22;
                        int position_x = col * 31 + 10;
                        context.setFill(Color.BLACK);
                        context.setFont(new Font(19));
                        if(initial[row][col]!=69) {
                            // draw the number
                            String toFill = hexConverter(initial[row][col]);
                            context.fillText(toFill + "", position_x, position_y);
                        }
                    }
                }
            int[][] player = gameBoard.getPlayer();
                for(int row = 0; row<16; row++) 
                {
                    for(int col = 0; col<16; col++) 
                    {
                        int position_y = row * 31 + 19;
                        int position_x = col * 31 + 10;
                        context.setFill(Color.RED);
                        context.setFont(new Font(19));
                        if(player[row][col]!=69) {
                            // draw the number
                            String toFill = hexConverter(player[row][col]);
                            context.fillText(toFill + "", position_x, position_y);
                        }
                    }
                }
            
                if(gameBoard.checkForSuccess() == true) {
                    
                        curr_player.addToHistory(levelFlag, timer.getTimer());
                        historyLabel.setText(curr_player.getHistory());
                        
                        timer.pauseGame();
                        timer = new RoyTimer();
                        pauseButton.setText("Pause");

                        writeUsersData();
			context.clearRect(0, 0, 530, 530);
			// set the fill color to green
			context.setFill(Color.GREEN);
			// set the font to 36pt
			context.setFont(new Font(38));
			// display SUCCESS text on the screen
			context.fillText("SUCCESS!", 150, 250);
				}
        }
                // TODO pause and update history with timer
    }
    public void canvasMouseClicked() {
        
            EventHandler handler;
            handler = (EventHandler<MouseEvent>) (MouseEvent event) -> {
                int mouse_x = (int) event.getX();
                int mouse_y = (int) event.getY();
                
                
                // convert location to int of proper cell
                player_selected_row = (int) (mouse_y / 31); // update player selected row
                player_selected_col = (int) (mouse_x / 31); // update player selected column
                
                //get the canvas graphics context and redraw
                drawOnCanvas(canvas.getGraphicsContext2D());
            };


            // attach a new EventHandler of the MouseEvent type to the canvas
            canvas.setOnMouseClicked(handler);
    }
    
    @FXML
    Button pauseButton;
    
    @FXML
    public void pauseButtonHandler(){
        if(pauseButton.getText().equals("Pause")){
            timer.pauseGame();
            keypad.setDisable(true);
            canvas.setDisable(true);
            pauseButton.setText("Resume");
        }
        else{
            timer.pauseGame();
            t.stop();
            t = new Thread(timer);
            t.start();
            canvas.setDisable(false);
            keypad.setDisable(false);
            pauseButton.setText("Pause");
        }
    }
   
    boolean firstTime = true;
    
    public void newGameClicked() 
    {   
        gameBoard.randomBoards();
//        keyboardInit();
        t.stop();
        timer.resetTimer();
        t = new Thread(timer);
        
        t.start();
        firstTime = false;
        
        historyLabel.setText(curr_player.getHistory());
        gameBoard.resetPlayer();
        String level = choiceBox.getValue();
        if(level.equals("Easy")) levelFlag = 1;
        if(level.equals("Medium")) levelFlag = 2;
        if(level.equals("Hard")) levelFlag = 3;
        if(level.equals("Demo")) levelFlag = 4;
        drawOnCanvas(context);         
    }

    private String hexConverter(int i) {
        Integer wrap = i;
        if(i < 10)
            return wrap.toString();
        else
        {
            switch(wrap){
                case(10): return "A";
                case(11): return "B";
                case(12): return "C";
                case(13): return "D";
                case(14): return "E";
                case(15): return "F";
            }
        }
        return "";
    }
    
    
// #############################################################################
// #############################################################################
//----------------------        Numbers Buttons      ---------------------------
// #############################################################################
// #############################################################################
    public void buttonZeroPressed() {
	gameBoard.modifyPlayer(0, player_selected_row, player_selected_col);
	drawOnCanvas(canvas.getGraphicsContext2D());	
    }
    public void buttonOnePressed() {
	gameBoard.modifyPlayer(1, player_selected_row, player_selected_col);
	drawOnCanvas(canvas.getGraphicsContext2D());		
    }
    public void buttonTwoPressed() {
        gameBoard.modifyPlayer(2, player_selected_row, player_selected_col);
	drawOnCanvas(canvas.getGraphicsContext2D());	
    }
    public void buttonThreePressed() {
        gameBoard.modifyPlayer(3, player_selected_row, player_selected_col);
	drawOnCanvas(canvas.getGraphicsContext2D());	
    }
    public void buttonFourPressed() {
        gameBoard.modifyPlayer(4, player_selected_row, player_selected_col);
	drawOnCanvas(canvas.getGraphicsContext2D());	
    }
    public void buttonFivePressed() {
        gameBoard.modifyPlayer(5, player_selected_row, player_selected_col);
	drawOnCanvas(canvas.getGraphicsContext2D());	
    }
    public void buttonSixPressed() {
        gameBoard.modifyPlayer(6, player_selected_row, player_selected_col);
	drawOnCanvas(canvas.getGraphicsContext2D());	
    }
    public void buttonSevenPressed() {
        gameBoard.modifyPlayer(7, player_selected_row, player_selected_col);
	drawOnCanvas(canvas.getGraphicsContext2D());	
    }
    public void buttonEightPressed() {
        gameBoard.modifyPlayer(8, player_selected_row, player_selected_col);
	drawOnCanvas(canvas.getGraphicsContext2D());	
    }
    public void buttonNinePressed() {
        gameBoard.modifyPlayer(9, player_selected_row, player_selected_col);
	drawOnCanvas(canvas.getGraphicsContext2D());	
    }
    public void buttonAPressed() {
	gameBoard.modifyPlayer(10, player_selected_row, player_selected_col);
	drawOnCanvas(canvas.getGraphicsContext2D());		
    }
    public void buttonBPressed() {
	gameBoard.modifyPlayer(11, player_selected_row, player_selected_col);
	drawOnCanvas(canvas.getGraphicsContext2D());		
    }
    public void buttonCPressed() {
	gameBoard.modifyPlayer(12, player_selected_row, player_selected_col);
	drawOnCanvas(canvas.getGraphicsContext2D());		
    }
    public void buttonDPressed() {
	gameBoard.modifyPlayer(13, player_selected_row, player_selected_col);
	drawOnCanvas(canvas.getGraphicsContext2D());		
    }
    public void buttonEPressed() {
	gameBoard.modifyPlayer(14, player_selected_row, player_selected_col);
	drawOnCanvas(canvas.getGraphicsContext2D());		
    }
    public void buttonFPressed() {
	gameBoard.modifyPlayer(15, player_selected_row, player_selected_col);
	drawOnCanvas(canvas.getGraphicsContext2D());		
    }
    
    public void keyboardInit(){
        KeyCombination combi0 = new KeyCodeCombination(KeyCode.DIGIT0, KeyCombination.CONTROL_ANY);
        Runnable run0 = ()-> buttonZeroPressed();
        historyLabel.getScene().getAccelerators().put(combi0, run0);
        KeyCombination combi1 = new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.CONTROL_ANY);
        Runnable run1 = ()-> buttonOnePressed();
        historyLabel.getScene().getAccelerators().put(combi1, run1);
        KeyCombination combi2 = new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.CONTROL_ANY);
        Runnable run2 = ()-> buttonTwoPressed();
        historyLabel.getScene().getAccelerators().put(combi2, run2);
        KeyCombination combi3 = new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.CONTROL_ANY);
        Runnable run3 = ()-> buttonThreePressed();
        historyLabel.getScene().getAccelerators().put(combi3, run3);
        KeyCombination combi4 = new KeyCodeCombination(KeyCode.DIGIT4, KeyCombination.CONTROL_ANY);
        Runnable run4 = ()-> buttonFourPressed();
        historyLabel.getScene().getAccelerators().put(combi4, run4);
        KeyCombination combi5 = new KeyCodeCombination(KeyCode.DIGIT5, KeyCombination.CONTROL_ANY);
        Runnable run5 = ()-> buttonFivePressed();
        historyLabel.getScene().getAccelerators().put(combi5, run5);
        KeyCombination combi6 = new KeyCodeCombination(KeyCode.DIGIT6, KeyCombination.CONTROL_ANY);
        Runnable run6 = ()-> buttonSixPressed();
        historyLabel.getScene().getAccelerators().put(combi6, run6);
        KeyCombination combi7 = new KeyCodeCombination(KeyCode.DIGIT7, KeyCombination.CONTROL_ANY);
        Runnable run7 = ()-> buttonSevenPressed();
        historyLabel.getScene().getAccelerators().put(combi7, run7);
        KeyCombination combi8 = new KeyCodeCombination(KeyCode.DIGIT8, KeyCombination.CONTROL_ANY);
        Runnable run8 = ()-> buttonEightPressed();
        historyLabel.getScene().getAccelerators().put(combi8, run8);
        KeyCombination combi9 = new KeyCodeCombination(KeyCode.DIGIT9, KeyCombination.CONTROL_ANY);
        Runnable run9 = ()-> buttonNinePressed();
        
        historyLabel.getScene().getAccelerators().put(combi9, run9);
        KeyCombination combiA = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_ANY);
        Runnable runA = ()-> buttonAPressed();
        historyLabel.getScene().getAccelerators().put(combiA, runA);
        KeyCombination combiB = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_ANY);
        Runnable runB = ()-> buttonBPressed();
        historyLabel.getScene().getAccelerators().put(combiB, runB);
        KeyCombination combiC = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
        Runnable runC = ()-> buttonCPressed();
        historyLabel.getScene().getAccelerators().put(combiC, runC);
        KeyCombination combiD = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_ANY);
        Runnable runD = ()-> buttonDPressed();
        historyLabel.getScene().getAccelerators().put(combiD, runD);
        KeyCombination combiE = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_ANY);
        Runnable runE = ()-> buttonEPressed();
        historyLabel.getScene().getAccelerators().put(combiE, runE);
        KeyCombination combiF = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_ANY);
        Runnable runF = ()-> buttonFPressed();
        historyLabel.getScene().getAccelerators().put(combiF, runF);
        KeyCombination combiP = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_ANY);
        Runnable runP = ()-> pauseButtonHandler();
        historyLabel.getScene().getAccelerators().put(combiP, runP);
        
        KeyCombination esc = new KeyCodeCombination(KeyCode.ESCAPE, KeyCombination.CONTROL_ANY);
        Runnable runEsc = ()-> closeButtonAction();
        historyLabel.getScene().getAccelerators().put(esc, runEsc);
        
        
        
        KeyCombination num0 = new KeyCodeCombination(KeyCode.NUMPAD0, KeyCombination.CONTROL_ANY);
        Runnable run_0 = ()-> buttonZeroPressed();
        historyLabel.getScene().getAccelerators().put(num0, run_0);
        KeyCombination num1 = new KeyCodeCombination(KeyCode.NUMPAD1, KeyCombination.CONTROL_ANY);
        Runnable run_1 = ()-> buttonOnePressed();
        historyLabel.getScene().getAccelerators().put(num1, run_1);
        KeyCombination num2 = new KeyCodeCombination(KeyCode.NUMPAD2, KeyCombination.CONTROL_ANY);
        Runnable run_2 = ()-> buttonTwoPressed();
        historyLabel.getScene().getAccelerators().put(num2, run_2);
        KeyCombination num3 = new KeyCodeCombination(KeyCode.NUMPAD3, KeyCombination.CONTROL_ANY);
        Runnable run_3 = ()-> buttonThreePressed();
        historyLabel.getScene().getAccelerators().put(num3, run_3);
        KeyCombination num4 = new KeyCodeCombination(KeyCode.NUMPAD4, KeyCombination.CONTROL_ANY);
        Runnable run_4 = ()-> buttonFourPressed();
        historyLabel.getScene().getAccelerators().put(num4, run_4);
        KeyCombination num5 = new KeyCodeCombination(KeyCode.NUMPAD5, KeyCombination.CONTROL_ANY);
        Runnable run_5 = ()-> buttonFivePressed();
        historyLabel.getScene().getAccelerators().put(num5, run_5);
        KeyCombination num6 = new KeyCodeCombination(KeyCode.NUMPAD6, KeyCombination.CONTROL_ANY);
        Runnable run_6 = ()-> buttonSixPressed();
        historyLabel.getScene().getAccelerators().put(num6, run_6);
        KeyCombination num7 = new KeyCodeCombination(KeyCode.NUMPAD7, KeyCombination.CONTROL_ANY);
        Runnable run_7 = ()-> buttonSevenPressed();
        historyLabel.getScene().getAccelerators().put(num7, run_7);
        KeyCombination num8 = new KeyCodeCombination(KeyCode.NUMPAD8, KeyCombination.CONTROL_ANY);
        Runnable run_8 = ()-> buttonEightPressed();
        historyLabel.getScene().getAccelerators().put(num8, run_8);
        KeyCombination num9 = new KeyCodeCombination(KeyCode.NUMPAD9, KeyCombination.CONTROL_ANY);
        Runnable run_9 = ()-> buttonNinePressed();
        historyLabel.getScene().getAccelerators().put(num9, run_9);
        KeyCombination ctrlN = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
        Runnable run_N = ()-> newGameClicked();
        historyLabel.getScene().getAccelerators().put(ctrlN, run_N);
        
        KeyCombination up = new KeyCodeCombination(KeyCode.UP, KeyCombination.CONTROL_ANY);
        Runnable run_up = ()-> {
            if(player_selected_row != 0){
                player_selected_row--;
                drawOnCanvas(canvas.getGraphicsContext2D());
            }
        };
        historyLabel.getScene().getAccelerators().put(up, run_up);
        KeyCombination down = new KeyCodeCombination(KeyCode.DOWN, KeyCombination.CONTROL_ANY);
        Runnable run_down = ()-> {
            choiceBox.setDisable(true);
            if(player_selected_row != 15){
                player_selected_row++;
                drawOnCanvas(canvas.getGraphicsContext2D());
            }
            choiceBox.setDisable(false);
        };
        historyLabel.getScene().getAccelerators().put(down, run_down);
        KeyCombination left = new KeyCodeCombination(KeyCode.LEFT, KeyCombination.CONTROL_ANY);
        Runnable run_left = ()-> {
            if(player_selected_col != 0){
                player_selected_col--;
                drawOnCanvas(canvas.getGraphicsContext2D());
            }
        };
        historyLabel.getScene().getAccelerators().put(left, run_left);
        KeyCombination right = new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.CONTROL_ANY);
        Runnable run_right = ()-> {
            if(player_selected_col != 15){
                player_selected_col++;
                drawOnCanvas(canvas.getGraphicsContext2D());
            }
        };
        historyLabel.getScene().getAccelerators().put(right, run_right);
        
        
    }   // Take care of keyboard shortcuts
//   

    void setPlayer(Player currPlayer) {
        this.curr_player = currPlayer;
        historyLabel.setText(curr_player.getHistory());
        welcomeLabel.setText("Welcome " + curr_player.getUsername());
    }

    void setUserdata(List users_data) {
        this.users_data = users_data;
    }
    
    void writeUsersData(){
        try {
                FileOutputStream f = new FileOutputStream("login.dat");
                ObjectOutputStream o = new ObjectOutputStream(f);

                o.writeObject(this.users_data);

                o.close();
                f.close();
            }
        catch(Exception ex){}
    }
    
    public class RoyTimer implements Runnable{
        
        int sec=0;
        boolean paused = false;
        
        @Override
        public void run() {
            while(!paused){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {}
                sec++;
                Platform.runLater( () -> {
                    timeTest.setText(getTimer());
                  });
                }     
            } 
        private void pauseGame(){
            this.paused = !paused;
        }
        
        private String getTimer(){
            return String.format("%02d:%02d:%02d", (sec / 60)/60, sec / 60, sec % 60 );
        } 
        
        private void resetTimer(){
            timeTest.setText("00:00:00");
            this.sec=0;
        }
        
        private boolean isPaused(){
            return !paused;
        }
    }
          
        
}


