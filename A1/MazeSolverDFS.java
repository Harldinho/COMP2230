//Harlan De Jong, c3349828 COMP2230 A1
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;
import java.lang.Math;
import java.util.Scanner;
import java.util.Stack;

public class MazeSolverDFS {
    public static void main(String[] args) { //driver code useds for initialisation primarily
        Scanner Read = new Scanner(System.in);
        String[] splitData = new String[5];
        String data = ""; 
        
        System.out.println("input file name: (maze.txt)");
        String mazeData = Read.nextLine(); //retreiving file name for input
        Read.close();

        try {
            File myObj = new File(mazeData);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) { //reading data from file
                data = myReader.nextLine();
            }
            myReader.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        splitData = data.split("[,:]");
        
        MazeSolver(Integer.parseInt(splitData[0]), Integer.parseInt(splitData[1]), Integer.parseInt(splitData[2]), Integer.parseInt(splitData[3]), splitData[4]); //calling mazesolver with file data
    }
    public static void MazeSolver(int colNum, int rowNum, int start, int finish, String openness){ //function to solve maze, using dfs and stack
        long startTime = System.currentTimeMillis();
        Stack<Integer> stack = new Stack<Integer>();
        String[] opennessArr = new String[colNum*rowNum];
        int[] visited = new int[colNum*rowNum+1];
        boolean found = false;
        int[] solution = new int[colNum*rowNum*rowNum];
        int count = 1;
        
        opennessArr = openness.split(""); //splitting for easier use
        for(int i=0;i<colNum*rowNum;i++){
            visited[i] = 0;
        }

        visited[start] = 1; //initialising start node on stack
        stack.push(start);
        int next = start;
        solution[0] = start;
        while(found == false){  //runs until last node is found
            if(stack.peek() == finish){ 
                found = true;
                break;
            }
            next = direction(next, opennessArr, rowNum, colNum, visited); //finds next node to traverse
            if(next == -1){
                stack.pop();
                next = stack.peek(); //if there are no more possible routes, backtrack
            }
            else{
                visited[next] = 1;
                stack.push(next);  //pushes next node to stack
            }
            solution[count] = next; //stores path taken
            count++;
        }
        System.out.print("Solution: " );
        for(int a=0;a<count;a++){
            System.out.print(solution[a]+", "); 
        }
        System.out.println("\nsteps: "+(count-1));
        System.out.println("Elapsed Time: "+(System.currentTimeMillis() - startTime)+"ms");
    }


     public static int direction(int num, String[] opennessArr, int rowNum, int colNum, int[] visited){ //finds the most optimal next node according to node direction and lower node value policy first
        int dir = -1;
        if(getRow(num, colNum) > 0 && (opennessArr[num-colNum-1].equals("2") || opennessArr[num-colNum-1].equals("3")) && visited[num-colNum] == 0){
            return num-colNum;
        }
        else if(getColumn(num, colNum) > 0 && (opennessArr[num-1-1].equals("1") || opennessArr[num-1-1].equals("3")) && visited[num-1] == 0){
            return num-1;
        }
        else if((opennessArr[num-1].equals("1") || opennessArr[num-1].equals("3")) && visited[num+1] == 0){
            return num+1;
        }
        else if((opennessArr[num-1].equals("2") || opennessArr[num-1].equals("3")) && visited[num+colNum] == 0){
            return num+colNum;
        }

        return dir;
    }

    public static int getColumn(int num, int colNum){ //retrieves the column number of a given node

        if(num%colNum != 0){
            return (num%colNum)-1;
        }
        
        return colNum-1;
    }

    public static int getRow(int num, int colNum){ //retrieves the row number of a given node
        if((num%colNum) == 0){
            return ((int)Math.floor(num/colNum))-1; 
        }
        return (int)Math.floor(num/colNum);
    }
}
