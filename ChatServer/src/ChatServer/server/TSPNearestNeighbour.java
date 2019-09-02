package ChatServer.server;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Stack;

public class TSPNearestNeighbour {
    private int numberOfNodes;
    private Stack<Integer> stack;

    public TSPNearestNeighbour() {
        stack = new Stack<Integer>();
    }

    public int[] tsp(int adjacencyMatrix[][]) {
        numberOfNodes = adjacencyMatrix[1].length - 1;
        int[] visited = new int[numberOfNodes + 1];
        int[] answerArr = new int[numberOfNodes];
        int k = 1;
        visited[1] = 1;
        stack.push(1);
        int element, dst = 0, i;
        int min;
        boolean minFlag = false;
        System.out.print(1 + "\t");
        answerArr[0] = 0;
        while (!stack.isEmpty()) {
            element = stack.peek();
            i = 1;
            min = Integer.MAX_VALUE;
            while (i <= numberOfNodes) {
                if (adjacencyMatrix[element][i] > 1 && visited[i] == 0) {
                    if (min > adjacencyMatrix[element][i]) {
                        min = adjacencyMatrix[element][i];
                        dst = i;
                        minFlag = true;
                    }
                }
                i++;
            }
            if (minFlag) {
                visited[dst] = 1;
                stack.push(dst);
                System.out.print(dst + "\t");
                answerArr[k] = dst -1 ;
                k++;
                minFlag = false;
                continue;
            }
            stack.pop();
        }
        return answerArr;
    }

    public int[] main(int matrix[][]) {
        int number_of_nodes = 4;
        int [] arr;
        Scanner scanner = null;
        try {
            //System.out.println("Enter the number of nodes in the graph");
            //scanner = new Scanner(System.in);
            //number_of_nodes = scanner.nextInt();
            int adjacency_matrix[][] = new int[number_of_nodes + 1][number_of_nodes + 1];
            //System.out.println("Enter the adjacency matrix");
            for (int i = 1; i <= number_of_nodes; i++) {
                for (int j = 1; j <= number_of_nodes; j++) {
                    adjacency_matrix[i][j] = matrix[i-1][j-1];
                }
            }
            for (int i = 1; i <= number_of_nodes; i++) {
                for (int j = 1; j <= number_of_nodes; j++) {
                    if (adjacency_matrix[i-1][j-1] == 1 && adjacency_matrix[j-1][i-1] == 0) {
                        adjacency_matrix[j-1][i-1] = 1;
                    }
                }
            }
            System.out.println("the nodes are visited as follows");
            TSPNearestNeighbour tspNearestNeighbour = new TSPNearestNeighbour();
            arr = tspNearestNeighbour.tsp(adjacency_matrix);
        } catch (InputMismatchException inputMismatch) {
            System.out.println("Wrong Input format");
            return null;
        }
//        scanner.close();
        return arr;
    }
}
