//Harlan De Jong, c3349828 COMP2230 A1
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.lang.Math;
import java.util.Scanner;
import java.util.Stack;


public class MazeGenerator {
    public static void main(String[] args) { 
        Scanner Read = new Scanner(System.in);
        
        System.out.println("input number of rows: ");
        int m = Read.nextInt();
        System.out.println("input number of columns: "); //asks user for size of maze
        int n = Read.nextInt();
        Read.close();
        DFSMaze(m,n);
    }

    public static void DFSMaze(int m, int n){ //function to generate a ranom maze using dfs and stack
        Stack<Integer> stack = new Stack<Integer>();
        int[][] maze = new int[m][n];
        int[][] visited = new int[m][n];
        int[][] direction = new int[m][n]; //initialisation
        int i=1;
        int currentCell = 0;
        int newNeighbour = 0;

        for(int a=0;a<m;a++){
            for(int b=0;b<n;b++){ //initialisation
                maze[a][b] = i++;
                visited[a][b] = 0;
                direction[a][b] = 0;
            }
        }

        int startNode = (int)((Math.random()*(n*m))+1);   //random start node

        visited[getRow(startNode, n)][getColumn(startNode, n)] = 1; //showing start node has been visited
        stack.push(startNode);

        while(!stack.empty()){ //while the stack isnt empty
            currentCell = stack.pop();

            if(getVisitedCheck(currentCell, n, m, visited)){ //if there is a possible neighbour
                stack.push(currentCell); //pushes current cell to stack
                newNeighbour = getNeighbourRandom(currentCell, n, m, visited, maze); //finds new neighbour for current cell randomly
                direction = updateDirection(newNeighbour, currentCell, direction, n); //updates direction according to positioning of neighbour and currentcell
                visited[getRow(newNeighbour, n)][getColumn(newNeighbour, n)] = 1; //showing neighbour has been visited
                stack.push(newNeighbour);
            }
        }
        
        try {
            FileWriter myWriter = new FileWriter("maze.txt");
            myWriter.write(n+","+m+":"+startNode+":"+newNeighbour+":"); //writing all maze info to file in correct format to "maze.txt"
            for(int k=0;k<m;k++){
                for(int l=0;l<n;l++){
                    myWriter.write(direction[k][l]+"");   
                }
            }
            myWriter.close();
        } 
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
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

    public static boolean getVisitedCheck(int current, int colNum, int rowNum, int[][] visited){ //checks if there is a possible loaction for a neighbouring node for current cell
        if((getColumn(current, colNum) != 0 && visited[getRow(current, colNum)][getColumn(current, colNum)-1] == 0) || (getColumn(current, colNum) != colNum-1 && visited[getRow(current, colNum)][getColumn(current, colNum)+1] == 0) || (getRow(current, colNum) != 0 && visited[getRow(current, colNum)-1][getColumn(current, colNum)] == 0) || (getRow(current, colNum) != rowNum-1 && visited[getRow(current, colNum)+1][getColumn(current, colNum)] == 0)){
            return true; //if one is found return true
        }   
        return false;
    }

    public static int getNeighbourRandom(int current, int colNum, int rowNum, int[][] visited, int[][] maze){ //finds a random possible neighbour for current cell
        int neighbour = 0;
        int i = 0;
        int[] neighbouring = new int[4];
        if(getColumn(current, colNum) != 0 && visited[getRow(current, colNum)][getColumn(current, colNum)-1] == 0){ //if it is not a left most node and it has not been visited then a possible neighbour location may be possible
            neighbouring[i] = maze[getRow(current, colNum)][getColumn(current, colNum)-1];
            i++;
        }
        if(getColumn(current, colNum) != colNum-1 && visited[getRow(current, colNum)][getColumn(current, colNum)+1] == 0){ //similar to above, looking to the right for neighbour
            neighbouring[i] = maze[getRow(current, colNum)][getColumn(current, colNum)+1];
            i++;
        }
        if(getRow(current, colNum) != 0 && visited[getRow(current, colNum)-1][getColumn(current, colNum)] == 0){ //similar to above, looking up for neighbour
            neighbouring[i] = maze[getRow(current, colNum)-1][getColumn(current, colNum)];
            i++;
        }
        if(getRow(current, colNum) != rowNum-1 && visited[getRow(current, colNum)+1][getColumn(current, colNum)] == 0){ //similar to above, looking down for neighbour
            neighbouring[i] = maze[getRow(current, colNum)+1][getColumn(current, colNum)];
        }
        int rand = (int)((Math.random()*(i)));
        neighbour = neighbouring[rand]; //chooses a random neighbour out of the possible candidates
        
        return neighbour;
    }

    public static int[][] updateDirection(int neighbour, int current, int[][] dir, int colNum){ //updates the direction for either neighbour or current depending on -> 0=both closed, 1=right open only, 2=down open only, 3=both open
        if(dir[getRow(current, colNum)][getColumn(current, colNum)] == 1 && getRow(current, colNum) < getRow(neighbour, colNum)){ //if current is below neighbour and right already
            dir[getRow(current, colNum)][getColumn(current, colNum)] = 3;
        }
        else if(dir[getRow(current, colNum)][getColumn(current, colNum)] == 2 && getColumn(current, colNum) < getColumn(neighbour, colNum)){//if current is to the right of neighbour and below already
            dir[getRow(current, colNum)][getColumn(current, colNum)] = 3;
        }
        else if(getRow(current, colNum) < getRow(neighbour, colNum)){ //if current is above neighbour only
            dir[getRow(current, colNum)][getColumn(current, colNum)] = 2;
        }
        else if(getColumn(current, colNum) < getColumn(neighbour, colNum)){ //if current is to the left of neighbour only
            dir[getRow(current, colNum)][getColumn(current, colNum)] = 1;
        }
        else if(getRow(current, colNum) > getRow(neighbour, colNum)){ //if neighbour is above current only
            dir[getRow(neighbour, colNum)][getColumn(neighbour, colNum)] = 2;
        }
        else if(getColumn(current, colNum) > getColumn(neighbour, colNum)){ //if neighbour is to the left of current only
            dir[getRow(neighbour, colNum)][getColumn(neighbour, colNum)] = 1;
        }

        return dir;
    }
}

