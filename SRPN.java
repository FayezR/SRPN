import java.util.*;
import java.lang.Math;

public class SRPN {

  //Creates stack named 'numStack' to store numbers inputted by user
  private Stack<Integer> numStack = new Stack<> ();
  private final String[] operators = {"+", "-", "/", "*", "%", "^"};
  private int[] rValues = {1804289383, 846930886, 1681692777, 1714636915, 1957747793, 424238335, 719885386, 1649760492, 596516649, 1189641421, 1025202362, 1350490027, 783368690, 1102520059, 2044897763, 1967513926, 1365180540, 1540383426, 304089172, 1303455736, 35005211, 521595368, 1804289383};
  private int rCount = 0;

  /*Method receives String from command line from Main.java Class and splits string into String array.
  */
  
  public void processCommand(String userInput){
    if (userInput.contains(" ")){ 
      processSingleToken(userInput.split(" ")); //Splits strings based on " " 
    }
    
    else{
      List<String> tempStore = new ArrayList<String>();
      tempStore.add(userInput);
      String[] token = tempStore.toArray(new String[tempStore.size()]);
      processSingleToken(token);
    }
  }

  //Method takes in String Array and processes each element of the Array seperately.
  public void processSingleToken(String[] token){
    for (int i =0; i<token.length; i++){ 

     if (isOperator(token[i])){ //Checks if token[i] is an operator by calling isOperator()
        processOperator(token[i]); //Method on line 52
      }
      else if (token[i].equals("d")){
        printStack();//method on line 
      }
      else if (token[i].equals("r")){
        addRValueToStack();
      }
      else if (token[i].equals("=")){
        printOutResult();
      }
      else if(isInteger(token[i])){ //Checks if token[i] is an integer by calling isInteger()
        addToStack(token[i]);
      }
      else{
        System.out.println("Unrecognised operator or operand \"" + token[i] + "\".");
      }  
    }
  }
  //Helper method checks whether string contains an operator
  private boolean isOperator (String s){ 
    for (int i = 0; i<operators.length; i++){
      if (s.equals(operators[i])){
        return true;
      }
    }
    return false;
  }

  /*Method checks for whether stack is empty or if the user wishes to divide a number by zero. If these conditions return false, then the operator is processed and the calculation performed.
 */
  private void processOperator(String operator){
    if (numStack.size() <2){
      System.out.println("Stack underflow.");
    }
    else if (dividingByZero(operator)){
      System.out.println("Divide by 0."); 
    }
    else{
      int num1 = numStack.pop();
      int num2 = numStack.pop();
      int result = performCalculation(num1, num2, operator);
      numStack.push(result);
    }

  }

  //Helper method returns true if a number is being divided by zero
  private boolean dividingByZero(String operator){
    if (numStack.peek() == 0 && operator.equals("/")){
      return true;
    }
    return false;
  }

  //Function takes in two integers and a string 'operator' as it's parameters and performs the user requested calculation.
  private int performCalculation (int num1, int num2, String operator){
    int result;
    if (operator.equals("+")){
      try{
       result= Math.addExact(num2,num1);
       return result;
      }
      catch (ArithmeticException e){  //catches integer overflow and returns the integer max value.
        return Integer.MAX_VALUE;
      }
    }
    else if (operator.equals("-")){
      try{
       result= Math.subtractExact(num2, num1);
       return result;
      }
      catch (ArithmeticException e){ //catches integer underflow and returns the integer min value.
        return Integer.MIN_VALUE;
      }
    }
    else if (operator.equals("/")){
      return num2 / num1;
    }
    else if (operator.equals("*")){
      try{
       result= Math.multiplyExact(num2, num1);
       return result;
      }
      catch (ArithmeticException e){ 
        if (num1 < 0 && num2 >0 || num2<0 && num1>0){ //catches integer underflow and returns the integer min value.
          return Integer.MIN_VALUE;
        }
        else{
          return Integer.MAX_VALUE; //Catches integer overflow and returns integer max value.
        } 
      }
    }
    else if (operator.equals("^")){
      try{
       double tempResult= Math.pow((int)num2, (int)num1);
       result = (int) tempResult;
       return result;
      }
      catch (ArithmeticException e){  //Catches integer overflow and returns integer max value.
        return Integer.MAX_VALUE;
      }
    
    }
    else if (operator.equals("%")){
      return num2%num1;
    }
    return 0;
  }

  /*Method checks if 'numStack' is empty. If true it prints Integer.MIN_VALUE, else it pops values from numStack and pushes to tempStack and prints out values contained in tempStack. Values are then popped from tempStack and pushed back onto numStack to preserve order and retain the original stack.
  */
  public void printStack(){
    Stack<Integer> tempStack = new Stack<>();
    if (numStack.empty()==true){
      System.out.println(Integer.MIN_VALUE);
    }
    else{
      while (numStack.empty() == false){
        tempStack.push(numStack.peek());
        numStack.pop();
      }
      while (tempStack.empty() == false){
      int i = tempStack.peek();
        System.out.println(i);
        tempStack.pop();
        numStack.push(i);
      } 
    }
  }

  //Method first checks if numStack contains less than 23 elements. If true, r value is added to numStack.
  private void addRValueToStack (){
  if (numStack.size() < 23){
      numStack.push(rValues[rCount]);
      if(rCount< rValues.length){ // cycles through the 'r' values
      rCount++;
      }
      else{ 
        rCount=0; // if end of r values reached, rCount is set to 0.
      }
    }
    else {
    System.out.println("Stack overflow."); 
    }
  }

  //Method first checks whether stack is empty. If so, it prints out 'Stack empty.' otherwise it prints out the last item on the Stack (the result).
  private void printOutResult (){
    if (!numStack.empty()){
      System.out.println(numStack.peek());
    }
    else{
      System.out.println("Stack empty.");
    }
  }

  


  //Helper method checks if user Input is an interger
  private boolean isInteger(String userInput){
  if (userInput.matches("[0-9]+")) {
    return true;
  }
    return false;
  }

  /*Method converts String into an integer, saves it in 'number' and pushes to numStack. Method catches 'NumberFormatException; and pushes the minimum or Maximum integer value, depending on the original input of the user.
  */
  public void addToStack (String s){
    if(numStack.size()<23)
      try{
        int number = Integer.parseInt(s);
        numStack.push(number);
        
      }
      catch (NumberFormatException e){
       if(s.charAt(0)== '-'){
          numStack.push(Integer.MIN_VALUE);
        }
        else{
          numStack.push(Integer.MAX_VALUE);
        }
      }
    else{
      System.out.println("Stack overflow.");
    }
  }
}
