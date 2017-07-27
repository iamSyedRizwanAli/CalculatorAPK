package com.example.rizwan.calculator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Stack;


public class MainActivity extends AppCompatActivity {

    ArrayList<ExpressionComponent> components;

    Button[] button = new Button [17];

    boolean startFlag = false,
    answerFlag = false,
    signFlag = false,
    pointFlag = false,
    digitFlag = false,
    wrongEquation = false;

    boolean [] presentOperators = new boolean[4];

    Stack<Boolean> pointFlagStack = new Stack<Boolean>();

    char [] priorityOperators = null;

    ArrayList<String> colors = new ArrayList<String> (9);
    ArrayList<String> err_msgs = new ArrayList<String>(4);

    TextView screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initiateColorsAndErrMsgs();
        initializeAllViews();

    }


    /*
    * This function just initializes the priorityOperators character array
    * */

    void initiateCharPriorityArray(){

        priorityOperators = new char[4];

        priorityOperators[0] = '\u00f7';
        priorityOperators[1] = '\u2715';
        priorityOperators[2] = '+';
        priorityOperators[3] = '\u2013';

    }

    /*
    This method changes the buttons color
     */

    public void changeButtonColors(int color){

        for(int i = 0 ; i < 17 ; i++){
            if(i != 10){
                button[i].setBackgroundColor(color);
            }
        }

    }

    /*
    This method clears display screen as well as the data structures used in it
    */

    void clearScreen(){
        screen.setText("0");
        reinitiateNotificationScreen();

        if(answerFlag) {
            components.clear();
            pointFlagStack.clear();
        }
        startFlag = answerFlag = digitFlag = pointFlag = signFlag = false;

        presentOperators[0] = presentOperators[1] = presentOperators[2] = presentOperators[3] = false;
    }

    /*
    This method initializes all the views. e.g. textView and Buttons
    */

    public void initializeAllViews(){

        button[0] = (Button) findViewById(R.id.button0);
        button[1] = (Button) findViewById(R.id.button1);
        button[2] = (Button) findViewById(R.id.button2);
        button[3] = (Button) findViewById(R.id.button3);
        button[4] = (Button) findViewById(R.id.button4);
        button[5] = (Button) findViewById(R.id.button5);
        button[6] = (Button) findViewById(R.id.button6);
        button[7] = (Button) findViewById(R.id.button7);
        button[8] = (Button) findViewById(R.id.button8);
        button[9] = (Button) findViewById(R.id.button9);
        button[10] = (Button) findViewById(R.id.bspace);
        button[11] = (Button) findViewById(R.id.buttonPlus);
        button[12] = (Button) findViewById(R.id.buttonMinus);
        button[13] = (Button) findViewById(R.id.buttonSlash);
        button[14] = (Button) findViewById(R.id.buttonStar);
        button[15] = (Button) findViewById(R.id.buttonPoint);
        button[16] = (Button) findViewById(R.id.buttonEquals);

        //This initializes the TextViews present in the calculator

        screen = (TextView) findViewById(R.id.screen);
    }

    /*
    This method initializes the ArrayList of colors as well as the err_msgs
     */

    public void initiateColorsAndErrMsgs(){

        colors.add(0, "#8BC34a");
        colors.add(1, "#00BCD4");
        colors.add(2, "#795548");
        colors.add(3, "#303F9F");
        colors.add(4, "#CDDC39");
        colors.add(5, "#536DFE");
        colors.add(6, "#E040FB");
        colors.add(7, "#4CAF50");
        colors.add(8, "#009688");

        err_msgs.add(0, "Invalid equation \n You can hold press backspace button to clear screen");
        err_msgs.add(1, "Ready!");
        err_msgs.add(2, "Notification: ");
        err_msgs.add(3, "Calculated \n You can hold press backspace button to clear screen");

    }

    /*
    This method displays the invalid equation error message
     */

    public void displayInvalidEquationErrorMsg(){
        changeButtonColors(Color.parseColor("#FF5722"));
        wrongEquation = true;
    }

    /*
    This method checks the validity of the equation entered by the user
     */

    public boolean regexVerification(CharSequence string){

        boolean verified = true;
        int size = string.length();

        boolean isFirstCharDigit = false,
                isLastCharDigit = false;

        char cTemp = string.charAt(0);

        if(cTemp >= '0' && cTemp <= '9'){
            isFirstCharDigit = true;
        }else {
            verified = false;
        }

        cTemp = string.charAt(size - 1);

        if((cTemp >= '0' && cTemp <= '9') || cTemp == '.'){
            isLastCharDigit = true;
        }else{
            verified = false;
        }

        if(verified && isLastCharDigit && isFirstCharDigit){

            boolean division = false;
            int value = 0;

            for(int i = 0 ; i < size && verified ; i++){
                cTemp = string.charAt(i);

                if(cTemp >= '0' && cTemp <= '9'){
                    value *= 10;
                    value += cTemp - 48;
                }

                if(cTemp == '\u00f7'){

                    if(division && value == 0){
                        verified = false;
                    }else{
                        value = 0;
                        division = true;
                    }
                } else if(cTemp == '\u2013' || cTemp == '\u2715' || cTemp == '+' || i == size - 1){
                    if(division && value == 0){
                        verified = false;
                    }else{
                        value = 0;
                        division = false;
                    }
                }
            }

        }


        return verified;
    }

    /*
    This method reinitiate notification screen to Ready message
     */

    public void reinitiateNotificationScreen(){
        changeButtonColors(Color.parseColor("#0288d1"));
    }

    /*
    This method handles all the events generated by click of the buttons on the calculator screen
     */

    public void buttonClick(View view){

        /*
        The switch cases are handled on the basis of view id
         */

        int id = view.getId();

        if(id == R.id.button0){

            button0function();

        } else if (id == R.id.button1 || id == R.id.button2 || id == R.id.button3 || id == R.id.button4 ||
                id == R.id.button5 || id == R.id.button6 ||  id == R.id.button7 || id == R.id.button8 ||
                id == R.id.button9){

            numberButtonFunction(id);

        } else if( id == R.id.buttonMinus || id == R.id.buttonPlus || id == R.id.buttonStar || id == R.id.buttonSlash){

            operationButtonFunction(id);

        } else if(id == R.id.buttonEquals){

            buttonEqualFunction();

        } else if(id == R.id.buttonPoint){

            pointButtonFunction();

        } else if(id == R.id.bspace){

            buttonBackSpaceFunction();

        }
    }

    /*
    This method manipulates the ExpressionComponent List and calculates result
     */

    public void manipulateComponentsArray(){

        CharSequence x = screen.getText();
        int size = x.length();

        components = new ArrayList<ExpressionComponent>();

        char cTemp = 0;
        String value = new String();

        for(int i = 0 ; i < size ; i++){

            cTemp = x.charAt(i);

            if((cTemp >= '0' && cTemp <= '9') || cTemp == '.'){
                value += cTemp;
            } else if(cTemp == '\u2013' || cTemp == '\u2715' || cTemp == '+' || cTemp == '\u00f7'){
                components.add(new ExpressionComponent(Double.parseDouble(value), cTemp));
                value = "0";

                if(cTemp == '+'){
                    presentOperators[2] = true;
                } else if(cTemp == '\u2013'){
                    presentOperators[3] = true;
                } else if(cTemp == '\u00f7'){
                    presentOperators[0] = true;
                } else if(cTemp == '\u2715'){
                    presentOperators[1] = true;
                }

            }

            if(i == size - 1){
                components.add(new ExpressionComponent(Double.parseDouble(value), '\0'));
                value = "0";
            }
        }

    }

    /*
    This method performs calculation on components array list
     */

    public double doCalculation(){

        manipulateComponentsArray();

        int size = components.size();

        for(int i = 0 ; i < 4 && size != 1 ; i++){

            for(int j = 0 ; j < size && presentOperators[i] ; j++){

                if(components.get(j).getSymbol() == priorityOperators[i]){

                    components.get(j).setValue(simplify(components.get(j).getValue(), components.get(j+1).getValue(),
                            components.get(j).getSymbol()));

                    components.get(j).setSymbol(components.get(j+1).getSymbol());
                    components.remove(j+1);
                    size = components.size();
                }

            }

        }

        return components.get(0).getValue();
    }

    /*
    * The function below does the mathematics
    * */

    double simplify(double a, double b, char s){

        if(s == '+'){
            return a + b;
        } else if(s == '\u00f7'){
            return a / b;
        } else if(s == '\u2013') {
            return a - b;
        } else if(s == '\u2715'){
            return a * b;
        }

        return 0;
    }

    /*
    The following functions are implementation of the methods called in buttonClick(View view) method
     */


    /*The following method (numberButtonFunction) requires id because it will sort
    out the button having number (1-9) with the help of id
    */

    void numberButtonFunction(int id){

        Button x = (Button) findViewById(id);

        if(startFlag && !answerFlag){
            screen.setText(screen.getText() + x.getText().toString());

            if(signFlag) {
                signFlag = false;
            }

            if(!digitFlag)
                digitFlag = true;
        } else{
            if(answerFlag) {
                clearScreen();
            }

            screen.setText(x.getText());
            startFlag = digitFlag = true;

        }

    }

    void button0function(){

        if(startFlag && !answerFlag){
            screen.setText(screen.getText() + "0");

            if(!digitFlag)
                digitFlag = true;

            if(signFlag)
                signFlag = false;

        } else{
            clearScreen();

            startFlag = digitFlag = true;
        }

    }

    void pointButtonFunction(){

        if(!pointFlag) {

            if (answerFlag) {
                clearScreen();
                startFlag = digitFlag = true;
            }

            if (startFlag && !signFlag) {
                screen.setText(screen.getText() + ".");
                pointFlag = true;
            } else if (!startFlag && !signFlag) {
                screen.setText("0.");
                startFlag = pointFlag = true;
            } else if (startFlag && signFlag) {
                screen.setText(screen.getText() + "0.");
                pointFlag = true;
            }
        }
    }

    /*The following method (operationButtonFunction) requires id because it will sort
    out the operator button with the help of id
    */

    void operationButtonFunction(int id){

        Button x = (Button) findViewById(id);
        char operator = x.getText().toString().charAt(0);

        pointFlagStack.push(pointFlag);

        if(answerFlag)
            answerFlag = false;

        if(startFlag && !signFlag && digitFlag){

            screen.setText(screen.getText() + x.getText().toString());
            signFlag = true;
            digitFlag = false;
            pointFlag = false;

        }else if(startFlag && signFlag && !digitFlag){
            CharSequence tempSequence = screen.getText();

            if(tempSequence.charAt(tempSequence.length() - 2) != operator) {
                tempSequence = tempSequence.subSequence(0, tempSequence.length() - 1);
                screen.setText(tempSequence.toString() + operator);
            }
        }

    }

    void buttonBackSpaceFunction(){

        if(!answerFlag) {

            CharSequence x = screen.getText();

            if (x.length() > 1) {
                char checkChar = x.charAt(x.length() - 2);

                char removedChar = x.charAt(x.length() - 1);

                x = x.subSequence(0, x.length() - 1);


                screen.setText(x);

                if (removedChar >= 48 && removedChar <= 57) {
                    digitFlag = false;
                } else if (removedChar == '.') {
                    pointFlag = false;
                } else {
                    signFlag = false;
                    pointFlag = pointFlagStack.pop();
                }

                if ((int) checkChar >= 48 && (int) checkChar <= 57) {
                    digitFlag = true;
                    signFlag = false;
                } else if (checkChar == '.') {
                    digitFlag = false;
                    pointFlag = true;
                } else {
                    digitFlag = false;
                    signFlag = true;
                }

            } else {
                clearScreen();
            }
        }
        else{
            clearScreen();
            button[10].setText("\u276E"); // button[10] is backspace key
        }
    }

    void buttonEqualFunction() {

        if (startFlag) {

            if(priorityOperators == null){
                initiateCharPriorityArray();
            }

            if (regexVerification(screen.getText())) {
                answerFlag = true;
                digitFlag = true;

                changeButtonColors(Color.parseColor("#4FC34A"));

                screen.setText(Double.toString(doCalculation()));

                button[10].setText("C"); // button[10] is the backspace button

            } else {
                displayInvalidEquationErrorMsg();
            }
        }
    }

    /*
    This class is made for calculation purposes
     */

    class ExpressionComponent {
        private double value;
        private char symbol;

        public ExpressionComponent() {
            value = 0;
            symbol = 0;
        }

        public ExpressionComponent(double val, char s) {
            value = val;
            symbol = s;
        }

        public void assign(ExpressionComponent e){
            value = e.getValue();
            symbol = e.getSymbol();
        }

        public void setValue(double val) {
            value = val;
        }

        public void setSymbol(char s) {
            symbol = s;
        }

        public double getValue() {
            return value;
        }

        public char getSymbol() {
            return symbol;
        }
}


}